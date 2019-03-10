import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction {
    
    private Date date;
    private String description;
    private List<Posting> additions;
    private List<Posting> removals;


    public Transaction(Date transactionDate, String description) {
        this.date = transactionDate;
        this.description = description;
        this.additions = new ArrayList<>();
        this.removals = new ArrayList<>();
    } 
    
    
    public boolean checkTransactionBalance() {
        boolean balanceIsZero = false;

        BigDecimal amountAdditions = this.additions.stream()
            .map(p -> p.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal amountRemovals = this.removals.stream()
            .map(p -> p.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (amountAdditions.add(amountRemovals).compareTo(new BigDecimal("0")) == 0) {
            balanceIsZero = true;
        }

        return balanceIsZero;
    }
    
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
