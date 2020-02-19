package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

public class AccountDao extends JdbcCrudDao<Account> {
    private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);

    private final String TABLE_NAME = "account";
    private final String ID_COLUMN = "id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    
    @Autowired
    public AccountDao(DataSource dataSource) {
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

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnNames() {
        return ID_COLUMN;
    }

    @Override
    Class<Account> getEntityCLass() {
        return Account.class;
    }


    /** Update amount in the account
     * - negative amount indicates withdrawal
     * - positive amount indicates deposit
     * @param account 
     * @param amount
     * @return
     */
    public Account updateAccountAmount(Account account, Double amount) {
        Double bal = account.getAmount();
        Double newBal = bal + amount;
        if(newBal < 0 ) {
            throw new IllegalArgumentException("Insufficient Balance!!");
        }
        account.setAmount(newBal);
        return save(account);
    }

    @Override
    public int updateOne(Account entity) {
        String updateSql = "UPDATE" + getTableName() + " SET amount = ?";
        return getJdbcTemplate().update(updateSql, entity.getAmount());
    }

    public void setSimpleJdbcInsert(SimpleJdbcInsert simpleJdbcInsert) {
     this.simpleJdbcInsert = simpleJdbcInsert;   
    }

    @Override
    public <S extends Account> Iterable<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    public static Logger getLogger() {
        return logger;
    }
}
