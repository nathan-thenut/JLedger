import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {
    
    private Account parent;
    private List<Account> children;
    private String name;
    private BigDecimal amount;
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String accountName) {
        this.name = accountName;
        this.children = new ArrayList<>();
        this.amount = BigDecimal.ZERO;
    }

    public Account(String accountName, Account accountParent) {
        this.name = accountName;
        this.parent = accountParent;
        this.amount = BigDecimal.ZERO;
    }

    private void calculateOverAllTransactionAmount() {
        BigDecimal additions = this.transactions.stream()
            .map(t -> t.getAdditions())
            .flatMap(List::stream)
            .filter(p -> p.getAccount().equals(this))
            .map(p -> p.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal removals = this.transactions.stream()
            .map(t -> t.getRemovals())
            .flatMap(List::stream)
            .filter(p -> p.getAccount().equals(this))
            .map(p -> p.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (Account child : this.children) {
            this.amount.add(child.getAmount());
        }

        this.amount.add(additions);
        this.amount.add(removals);

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

    public BigDecimal getAmount() {
        return amount;
    }
 
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        calculateOverAllTransactionAmount();
    }
}
