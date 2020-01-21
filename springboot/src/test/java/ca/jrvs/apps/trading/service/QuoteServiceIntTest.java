package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteDao quoteDao;
//    private Quote savedQuote;

    @Before
    public void setup() throws Exception {
        quoteDao.deleteAll();

        Quote quote1 = new Quote();
        Quote quote2 = new Quote();
//        Quote quote3 = new Quote();


        quote1.setId("AAPL");
        quote1.setLastPrice(11.1d);
        quote1.setBidPrice(11.2d);
        quote1.setBidSize(11);
        quote1.setAskPrice(11d);
        quote1.setAskSize(11);
        quoteDao.save(quote1);


        quote2.setId("JNJ");
        quote2.setLastPrice(10.1d);
        quote2.setBidPrice(10.2d);
        quote2.setBidSize(10);
        quote2.setAskPrice(10d);
        quote2.setAskSize(10);
        quoteDao.save(quote2);
    }

    @Test
    public void findIexQuoteByTicker() {
        IexQuote iexQuote = quoteService.findIexQuoteById("JNJ");
        assertEquals("JNJ", iexQuote.getSymbol());
    }

    @Test
    public void updateMarketData() {
        quoteService.updateMarketData();
        assertTrue(quoteDao.existsById("JNJ"));
        assertFalse(quoteDao.existsById("Invalid"));
    }

    @Test
    public void saveQuotes() {
        List<String> tickerList = new ArrayList<>();
        tickerList.add("AAPL");
        tickerList.add("JNJ");
        quoteService.saveQuotes(tickerList);
        assertEquals(4, quoteDao.count());

    }

//    @Test
//    public void saveQuote() {
//        quoteService.saveQuote(savedQuote);
//        assertEquals(2, quoteDao.count());
//
//    }

    @Test
    public void findAllQuotes() {
        List<Quote> quotes = quoteService.findAllQuotes();
        assertEquals(1, quoteDao.count());
    }

    @Test
    public void clean() throws Exception {
        quoteDao.deleteAll();
    }
}
