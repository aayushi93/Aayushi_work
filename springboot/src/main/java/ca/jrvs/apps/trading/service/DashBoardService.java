package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.PortfolioView;
import ca.jrvs.apps.trading.model.view.SecurityRow;
import ca.jrvs.apps.trading.model.view.TraderAccountViewDashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DashBoardService {
    private TraderDao traderDao;
    private PositionDao positionDao;
    private AccountDao accountDao;
    private QuoteDao quoteDao;

    @Autowired
    public DashBoardService(TraderDao traderDao, PositionDao positionDao, AccountDao accountDao, QuoteDao quoteDao) {
        this.accountDao = accountDao;
        this.traderDao = traderDao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    /**
     * @param traderId
     * @throws IllegalArgumentException if trader Id not found.
     */
    private Account findAccountByTraderId(Integer traderId) {
        List<Account> accounts = accountDao.findRowByColumnId(traderId, "traderId");
        if (accounts.size() == 0) {
            throw new IllegalArgumentException("Trader not found.");
        }
        return accounts.get(0);
    }

    /**
     * Create and return a traderAccountView by traderId
     * - get trader account by id
     * - get trader info by id
     * - create and return a traderAccountView
     *
     * @param traderId must not be null
     * @return traderAccountView
     * @throws IllegalArgumentException if traderId is null or not found
     */


    public TraderAccountViewDashBoard getTraderAccount(Integer traderId) {
        Trader trader = traderDao.findById(traderId).get();
        Account account = findAccountByTraderId(traderId);
        TraderAccountViewDashBoard traderAccountViewDash = new TraderAccountViewDashBoard();
        traderAccountViewDash.setAccount(account);
        traderAccountViewDash.setTrader(trader);
        return traderAccountViewDash;
    }

    /**
     * Create and return portfolioView by traderId
     * - get account by trader id
     * - get positions by account id
     * - create and return a portfolioView
     *
     * @param traderId must not be null
     * @return PortfolioView()
     * @throws IllegalArgumentException if traderId is null or not found.
     */

    public PortfolioView getProfileViewByTraderId(Integer traderId) {
        Account account = findAccountByTraderId(traderId);
        Trader trader = traderDao.findById(traderId).get();
        List<Position> positions = positionDao.findRowByColumnId(account.getId(), "accountId");
        if (positions.size() == 0) {
            return new PortfolioView();
        }
        List<SecurityRow> securityRows = new ArrayList<>();
        for (Position position : positions) {
            SecurityRow securityRow = new SecurityRow();
            securityRow.setTicker(position.getTicker());
            securityRow.setPosition(position);
            securityRow.setQuote(quoteDao.findById(position.getTicker()).get());
            securityRows.add(securityRow);
        }
        PortfolioView portfolioView = new PortfolioView();
        portfolioView.setSecurityRowList(securityRows);
        return portfolioView;
    }

}
