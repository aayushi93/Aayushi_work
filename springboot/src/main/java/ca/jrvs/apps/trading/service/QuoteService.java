package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class QuoteService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private IexQuote iexQuote;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteService(MarketDataDao marketDataDao) {
//        this.iexQuote = iexQuote;
        this.marketDataDao = marketDataDao;
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
}