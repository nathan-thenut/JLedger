import java.util.ArrayList;
import java.util.List;

/*
 * This class represents a ledger file.
 */
public class Ledger {
    
    private String name;
    private List<Account> accounts;    
    private List<Transaction> transactions;

    public Ledger(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public Ledger(String name, List<Account> accounts, List<Transaction> transactions) {
        this.name = name;
        this.accounts = accounts;
        this.transactions = transactions;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
