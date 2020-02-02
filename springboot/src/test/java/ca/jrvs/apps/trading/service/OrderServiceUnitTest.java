package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {

    @Captor
    ArgumentCaptor<SecurityOrder> captorSecurityOrder;

    @Mock
    private AccountDao accountDao;
    @Mock
    private SecurityOrderDao securityOrderDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private PositionDao positionDao;

    @InjectMocks
    private OrderService orderService;

    @Before
    public void setup() {
        Quote quote = new Quote();
        quote.setBidPrice(150d);
        quote.setBidSize(20);
        quote.setAskSize(20);
        quote.setAskPrice(200d);
        quote.setTicker("FB");
        quote.setLastPrice(120d);
        when(quoteDao.findById("FB")).thenReturn(Optional.of(quote));

        Account account = new Account();
        account.setAmount(2000d);
        account.setId(10);
        account.setTraderId(5);
        when(accountDao.findById(any())).thenReturn(Optional.of(account));

        List<Position> listPosition = new ArrayList<>();
        Position position = new Position();
        position.setPosition(7);
        position.setTicker("FB");
        position.setAccountId(10);
        when(positionDao.findRowByColumnId(any(), any())).thenReturn(listPosition);

        Account withdrawAmount = new Account();
        withdrawAmount.setAmount(2000d - 800d);
        withdrawAmount.setTraderId(5);
        withdrawAmount.setId(10);
        when(accountDao.save(any())).thenReturn(withdrawAmount);

        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setStatus("Completed");
        securityOrder.setId(20);
        securityOrder.setAccountId(12);
        securityOrder.setSize(10);
        securityOrder.setTicker("FB");
        securityOrder.setPrice(150d);
        when(securityOrderDao.save(any())).thenReturn(securityOrder);
    }

    @Test
    public void buyApproved() {
        MarketOrderDto marketOrderDto = new MarketOrderDto();
        marketOrderDto.setAccountId(10);
        marketOrderDto.setSize(5);
        marketOrderDto.setTicker("FB");
        SecurityOrder securityOrderWithdraw = orderService.executeMarketOrder(marketOrderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        assertEquals((Integer) 22, captorSecurityOrder.getValue().getId());
        assertEquals(captorSecurityOrder.getAllValues().get(1).getStatus(), "FILLED");
    }

    @Test
    public void buyRejected() {
        MarketOrderDto marketOrderDto = new MarketOrderDto();
        Account account = new Account();
        marketOrderDto.setSize(60);
        account.setAmount(200d);
        orderService.executeMarketOrder(marketOrderDto);
        verify(securityOrderDao, times(2)).save(captorSecurityOrder.capture());
        assertEquals(captorSecurityOrder.getAllValues().get(1).getStatus(), "REJECTED");
    }

    @Test
    public void sellApproved() {
        MarketOrderDto marketOrderDto = new MarketOrderDto();
        marketOrderDto.setAccountId(10);
        marketOrderDto.setSize(-9);
        marketOrderDto.setTicker("FB");
        SecurityOrder securityOrderWithdraw = orderService.executeMarketOrder(marketOrderDto);
        verify(securityOrderDao).save(captorSecurityOrder.capture());
        assertEquals((Integer) 22, captorSecurityOrder.getValue().getId());
        assertEquals(captorSecurityOrder.getAllValues().get(1).getStatus(), "FILLED");
    }

    @Test
    public void sellRejected() {
        MarketOrderDto marketOrderDto = new MarketOrderDto();
        Position position = new Position();
        position.setPosition(11);
        orderService.executeMarketOrder(marketOrderDto);
        verify(securityOrderDao, timeout(2)).save(captorSecurityOrder.capture());
        assertEquals(captorSecurityOrder.getAllValues().get(1).getStatus(), "REJECTED");
    }
}
