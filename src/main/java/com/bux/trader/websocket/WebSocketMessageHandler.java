package com.bux.trader.websocket;

import com.bux.trader.config.exception.PriceConfigurationException;
import com.bux.trader.entity.rest.TradeQuote;
import com.bux.trader.service.SubscriptionService;
import com.bux.trader.service.TradeService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;

@Service
public class WebSocketMessageHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private static final String CONNECT_CONNECTED = "connect.connected";
    private static final String TRADING_QUOTE = "trading.quote";

    private Gson gson = new Gson();

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TradeService tradeService;

    private boolean recheckPosition = true;

    public void handleMessage(JsonObject jsonObject, Session session) {
        String message = getMessage(jsonObject);

        try {
            if (CONNECT_CONNECTED.equals(message)) {
                subscriptionService.subscribeToProductChannel(session);
            }
            if (recheckPosition && TRADING_QUOTE.equals(message)) {
                TradeQuote tradeQuote = gson.fromJson(jsonObject.get("body"), TradeQuote.class);
                recheckPosition = tradeService.processQuote(tradeQuote);

                if(!recheckPosition){
                    subscriptionService.unsubscribeFromProductChannel(session);
                    session.close();
                }
            }
        } catch (IOException e) {
            log.error("Error processing quote: " + e);
        }

    }

    private String getMessage(JsonObject jsonObject){
        JsonElement jsonElement = jsonObject.get("t");
        return jsonElement.getAsString();
    }
}