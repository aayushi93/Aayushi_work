package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.service.QuoteService;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {
    private Logger logger = LoggerFactory.getLogger(AppConfig.class);


    @Bean
    public QuoteService serviceQuote(MarketDataDao marketDataDao) {
        return new QuoteService(marketDataDao);
    }

    @Bean
    public MarketDataDao marketData(HttpClientConnectionManager httpClientConnectionManager,
                                    MarketDataConfig marketDataConfig) {
        return new MarketDataDao(httpClientConnectionManager, marketDataConfig);

    }

    @Bean
    public MarketDataConfig marketDataConfig() {
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/v1/");
        marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));
        return marketDataConfig;
    }

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager clientManager = new PoolingHttpClientConnectionManager();
        clientManager.setMaxTotal(50);
        clientManager.setDefaultMaxPerRoute(50);
        return clientManager;
    }
}