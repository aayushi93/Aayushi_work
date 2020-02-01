package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class QuoteService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
    
    private MarketDataDao marketDataDao;
    private QuoteDao quoteDao;

    @Autowired
    public QuoteService(MarketDataDao marketDataDao, QuoteDao quoteDao) {
        this.marketDataDao = marketDataDao;
        this.quoteDao = quoteDao;
    }

    public QuoteService(MarketDataDao marketDataDao) {
    }

    /**
     * Find an IexQuote
     *
     * @param ticker ID
     * @return IexQuote object
     * @throws IllegalArgumentException
     */
    public IexQuote findIexQuoteById(String ticker) {
        return marketDataDao.findById(ticker)
                .orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid!"));
    }

    /**
     * Update Quote table against Iex source
     * - get all quotes from the db
     * - foreach ticker get iexQuote
     * - convert iexQuote to quoteEntity
     * - persist quote to db
     *
     * @throws if                                          ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException                    for invalid input
     * @return
     */
    public List<Quote> updateMarketData() {
        List<Quote> quotes = quoteDao.findAll();

        List<Quote> allQuotes = new ArrayList<>();
        IexQuote iexQuote;
        Quote savedQuotes;
        for (Quote quote : quotes) {
            String ticker = quote.getId();
            iexQuote = findIexQuoteById(ticker);
            savedQuotes = buildQuoteFromIexQuote(iexQuote);
            allQuotes.add(savedQuotes);
        }
        quoteDao.saveAll(allQuotes);


        return quotes;
    }

    public List<Quote> saveQuotes(List<String> tickers) {
        List<Quote> quotes = new ArrayList<>();
        for (String ticker : tickers) {
            quotes.add(saveQuotes(ticker));
        }
        return quotes;
    }

    /**
     * Helper method for saveQuotes
     *
     * @param ticker
     * @return quote
     */
    private Quote saveQuotes(String ticker) {
        IexQuote iexQuote;
        iexQuote = marketDataDao.findById(ticker).get();
        Quote quote = buildQuoteFromIexQuote(iexQuote);
        return saveQuote(quote);
    }

    public Quote saveQuote(Quote quote) {
        return quoteDao.save(quote);
    }

    public List<Quote> findAllQuotes() {
        return quoteDao.findAll();
    }

    protected Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
        Quote quote = new Quote();
        quote.setTicker(iexQuote.getSymbol());
        quote.setLastPrice((double) iexQuote.getLatestPrice());
        quote.setAskPrice((double) iexQuote.getIexAskPrice());
        quote.setAskSize((int) iexQuote.getIexAskSize());
        quote.setBidPrice((double) iexQuote.getIexBidPrice());
        quote.setBidSize((int) iexQuote.getIexBidSize());
        return quote;


    }
}
