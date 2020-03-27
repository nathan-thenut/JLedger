package jledger.model;

import java.util.ArrayList;
import java.util.List;

/*
 * This class represents a ledger file.
 */
public class Ledger {
    
    private String name;

    private Account expensesRoot;
    private Account incomeRoot;
    private Account liabilitiesRoot;
    private Account equityRoot;
    private Account assetsRoot;


    public Ledger(String name) {
        this.name = name;

        this.expensesRoot = new Account("Expenses", Account.AccountType.EXPENSES);
        this.incomeRoot = new Account("Revenue", Account.AccountType.INCOME);
        this.liabilitiesRoot = new Account("Liabilities", Account.AccountType.LIABILITIES);
        this.equityRoot = new Account("Equity", Account.AccountType.EQUITY);
        this.assetsRoot = new Account("Assets", Account.AccountType.ASSETS);
    }

    public Ledger(String name, List<Account> accounts) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
