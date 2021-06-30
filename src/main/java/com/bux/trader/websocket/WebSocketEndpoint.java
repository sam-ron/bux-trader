package com.bux.trader.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WebSocketEndpoint extends Endpoint{

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Gson gson = new Gson();

    private WebSocketMessageHandler wsMessageHandler;

    public WebSocketEndpoint(WebSocketMessageHandler wsMessageHandler) {
        this.wsMessageHandler = wsMessageHandler;
    }

    @Override
    public void onOpen(Session session, javax.websocket.EndpointConfig config) {

        MessageHandler.Whole<String> messageHandler = new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                JsonObject jsonElement = gson.fromJson(message, JsonObject.class);
                wsMessageHandler.handleMessage(jsonElement, session);
            }
        };
        session.addMessageHandler(messageHandler);
    }


}
