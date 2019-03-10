import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {
    
    private Account parent;
    private List<Account> children;
    private String name;
    private AccountType type;
    private BigDecimal amount;
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
        this.amount = BigDecimal.ZERO;
        this.type = accountType;
    }
 

    private void calculateOverAllTransactionAmount() {
        BigDecimal additions = this.transactions.stream()
            .map(t -> t.getAdditions())
            .flatMap(List::stream)
            .filter(p -> p.getAccount().equals(this))
            .map(p -> p.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
       
        System.out.println(this.name + " Additions: " + additions);

        BigDecimal removals = this.transactions.stream()
            .map(t -> t.getRemovals())
            .flatMap(List::stream)
            .filter(p -> p.getAccount().equals(this))
            .map(p -> p.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println(this.name + " Removals: " + removals);
        
        this.amount = BigDecimal.ZERO;

        for (Account child : this.children) {
            this.amount = this.amount.add(child.getAmount());
        }

        System.out.println(this.name + " Amount after adding Children's amount: " + this.amount);
        this.amount = this.amount.add(additions);
        System.out.println(this.name + " Amount after adding additions: " + this.amount);
        this.amount = this.amount.add(removals);
        System.out.println(this.name + " Amount after adding removals: " + this.amount);

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

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
}
