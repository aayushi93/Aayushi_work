package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.*;
import org.assertj.core.util.Lists;
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

public class PositionDaoInt {

    @Autowired
    private PositionDao positionDao;

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
    private Position position;

    public PositionDaoInt(PositionDao positionDao, SecurityOrderDao securityOrderDao,
                          AccountDao accountDao, TraderDao traderDao, QuoteDao quoteDao) {
        this.positionDao = positionDao;
        this.securityOrderDao = securityOrderDao;
        this.accountDao = accountDao;
        this.traderDao = traderDao;
        this.quoteDao = quoteDao;
    }

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
        savedTrader.setFirstName("John");
        savedTrader.setLastName("Tan");
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
        position = new Position();
        position.setAccountId(account.getId());
        position.setTicker(savedQuote.getTicker());
        position.setPosition(securityOrder.getSize());
    }

    @Test
    public void findAllById() {
        List<Position> positions = Lists.newArrayList(positionDao.findAllById(Arrays.asList(account.getId())));
        assertEquals(1, positions.size());
        assertEquals(position.getAccountId(), positions.get(0).getAccountId());
        assertEquals(savedQuote.getTicker(), positions.get(0).getTicker());
        assertEquals(securityOrder.getSize(), positions.get(0).getPosition());
    }

    @Test
    public void delete() {
        securityOrderDao.deleteById(securityOrder.getId());
        accountDao.deleteById(account.getId());
        traderDao.deleteById(savedTrader.getId());
    }
}
