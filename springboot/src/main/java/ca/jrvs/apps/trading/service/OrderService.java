package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderService.class);

    private AccountDao accountDao;
    private SecurityOrderDao securityOrderDao;
    private PositionDao positionDao;
    private QuoteDao quoteDao;

    @Autowired
    public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao, PositionDao positionDao, QuoteDao quoteDao) {
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.positionDao = positionDao;
        this.quoteDao = quoteDao;
    }


    /**
     * Execute market order
     * - Validate size and ticker of order
     * - Create securityOrder
     * - Handle buy order - check account balance
     * - Handle sell order - check position for ticker
     * - Save and return securityOrder
     *
     * @param orderDTO market order
     * @return SecurityOrder from security_order table
     * @throws org.springframework.dao.DataAccessException if unable to get data from DAO
     * @throws IllegalArgumentException                    for invalid input
     */
    public SecurityOrder executeMarketOrder(MarketOrderDto orderDTO) {
        Integer securityOrderId;
        String ticker = orderDTO.getTicker().toUpperCase();
        Integer accoundId = orderDTO.getAccountId();

        Quote quote = quoteDao.findById(ticker).get();
        Account account = accountDao.findById(accoundId).get();

        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setSize(orderDTO.getSize());
        securityOrder.setStatus("Created");
        securityOrder.setTicker(orderDTO.getTicker());
        securityOrder.setAccountId(accoundId);

        if (orderDTO.getSize() > 0) {
            if (orderDTO.getSize() > quote.getAskSize()) {
                throw new IllegalArgumentException("Size of the order is exceeding");
            }
            securityOrder.setPrice(quote.getBidPrice());
            handleSellMarketOrder(orderDTO, securityOrder, account);
            quote.setBidSize(quote.getBidSize() - orderDTO.getSize());
        } else {
            throw new IllegalArgumentException("Size of the order must be more than 0.");
        }
        securityOrder.setStatus("Completed");
        return securityOrder;
    }

    /**
     * Execute a buy order
     *
     * @param marketOrderDto user order
     * @param securityOrder  to be saved in database
     * @param account        account
     * @return securityOrder id is generated
     */
    protected void handleBuyMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder, Account account) {
        Double cost = securityOrder.getPrice() * securityOrder.getSize();
        if (cost > account.getAmount()) {
            throw new IllegalArgumentException("Insufficient fund in the target account");
        }
        account.setAmount(account.getAmount() - cost);
        securityOrder.setStatus("Completed");
        accountDao.save(account);
        securityOrder.setId(securityOrderDao.save(securityOrder).getId());
    }

    /**
     * Execute a sell order
     *
     * @param marketOrderDto user order
     * @param securityOrder  to be saved in a database
     * @param account        account
     */
    protected void handleSellMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder, Account account) {
        Position position = null;
        List<Position> positions = positionDao.findRowByColumnId(account.getId(), "accountId");
        if (positions == null) {
            throw new IllegalArgumentException("The order must be smaller than security position");
        }
        boolean condition = false;
        for (Position pos : positions) {
            if (pos.getTicker() == marketOrderDto.getTicker().toUpperCase()) {
                condition = true;
                if (pos.getPosition() < marketOrderDto.getSize()) {
                    throw new IllegalArgumentException("The order must be smaller than security position");
                }
            }
        }
        if (!condition) {
            throw new IllegalArgumentException("No position owned by the user for " +
                    "security with ticker: " + marketOrderDto.getTicker());
        }
        Double sale = securityOrder.getPrice() * securityOrder.getSize() * -1;
        account.setAmount(account.getAmount() + sale);
        securityOrder.setStatus("Completed");
        accountDao.save(account);
        securityOrder.setId(securityOrderDao.save(securityOrder).getId());
    }
}
