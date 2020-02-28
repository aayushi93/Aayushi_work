package ca.jrvs.apps.trading.model.domain;

public class TraderAccountView implements Entity<Integer> {

    private Integer traderId;
    private Integer accountId;
    private Double balance;
    private Integer position;

    public Integer getTraderId() {
        return traderId;
    }

    public void setTraderId(Integer traderId) {
        this.traderId = traderId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public Integer getId() {
        return traderId;
    }

    @Override
    public void setId(Integer traderId) {
        this.traderId = traderId;

    }
}
