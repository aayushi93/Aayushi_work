package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest {
    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteDao quoteDao;
    private Quote savedQuoteDao;

    @Before
    public void setup() {
        savedQuoteDao = new Quote();
        insertOne();
        insertTwo();
//      insertThree();

    }

    public void insertOne() {
        savedQuoteDao.setAskPrice(10d);
        savedQuoteDao.setAskSize(10);
        savedQuoteDao.setBidPrice(10.2d);
        savedQuoteDao.setBidSize(10);
        savedQuoteDao.setId("AAPL");
        savedQuoteDao.setLastPrice(10.1d);
        quoteDao.save(savedQuoteDao);

    }

    private void insertTwo() {
        List<Quote> insertQuoteTwo = new ArrayList<>();
        Quote quoteOne = new Quote();
        quoteOne.setAskPrice(12d);
        quoteOne.setAskSize(12);
        quoteOne.setBidPrice(12.2d);
        quoteOne.setBidSize(12);
        quoteOne.setId("TD");
        quoteOne.setLastPrice(12.1d);
        insertQuoteTwo.add(quoteOne);
        Quote quoteTwo = new Quote();
        quoteTwo.setAskPrice(12d);
        quoteTwo.setAskSize(12);
        quoteTwo.setBidPrice(12.2d);
        quoteTwo.setBidSize(12);
        quoteTwo.setId("FB");
        quoteTwo.setLastPrice(12.1d);
        insertQuoteTwo.add(quoteTwo);
        quoteDao.saveAll(insertQuoteTwo);
    }

    @Test
    public void findIexQuoteById() {
        IexQuote iexQuote = quoteService.findIexQuoteById("AAPL");
        assertNotNull(iexQuote);
    }

    @Test
    public void findById() {
        String ticker = savedQuoteDao.getId();
        Optional<Quote> quote = quoteDao.findById(ticker);
        assertEquals(quote.get().getAskPrice(), savedQuoteDao.getAskPrice());
        assertEquals(quote.get().getAskSize(), savedQuoteDao.getAskSize());
        assertEquals(quote.get().getBidPrice(), savedQuoteDao.getBidPrice());
        assertEquals(quote.get().getBidSize(), savedQuoteDao.getBidSize());
        assertEquals(quote.get().getId(), savedQuoteDao.getId());
        assertEquals(quote.get().getLastPrice(), savedQuoteDao.getLastPrice());

    }

    @Test
    public void findAll() {
        String ticker = savedQuoteDao.getId();
        List<Quote> quoteList = quoteDao.findAll();
        assertEquals(quoteList.get(0).getAskPrice(), savedQuoteDao.getAskPrice());
        assertEquals(quoteList.get(0).getAskSize(), savedQuoteDao.getAskSize());
        assertEquals(quoteList.get(0).getBidPrice(), savedQuoteDao.getBidPrice());
        assertEquals(quoteList.get(0).getBidSize(), savedQuoteDao.getBidSize());
        assertEquals(quoteList.get(0).getId(), savedQuoteDao.getId());
        assertEquals(quoteList.get(0).getLastPrice(), savedQuoteDao.getLastPrice());

    }

    @Test
    public void existsById() {
        assertTrue(quoteDao.existsById(savedQuoteDao.getId()));
        assertFalse(quoteDao.existsById("PPPP"));
    }

    @Test
    public void count() {
        assertEquals(3, quoteDao.count());
    }

    @Test
    public void saveQuotes() {
        List<String> tickers = new ArrayList<>();
        tickers.add("JNJ");
        tickers.add("AAPL");
        tickers.add("FB");
        quoteService.saveQuotes(tickers);
        assertEquals(4, quoteDao.count());
    }



    @Test
    public void unimplemented() {
        try {
            quoteDao.delete(savedQuoteDao);
        } catch (UnsupportedOperationException ex) {
            assertTrue(true);
        }
        try {
            quoteDao.deleteAll(new ArrayList<Quote>());
        } catch (UnsupportedOperationException ex) {
            assertTrue(true);
        }
        try {
            quoteDao.findAllById(new ArrayList<String>());
        } catch (UnsupportedOperationException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void delete() {
        quoteDao.deleteById(savedQuoteDao.getId());
        assertEquals(2, quoteDao.count());
        quoteDao.deleteAll();
        assertEquals(0, quoteDao.count());
        try {
            quoteDao.existsById("AAPL");
        } catch (NullPointerException e) {
            assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void saveQuote() {
        quoteService.saveQuote(savedQuoteDao);
    }



}
