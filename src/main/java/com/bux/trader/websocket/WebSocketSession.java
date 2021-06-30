package com.bux.trader.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;


@Configuration
public class WebSocketSession {

    @Value("${bux.auth.bearer.token}")
    private String authToken;

    @Value("${bux.auth.accept.lang}")
    private String lang;

    @Value("${bux.ws.url}")
    private String wsUri;

    @Autowired
    private WebSocketMessageHandler wsMessageHandler;

    @Bean
    public Session session() {
        try {
            WebSocketConfigurator webSocketConfigurator = new WebSocketConfigurator(authToken, lang);
            WebSocketEndpoint endpointConfig = new WebSocketEndpoint(wsMessageHandler);

            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create()
                    .configurator(webSocketConfigurator)
                    .build();

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            return container.connectToServer(endpointConfig, clientEndpointConfig, new URI(wsUri));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
