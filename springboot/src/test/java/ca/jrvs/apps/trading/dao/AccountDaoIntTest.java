package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
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
public class AccountDaoIntTest {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private Account account;
    private Trader savedTrader;

    public AccountDaoIntTest(AccountDao accountDao, TraderDao traderDao) {
        this.accountDao = accountDao;
        this.traderDao = traderDao;
    }

    @Before
    public void insertOne() {
        savedTrader = new Trader();
        savedTrader.setCountry("Canada");
        savedTrader.setDob(new Date(1993, 03, 06));
        savedTrader.setEmail("abc2020@jarvis.com");
        savedTrader.setFirstName("Aay");
        savedTrader.setLastName("Mathur");
        savedTrader.setId(traderDao.save(savedTrader).getId());
        account = new Account();
        account.setTraderId(savedTrader.getId());
        account.setAmount(800d);
        account.setId(accountDao.save(account).getId());
    }

    @Test
    public void findAllById() {
        List<Account> accounts = Lists.newArrayList(accountDao.findAllById(Arrays.asList(account.getId())));
        assertEquals(1, accounts.size());
        assertEquals(account.getAmount(), accounts.get(0).getAmount());
        assertEquals(account.getTraderId(), accounts.get(0).getTraderId());
    }

    @Test
    public void update() {
        account.setAmount(500d);
        Account accountWithdraw = accountDao.save(account);
        assertEquals(account.getAmount(), accountWithdraw.getAmount());
    }

    @After
    public void deleteOne() {
        accountDao.deleteById(account.getId());
        traderDao.deleteById(savedTrader.getId());
    }
}
