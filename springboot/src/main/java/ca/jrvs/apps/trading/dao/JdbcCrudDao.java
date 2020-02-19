package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class JdbcCrudDao<T extends Entity<Integer>> implements CrudRepository<T, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

    abstract public JdbcTemplate getJdbcTemplate();

    abstract public SimpleJdbcInsert getSimpleJdbcInsert();

    abstract public String getTableName();

    abstract public String getIdColumnNames();

    abstract Class<T> getEntityCLass();

    /**
     * Save an entity and update auto-generated integer ID
     *
     * @param entity to be saved
     * @return save entity
     */
    @Override
    public <S extends T> S save(S entity) {
        if (existsById(entity.getId())) {
            if (updateOne(entity) != 1) {
                throw new DataRetrievalFailureException("Unable to update quote");
            }
        } else {
            addOne(entity);
        }
        return entity;
    }

    /**
     * Helper method to save one quote
     * @param entity
     */
    private <S extends T> void addOne(S entity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
        Number newId = getSimpleJdbcInsert().executeAndReturnKey(parameterSource);
        entity.setId(newId.intValue());
    }

    /**
     * Helper method to update one quote
     * @param entity to be updated
     */
    abstract public int updateOne(T entity);

    public Optional<T> findById(Integer id) {
        Optional<T> entity = Optional.empty();
        String selectSql = "SELECT * FROM " + getTableName() + "WHERE" + getIdColumnNames() + " =?";

        try {
            entity = Optional.ofNullable((T) getJdbcTemplate()
                    .queryForObject(selectSql, BeanPropertyRowMapper.newInstance(getEntityCLass()), id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            logger.debug("Can't find trader id: " + id, ex);
        }
        return entity;
    }

    @Override
    public boolean existsById(Integer id) {
        if (id == null) {
            return false;
        }
        long count;
        String countSql = "SELECT COUNT(*) FROM " + getTableName() + "WHERE " + getIdColumnNames() + " =?";
        try {
            count = getJdbcTemplate().queryForObject(countSql, Long.class, id);
        } catch (NullPointerException e) {
            count = 0;
        }
        return count != 0;
    }

    @Override
    public List<T> findAll() {
        String selectSql = "SELECT * FROM " + getTableName();
        return getJdbcTemplate()
                .query(selectSql, BeanPropertyRowMapper.newInstance(getEntityCLass()));
    }

    @Override
    public List<T> findAllById(Iterable<Integer> ids) {
        List<T> entities = new ArrayList<>();
        for (Integer id : ids) {
            entities.add(findById(id).get());
        }
        return entities;
    }

    public List<T> findRowByColumnId(Integer id, String column) {
        List<T> entityOut = new ArrayList<>();
        String selectSql = "SELECT * FROM " + getTableName() + "WHERE " + column + "=?";
        try {
            entityOut = getJdbcTemplate().query(selectSql, BeanPropertyRowMapper.newInstance(getEntityCLass()), id);
        } catch (IncorrectResultSizeDataAccessException ex) {
            logger.debug("Can't find column with id: " + id, ex);
        }
        return entityOut;
    }

    @Override
    public void delete(T entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteById(Integer id) {
        String deleteSql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnNames() + " =?";
        getJdbcTemplate().update(deleteSql, id);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public long count() {
        long count;
        String countSql = "SELECT COUNT(*) FROM " + getTableName();
        try {
            count = getJdbcTemplate().queryForObject(countSql, Long.class);
        } catch (NullPointerException e) {
            count = 0;
        }
        return count;
    }

    @Override
    public void deleteAll() {
        String deleteSql = "DELETE FROM " + getTableName();
        getJdbcTemplate().update(deleteSql);
    }
}
