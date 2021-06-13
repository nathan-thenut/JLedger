package model;

import java.util.HashMap;
import java.util.Map;


/*
 * This class represents a ledger file.
 */
public class Ledger {
    
    private String name;

    private Map<Account.AccountType, Account> accountMap;

    public Ledger(String name) {
        this.name = name;
        this.accountMap = new HashMap<>();

        this.accountMap.put(Account.AccountType.Expenses, new Account("Expenses", Account.AccountType.Expenses));
        this.accountMap.put(Account.AccountType.Income, new Account("Revenue", Account.AccountType.Income));
        this.accountMap.put(Account.AccountType.Liabilities, new Account("Liabilities", Account.AccountType.Liabilities));
        this.accountMap.put(Account.AccountType.Equity, new Account("Equity", Account.AccountType.Equity));
        this.accountMap.put(Account.AccountType.Assets, new Account("Assets", Account.AccountType.Assets));
        this.accountMap.put(Account.AccountType.Adjustment, new Account("Adjustment", Account.AccountType.Adjustment));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateAccounts(Map<Account.AccountType, Account> updatedAccounts) {
        for (Account.AccountType type : updatedAccounts.keySet()) {
            this.accountMap.put(type, updatedAccounts.get(type));
        }
    }


    public Account findOrAddAccount(String fullAccountString) {
        String[] accounts = fullAccountString.split(":");
        Account.AccountType type = Account.AccountType.valueOfString(accounts[0]);

        Account root = this.accountMap.get(type);

        if (root == null) {
            root = new Account(accounts[0], type);
            this.accountMap.put(type, root);
        }

        Account current = null;
        for (int i = 1; i < accounts.length; i++) {
            current = root.findAccountByName(accounts[i]);
            if (current == null) {
                current = new Account(accounts[i], type);
                if (i == 1) {
                    root.addChild(current);
                } else {
                    Account parent = root.findAccountByName(accounts[i - 1]);
                    if (parent != null) {
                        parent.addChild(current);
                    }
                }
            }
        }
        return current;
    }

    public Map<Account.AccountType, Account> getAccounts() {
        return this.accountMap;
    }
}
