package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.PortfolioView;
import ca.jrvs.apps.trading.model.view.TraderAccountViewDashBoard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DashBoardServiceUnitTest {

    @Mock
    private AccountDao accountDao;
    @Mock
    private TraderDao traderDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private PositionDao positionDao;
    @InjectMocks
    private DashBoardService dashBoardService;

    @Before
    public void setup() {
        Quote quote1 = new Quote();
        quote1.setBidSize(15);
        quote1.setAskSize(15);
        quote1.setBidPrice(100d);
        quote1.setAskPrice(150d);
        quote1.setTicker("AMZN");
        quote1.setLastPrice(119d);
        when(quoteDao.findById("AMZN")).thenReturn(Optional.of(quote1));
        Quote quote2 = new Quote();
        quote2.setBidSize(20);
        quote2.setAskSize(20);
        quote2.setBidPrice(110d);
        quote2.setAskPrice(180d);
        quote2.setTicker("JRVS");
        quote2.setLastPrice(132d);
        when(quoteDao.findById("JRVS")).thenReturn(Optional.of(quote2));

        Account account = new Account();
        account.setAmount(2000d);
        account.setId(12);
        account.setTraderId(7);
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);
        when(accountDao.findRowByColumnId(any(), any())).thenReturn(accountList);

        Position position1 = new Position();
        position1.setPosition(11);
        position1.setTicker("AMZN");
        position1.setAccountId(12);
        Position position2 = new Position();
        position2.setPosition(13);
        position2.setTicker("JRVS");
        position2.setAccountId(12);
        List<Position> positionList = new ArrayList<>();
        positionList.add(position1);
        positionList.add(position2);
        when(positionDao.findRowByColumnId(any(), any())).thenReturn(positionList);

        Trader trader = new Trader();
        trader.setCountry("Canada");
        trader.setDob(new Date(1993, 03, 06));
        trader.setEmail("jrvs@gmail.com");
        trader.setFirstName("Aayushi");
        trader.setLastName("Mat");
        trader.setId(7);
        when(traderDao.findById(any())).thenReturn(Optional.of(trader));
    }

    @Test
    public void getTraderAccount() {
        TraderAccountViewDashBoard traderAccountViewDashBoard = dashBoardService.getTraderAccount(7);
        assertEquals(traderAccountViewDashBoard.getAccount().getAmount(), (Double) 1000d);
        assertEquals(traderAccountViewDashBoard.getAccount().getId(), (Integer) 12);
        assertEquals(traderAccountViewDashBoard.getTrader().getCountry(), "Canada");
    }

    @Test
    public void getProfileViewByTraderId() {
        PortfolioView portfolioView = dashBoardService.getProfileViewByTraderId(7);
        Quote quoteOne = portfolioView.getSecurityRowList().get(0).getQuote();
        Quote quoteTwo = portfolioView.getSecurityRowList().get(1).getQuote();
        assertEquals("AMZN", quoteOne.getTicker());
        assertEquals("FB", quoteTwo.getTicker());
        Position positionOne = portfolioView.getSecurityRowList().get(0).getPosition();
        Position positionTwo = portfolioView.getSecurityRowList().get(1).getPosition();
        assertEquals((Integer) 11, positionOne.getPosition());
        assertEquals((Integer) 13, positionTwo.getPosition());
    }
}
