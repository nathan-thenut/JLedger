import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction {
    
    private Date date;
    private List<Posting> additions;
    private List<Posting> removals;


    public Transaction(Date transactionDate) {
        this.date = transactionDate;
        this.additions = new ArrayList<>();
        this.removals = new ArrayList<>();
    } 
    
    public Transaction(Date date, List<Posting> additions, List<Posting> removals) {
        this.date = date;
        this.additions = additions;
        this.removals = removals;
    }

    
    private boolean checkTransactionBalance() {
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

    public List<Posting> getRemovals() {
        return removals;
    }

    public void setRemovals(List<Posting> removals) {
        this.removals = removals;
    }
}
