package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {

    private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);

    private static final String TABLE_NAME = "security_order";
    private static final String ID_COLUMN = "id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public SecurityOrderDao(DataSource dataSource) {
        setJdbcTemplate(new JdbcTemplate(dataSource));
        setSimpleJdbcInsert(new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME).usingGeneratedKeyColumns(ID_COLUMN));
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return simpleJdbcInsert;
    }

    public void setSimpleJdbcInsert(SimpleJdbcInsert simpleJdbcInsert) {
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnNames() {
        return ID_COLUMN;
    }

    @Override
    Class<SecurityOrder> getEntityCLass() {
        return SecurityOrder.class;
    }

    @Override
    public int updateOne(SecurityOrder securityOrder) {
        String updateSql = "UPDATE " + getTableName() + "SET accountId=?, status=?, ticker=?, size=?, price=?, " +
                " notes=?" + "WHERE id=? ";
        return getJdbcTemplate().update(updateSql, makeUpdateValues(securityOrder));
    }

    private Object[] makeUpdateValues(SecurityOrder securityOrder) {
        List<Object> values = new ArrayList<>();
        values.add(securityOrder.getAccountId());
        values.add(securityOrder.getSize());
        values.add(securityOrder.getPrice());
        values.add(securityOrder.getStatus());
        values.add(securityOrder.getTicker());
        values.add(securityOrder.getNotes());
        values.add(securityOrder.getId());
        return values.toArray();
    }

    @Override
    public <S extends SecurityOrder> Iterable<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    public static Logger getLogger() {
        return logger;
    }
}
