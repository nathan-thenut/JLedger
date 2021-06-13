package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Transaction {
    
    private final Logger LOG = LoggerFactory.getLogger(Transaction.class);

    private LocalDate date;
    private String description;
    private List<Posting> additions;
    private List<Posting> removals;


    public Transaction(LocalDate transactionDate, String description) {
        this.date = transactionDate;
        this.description = description;
        this.additions = new ArrayList<>();
        this.removals = new ArrayList<>();
    } 
    
    
    public boolean checkTransactionBalance() {
        
        List<String> currenciesAdditions = this.additions.stream()
            .map(p -> p.getCurrency())
            .distinct()
            .collect(Collectors.toList());

        List<String> currenciesRemovals = this.removals.stream()
            .map(p -> p.getCurrency())
            .distinct()
            .collect(Collectors.toList());

        if (!new HashSet<>(currenciesAdditions).equals(new HashSet<>(currenciesRemovals))) {
            LOG.error("{} {} - Number of currencies between additions and removals does not match.", this.date, this.description);
            return false;
        }


        for (String currency : currenciesAdditions) {

            BigDecimal amountAdditions = this.additions.stream()
                .filter(p -> p.getCurrency().equals(currency))
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal amountRemovals = this.removals.stream()
                .filter(p -> p.getCurrency().equals(currency))
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            if (amountAdditions.add(amountRemovals).compareTo(new BigDecimal("0")) != 0) {
                LOG.error("{} {} - Additions and removals do not match.", this.date, this.description);
                return false;
            }
        }
        

        return true;
    }
    
    
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public List<Posting> getAdditions() {
        return additions;
    }

    public void setAdditions(List<Posting> additions) {
        this.additions = additions;
    }

    public void addAdditions(List<Posting> newAdditions) {
        this.additions.addAll(newAdditions);
    }

    public List<Posting> getRemovals() {
        return removals;
    }

    public void setRemovals(List<Posting> removals) {
        this.removals = removals;
    }

    public void addRemovals(List<Posting> newRemovals) {
        this.removals.addAll(newRemovals);
    }

    public List<Posting> getAllPostings() {
        return Stream.concat(this.additions.stream(), this.removals.stream())
            .collect(Collectors.toList());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
