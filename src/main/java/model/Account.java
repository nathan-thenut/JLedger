package jledger.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Account {
   
    private final Logger LOG = LoggerFactory.getLogger(Account.class);

    private Account parent;
    private List<Account> children;
    private String name;
    private AccountType type;
    private HashMap<String, BigDecimal> currencyAmountMap;
    private List<Transaction> transactions = new ArrayList<>();
    
    public enum AccountType {
        INCOME,
        EXPENSES,
        LIABILITIES,
        EQUITY,
        ASSETS
    }


    public Account(String accountName, AccountType accountType) {
        this.name = accountName;
        this.children = new ArrayList<>();
        this.currencyAmountMap = new HashMap<>();
        this.type = accountType;
    }
 

    private void calculateOverAllTransactionAmount() {

        List<String> currencies = this.transactions.stream()
            .map(t -> t.getAllPostings())
            .flatMap(List::stream)
            .map(p -> p.getCurrency())
            .distinct()
            .collect(Collectors.toList());
        
        for (String currency : currencies) {

            BigDecimal additions = this.transactions.stream()
                .map(t -> t.getAdditions())
                .flatMap(List::stream)
                .filter(p -> p.getAccount().equals(this) && p.getCurrency().equals(currency))
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            LOG.debug("{} {} Additions: {}", this.name, currency, additions);

            BigDecimal removals = this.transactions.stream()
                .map(t -> t.getRemovals())
                .flatMap(List::stream)
                .filter(p -> p.getAccount().equals(this))
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            LOG.debug("{} {} Removals: {}", this.name, currency, removals);
            
            BigDecimal amount = BigDecimal.ZERO;

            for (Account child : this.children) {
                amount = amount.add(child.getAmount(currency));
            }

            LOG.debug("{} {} Amount after adding Children's amount: {}", this.name, currency, amount);
            amount = amount.add(additions);
            LOG.debug("{} {} Amount after adding additions: {}", this.name, currency, amount);
            amount = amount.add(removals);
            LOG.debug("{} {} Amount after adding Children's removals: {}", this.name, currency, amount);

            currencyAmountMap.put(currency, amount);

        }

    }

    
    public void addTransaction(Transaction action) {
        this.transactions.add(action);
        calculateOverAllTransactionAmount();
    }

    public void removeTransaction(Transaction action) {
        this.transactions.remove(action);
        calculateOverAllTransactionAmount();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount(String currency) {
        return ((currencyAmountMap.get(currency) == null) ? BigDecimal.ZERO : currencyAmountMap.get(currency));
    }
 
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        calculateOverAllTransactionAmount();
    }

    public Account getParent() {
        return parent;
    }

    public void setParent(Account parent) {
        this.parent = parent;
    }

    public List<Account> getChildren() {
        return children;
    }

    public void setChildren(List<Account> children) {
        this.children = children;
    }

    public void addChild(Account child) {
        this.children.add(child);
    }

    public void addChildren(List<Account> children) {
        this.children.addAll(children);
    }

    public void removeChild(Account child) {
        this.children.remove(child);
    }

    public void removeChildren(List<Account> children) {
        this.children.removeAll(children);
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
}
