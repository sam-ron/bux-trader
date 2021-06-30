package com.bux.trader.websocket;

import com.bux.trader.entity.rest.TradeQuote;
import com.bux.trader.service.SubscriptionService;
import com.bux.trader.service.TradeService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

@Service
public class WebSocketMessageHandler {

    private static final String CONNECT_CONNECTED = "connect.connected";
    private static final String TRADING_QUOTE = "trading.quote";

    private Gson gson = new Gson();

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private TradeService tradeService;

    public void handleMessage(JsonObject jsonObject, Session session) {
        JsonElement jsonElement = jsonObject.get("t");
        if (jsonElement == null) {
            return;
        }

        String t = jsonElement.getAsString();

//        System.out.println(t);

//        JsonElement body = jsonObject.get("body");
//        System.out.println(body);

        if (CONNECT_CONNECTED.equals(t)) {
            subscriptionService.SubscribeToProductChannel(session);
        }

        if (TRADING_QUOTE.equals(t)) {
            TradeQuote tradeQuote = gson.fromJson(jsonObject.get("body"), TradeQuote.class);
            System.out.println("TRADING_QUOTE RECEIVED");
            tradeService.processQuote(tradeQuote);
        }

    }
}