package com.bux.trader.service;

import com.bux.trader.config.TradeConfig;
import com.bux.trader.config.exception.PriceConfigurationException;
import com.bux.trader.repository.entity.TradePosition;
import com.bux.trader.rest.controller.TradeController;
import com.bux.trader.rest.entity.Amount;
import com.bux.trader.rest.entity.BuyResponse;
import com.bux.trader.rest.entity.SellResponse;
import com.bux.trader.rest.entity.TradeQuote;
import com.bux.trader.repository.TradeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.Mockito.when;

@TestPropertySource(locations="classpath:application-test.properties")
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class TradeServiceTest {

    @Mock
    private TradeController tradeController;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private TradeConfig tradeConfig;

    @InjectMocks
    private TradeService tradeService;


    /**
     * Test that buy order flow is executed correctly
     */
    @Test
    public void buyOrderTest(){
        BuyResponse testResponse = new BuyResponse();
        testResponse.setPositionId("testId");

        TradeQuote quote = new TradeQuote("testId", 750.00F);

        when(tradeConfig.getBuyLowerLimit()).thenReturn(500F);
        when(tradeConfig.getBuyUpperLimit()).thenReturn(1000F);
        when(tradeRepository.findByProductId(quote.getSecurityId())).thenReturn(null);
        when(tradeController.buyOrder(any())).thenReturn(testResponse);

        tradeService.processQuote(quote);

        verify(tradeRepository, times(1)).save(any());
        verify(tradeController, times(1)).buyOrder(any());

    }

    /**
     * Test that sell order flow is executed correctly
     */
    @Test
    public void sellOrderTest(){
        SellResponse testResponse = new SellResponse();
        testResponse.setId("testId");
        testResponse.setPrice(new Amount("BUX", 2, 1100F));

        TradePosition testPosition = new TradePosition("testProductId", 750.00F, 1000F ,500F, 100D);

        TradeQuote quote = new TradeQuote("testPositionId", 1100.00F);

        when(tradeConfig.getBuyLowerLimit()).thenReturn(500F);
        when(tradeConfig.getBuyUpperLimit()).thenReturn(1000F);
        when(tradeRepository.findByProductId(quote.getSecurityId())).thenReturn(testPosition);
        when(tradeRepository.findByProductId(quote.getSecurityId())).thenReturn(testPosition);
        when(tradeController.sellOrder(any())).thenReturn(testResponse);

        tradeService.processQuote(quote);

        verify(tradeRepository, times(1)).delete(any());
        verify(tradeController, times(1)).sellOrder(any());

    }

    /**
     * Test exception is thrown when pricing is misconfigured
     */
    @Test(expected = PriceConfigurationException.class)
    public void testPriceConfigurationExceptionThrown(){
        TradeQuote quote = new TradeQuote("testPositionId", 1100.00F);
        when(tradeConfig.getBuyLowerLimit()).thenReturn(5000F);
        when(tradeConfig.getBuyUpperLimit()).thenReturn(1000F);

        tradeService.processQuote(quote);
    }

}