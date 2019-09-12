package jledger.model;

import java.math.BigDecimal;

public class Posting {
    
    private Account account;
    private BigDecimal amount;
    private String currency;
    private PostingType type;
    private Transaction transaction;
    
    public enum PostingType {
        ADDITION,
        REMOVAL
    }

    public Posting(Account account, BigDecimal amount, String currency, Transaction transaction) {
        this.account = account;
        this.amount = amount;
        this.currency = currency;
        this.transaction = transaction;
        updateType();
    }

    private void updateType() {
        
        if (this.amount.signum() == -1) {
            this.type = PostingType.REMOVAL;
        } else {
            this.type = PostingType.ADDITION;
        }
 
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        updateType();
    }

    public PostingType getType() {
        return this.type;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

