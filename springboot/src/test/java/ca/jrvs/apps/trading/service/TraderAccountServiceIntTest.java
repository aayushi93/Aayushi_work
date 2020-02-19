package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class TraderAccountServiceIntTest {

    private TraderAccountView traderAccountView;
    @Autowired
    private TraderAccountService traderAccountService;
    @Autowired
    private QuoteDao quoteDao;
    @Autowired
    private TraderDao traderDao;

    @Before
    public void setup() {
        Trader savedTrader = new Trader();
        savedTrader.setCountry("Canada");
        savedTrader.setDob(new Date(1993, 03, 06));
        savedTrader.setFirstName("Jim");
        savedTrader.setLastName("Mat");
        savedTrader.setEmail("jim.mat@jarvis.com");
        traderAccountView = traderAccountService.createTraderAndAccount(savedTrader);
        traderAccountService.deposit(traderAccountView.getTraderId(), 5000d);
    }

    @Test
    public void withdrawAmount() {
        traderAccountService.withdraw(traderAccountView.getTraderId(), 1500d);
        try {
            traderAccountService.deleteTraderId(traderAccountView.getTraderId());
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
        traderAccountService.withdraw(traderAccountView.getTraderId(), 1500d);
    }

    @Test
    public void quote() {
        Quote savedQuote = new Quote();
        savedQuote.setAskPrice(11d);
        savedQuote.setAskSize(11);
        savedQuote.setBidPrice(11.2d);
        savedQuote.setBidSize(11);
        savedQuote.setId("AMZN");
        savedQuote.setLastPrice(11.1d);
        quoteDao.save(savedQuote);

        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setAccountId(traderAccountView.getAccountId());
        securityOrder.setPrice(200d);
        securityOrder.setSize(10);
        securityOrder.setStatus("In progress");
        securityOrder.setTicker(savedQuote.getTicker());
        securityOrder.setNotes("Not applicable");
        securityOrder.setId(traderAccountService.getSecurityOrderDao().save(securityOrder).getId());
        try {
            traderAccountService.deleteTraderId(traderAccountView.getTraderId());
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
        securityOrder.setSize(-11);
        securityOrder.setId(null);
        traderAccountService.getSecurityOrderDao().save(securityOrder);
        traderAccountService.withdraw(traderAccountView.getTraderId(), 5000d);
    }

    @After
    public void delete() {
        traderAccountService.deleteTraderId(traderAccountView.getTraderId());
    }
}
