package com.bux.trader.websocket;

import javax.websocket.ClientEndpointConfig;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WebSocketConfigurator extends ClientEndpointConfig.Configurator{

    private String authToken;
    private String lang;

    public WebSocketConfigurator(String authToken, String lang) {
        this.authToken = authToken;
        this.lang = lang;
    }

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        headers.putIfAbsent("Authorization", Collections.singletonList(authToken));
        headers.putIfAbsent("Accept-Language", Collections.singletonList(lang));
        super.beforeRequest(headers);
    }
}
