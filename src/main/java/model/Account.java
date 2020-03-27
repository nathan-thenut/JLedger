package jledger.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Account {
   
    private final Logger LOG = LoggerFactory.getLogger(Account.class);

    private Account parent;
    private Map<String, Account> children;
    private String name;
    private AccountType type;
    private Map<String, BigDecimal> currencyAmountMap;
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
        this.children = new HashMap<>();
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

            for (Account child : this.children.values()) {
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

    public Account findAccountByName(String name) {
        LOG.debug("Account {}: Trying to find account {}", this.name, name);
        if (this.children.get(name) != null) {
            LOG.debug("Account {}: Found account {}", this.name, name);
            return this.children.get(name);
        } else if (!this.children.isEmpty()) {
            for (String k : this.children.keySet()) {
                if (k.equalsIgnoreCase(name)) {
                    LOG.debug("Account {}: Found account {}", this.name, k);
                    return this.children.get(k);
                } else {
                    Account current = this.children.get(k).findAccountByName(name);
                    if (current != null) {
                        LOG.debug("Account {}: Found account {}", this.name, current.getName());
                        return current;
                    }
                }
            }
        }

        LOG.debug("Account {}: Account not found: {}", this.name, name);
        return null; 
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

    public Map<String, Account> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Account> children) {
        this.children = children;
    }

    public void addChild(Account child) {
        this.children.put(child.getName(), child);
    }

    public void addChildren(List<Account> children) {
        children.forEach(c ->{
            this.children.put(c.getName(), c);
        });
    }

    public void removeChild(Account child) {
        this.children.remove(child.getName(), child);
    }

    public void removeChildren(List<Account> children) {
        children.forEach(c -> {
            this.children.remove(c.getName(), c);
        });
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Account other = (Account) obj;

        return (other.getName().equals(this.name)
                && other.getChildren().equals(this.children)
                && other.getParent().equals(this.parent)
                && other.getType() == this.type
                && other.getTransactions().equals(this.transactions));
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.name.hashCode();
        hash = 23 * hash + this.children.hashCode();
        hash = 23 * hash + this.parent.hashCode();
        hash = 23 * hash + this.type.hashCode();
        hash = 23 * hash + this.transactions.hashCode();
        return hash;
    }
}
