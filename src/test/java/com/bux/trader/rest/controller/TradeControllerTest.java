package com.bux.trader.rest.controller;


import com.bux.trader.rest.entity.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles({"test"})
public class TradeControllerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(9090));

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BUY_ENDPOINT = "/users/me/trades";
    private static final String SELL_ENDPOINT = "users/me/portfolio/positions/";

   // @Test
    public void buyOrder() {
        //given
        UrlPattern externalUrl = urlPathEqualTo(BUY_ENDPOINT);
        stubFor(post(externalUrl)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("buy_order_response.json")));

        //when
        ResponseEntity<BuyResponse> response = restTemplate.postForEntity(BUY_ENDPOINT, createTestBuyRequest(), BuyResponse.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


   // @Test
    public void sellOrder() {
    }

    private BuyRequest createTestBuyRequest(){
        return BuyRequest.builder()
                .productId("testProductId")
                .investingAmount(new Amount("BUX", 2, 100))
                .leverage(2)
                .direction(Direction.BUY)
                .source(new Source("OTHER"))
                .build();
    }
}