package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {
    private static final String IEX_BATCH_PATH = "stock/market/batch?symbols=%s&types=quote&token=";
    private static final int HTTP_OK = 200;
    private static String IEX_BATCH_URL;
    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
    private HttpClientConnectionManager httpClientConnectionManager;

    @Autowired
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager, MarketDataConfig marketDataConfig) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
    }


    @Override
    public <S extends IexQuote> S save(S s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Get an IexQuote (Helper method which class findAllById)
     *
     * @param ticker
     * @throws IllegalArgumentException      if a given ticker is invalid
     * @throws DataRetrievalFailureException if HTTP request failed
     */
    @Override
    public Optional<IexQuote> findById(String ticker) {
        Optional<IexQuote> iexQuote;
        List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

        if (quotes.size() == 0) {
            return Optional.empty();
        } else if (quotes.size() == 1) {
            iexQuote = Optional.of(quotes.get(0));
        } else {
            throw new DataRetrievalFailureException("Unexpected number of quotes");
        }
        return iexQuote;

    }

    @Override
    public boolean existsById(String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Iterable<IexQuote> findAll() {
        return null;
    }

    /**
     * Get quotes from Iex
     *
     * @param tickerList is a list of tickers
     * @return a list of IEX quote object
     * @throws IllegalArgumentException      if any ticker is invalid or ticker is empty
     * @throws DataRetrievalFailureException if HTTP request is failed
     */
    @Override
    public List<IexQuote> findAllById(Iterable<String> tickerList) throws IllegalArgumentException,
            DataRetrievalFailureException {
        int tickerNumber = 0;
        for (String ticker : tickerList) {
            if (!ticker.matches("[a-zA-Z]{2,4}")) {
                throw new IllegalArgumentException("Illegal ticker format!");
            }
            tickerNumber++;
        }
        if (tickerNumber == 0) {
            throw new IllegalArgumentException("No ticker number found");
        }
        List<IexQuote> quotes = new ArrayList<>();
        String tickerListString = String.join(",", tickerList);
        String url = String.format(IEX_BATCH_URL, tickerListString);
        Optional<String> quoteString = null;
        try {
            quoteString = executeHttpGet(url);
        } catch (Exception e) {
            throw new DataRetrievalFailureException("Invalid URL" + e.getMessage());
        }
        JSONObject jsonObject = new JSONObject(quoteString.get());
        ObjectMapper mapper = new ObjectMapper();
        IexQuote quote;
        for (String ticker : tickerList) {
            String stringQuote = jsonObject.getJSONObject(ticker).getJSONObject("quote").toString();
            try {
                quote = mapper.readValue(stringQuote, IexQuote.class);
            } catch (IOException ex) {
                throw new JSONException("Failed to convert JSON to quote object" + ex.getMessage());
            }
            quotes.add(quote);
        }
        return quotes;
    }


    /**
     * Execute a get and return http entity/body as a string
     * <p>
     * Use EntityUtils.toString to process HTTP entity
     *
     * @param url resource URL
     * @return HTTP response body or Optional.empty for 404 response
     * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
     */
    private Optional<String> executeHttpGet(String url) throws DataRetrievalFailureException, MalformedURLException {
        HttpClient httpClient = getHttpClient();
        HttpGet httpRequest = new HttpGet(url);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpRequest);
        } catch (IOException e) {
            throw new MalformedURLException("Invalid URL" + e.getMessage());
        }
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status != HTTP_OK) {
            throw new DataRetrievalFailureException("Unexpected http status" + status);
        }
        if (httpResponse.getEntity() == null) {
            throw new RuntimeException("Empty response body");
        }
        String jsonString;
        try {
            HttpEntity httpEntity = httpResponse.getEntity();
            jsonString = EntityUtils.toString(httpEntity);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Conversion from entity to string failed" + ex.getMessage());
        }
        Optional<String> response = Optional.of(jsonString);
        return response;
    }

    /**
     * Borrow a HTTP client from HttpClientConnectionManager
     *
     * @return httpClient
     */

    private CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionManagerShared(true)
                .build();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(IexQuote iexQuote) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends IexQuote> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
