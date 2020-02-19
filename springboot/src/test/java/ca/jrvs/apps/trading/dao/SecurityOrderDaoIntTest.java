package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class SecurityOrderDaoIntTest {

    @Autowired
    private SecurityOrderDao securityOrderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private QuoteDao quoteDao;

    private Account account;
    private Trader savedTrader;
    private SecurityOrder securityOrder;
    private Quote savedQuote;

    @Before
    public void insertOne() {
        savedQuote = new Quote();
        savedQuote.setAskPrice(11d);
        savedQuote.setAskSize(11);
        savedQuote.setBidPrice(11.2d);
        savedQuote.setBidSize(11);
        savedQuote.setId("AMZN");
        savedQuote.setLastPrice(11.1d);
        quoteDao.save(savedQuote);
        savedTrader = new Trader();
        savedTrader.setCountry("USA");
        savedTrader.setDob(new Date(1993, 03, 06));
        savedTrader.setEmail("xyz2020@jarvis.com");
        savedTrader.setFirstName("Alk");
        savedTrader.setLastName("Mat");
        savedTrader.setId(traderDao.save(savedTrader).getId());
        account = new Account();
        account.setTraderId(savedTrader.getId());
        account.setAmount(2000d);
        account.setId(accountDao.save(account).getId());
        securityOrder = new SecurityOrder();
        securityOrder.setAccountId(account.getId());
        securityOrder.setPrice(500d);
        securityOrder.setSize(10);
        securityOrder.setStatus("Completed");
        securityOrder.setTicker(savedQuote.getTicker());
        securityOrder.setNotes("Not defined");
        securityOrder.setId(securityOrderDao.save(securityOrder).getId());
    }

    @Test
    public void findAllById() {
        List<SecurityOrder> securityOrders = Lists.newArrayList(securityOrderDao.findAllById
                (Arrays.asList(securityOrder.getId())));
        assertEquals(1, securityOrders.size());
        assertEquals(securityOrder.getAccountId(), securityOrders.get(0).getAccountId());
        assertEquals(securityOrder.getStatus(), securityOrders.get(0).getStatus());
        assertEquals(securityOrder.getTicker(), securityOrders.get(0).getTicker());
        assertEquals(securityOrder.getPrice(), securityOrders.get(0).getPrice());
        assertEquals(securityOrder.getSize(), securityOrders.get(0).getSize());
        assertEquals(securityOrder.getNotes(), securityOrders.get(0).getNotes());
    }

    @Test
    public void update() {
        securityOrder.setStatus("In progress");
        SecurityOrder securityOrderWithdraw = securityOrderDao.save(securityOrder);
        assertEquals(securityOrder.getStatus(), securityOrderWithdraw.getStatus());
    }

    @After
    public void deleteOne() {
        securityOrderDao.deleteById(securityOrder.getId());
        accountDao.deleteById(account.getId());
        traderDao.deleteById(savedTrader.getId());
    }
}