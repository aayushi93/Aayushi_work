package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {
    private static final String TABLE_NAME = "quote";
    private static final String ID_COLUMN_NAME = "ticker";

    private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public QuoteDao(DataSource dataSource) {
        jdbcTemplate = (new JdbcTemplate(dataSource));
        simpleJdbcInsert = (new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME));
    }

    /**
     * @param quote
     * @return quote
     * @throws DataAccessException for unexpected SQL result or SQL execution failure
     */
    @Override
    public Quote save(Quote quote) {
        if (existsById(quote.getTicker())) {
            int updatedRow = updateOne(quote);
            if (updatedRow != 1) {
                throw new DataRetrievalFailureException("Unable to update quote");
            }
        } else {
            addOne(quote);
        }
        return quote;
    }

    /**
     * Helper method that saves one quote
     *
     * @param quote
     */
    private void addOne(Quote quote) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
        int row = simpleJdbcInsert.execute(parameterSource);
        if (row != 1) {
            throw new IncorrectResultSizeDataAccessException("Failed to insert", 1, row);
        }
    }

    private int updateOne(Quote quote) {
        String updateSql = "UPDATE quote SET last_price=?, bid_price=?, " +
                "bid_size=?, ask_price=?, ask_price=? WHERE ticker=?";
        return jdbcTemplate.update(updateSql, makeUpDateValues(quote));
    }

    private Object[] makeUpDateValues(Quote quote) {
        List<Object> values = new ArrayList<>();
        values.add(quote.getLastPrice());
        values.add(quote.getBidPrice());
        values.add(quote.getBidSize());
        values.add(quote.getAskSize());
        values.add(quote.getAskPrice());
        values.add(quote.getTicker());
        return values.toArray();
    }

    public <S extends Quote> List<S> saveAll(Iterable<S> quotes) {
        List<S> resultQuote = new ArrayList<>();
        for (Quote quote : quotes) {
            resultQuote.add((S) save(quote));
        }
        return resultQuote;
    }


    @Override
    public Optional<Quote> findById(String ticker) {
        Quote quote = null;
        String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + " =?";
        try {
            quote = jdbcTemplate.queryForObject(selectSql, BeanPropertyRowMapper.newInstance(Quote.class), ticker);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("Can't find ticker marker: " + ticker, e);
        }
        return Optional.of(quote);
    }

    @Override
    public boolean existsById(String ticker) {
        String selectSql = "SELECT COUNT (*) FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + " =?";
        boolean result = jdbcTemplate.queryForObject(selectSql, Integer.class, ticker) > 0;
        return result;
    }

    @Override
    public List<Quote> findAll() {
        String selectSql = "SELECT * FROM " + TABLE_NAME;
        return jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(Quote.class));
    }


    @Override
    public long count() {
        String selectSql = "SELECT COUNT (*) FROM " + TABLE_NAME;
        return jdbcTemplate.queryForObject(selectSql, Long.class);
    }


    @Override
    public void deleteById(String ticker) {
        String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + " =? ";
        jdbcTemplate.update(deleteSql, ticker);
    }


    @Override
    public void deleteAll() {
        String deleteSql = "DELETE FROM " + TABLE_NAME;
        jdbcTemplate.update(deleteSql);
    }


    @Override
    public Iterable<Quote> findAllById(Iterable<String> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Quote quote) {
        throw new UnsupportedOperationException("Not implemented");

    }

    @Override
    public void deleteAll(Iterable<? extends Quote> iterable) {
        throw new UnsupportedOperationException("Not implemented");

    }


}