package jledger.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LedgerTest {
    
    @Test 
    public void testOpeningTransaction() {
        Account liabilities = new Account("Liabilities", Account.AccountType.LIABILITIES);
        Account equity = new Account("Equity", Account.AccountType.EQUITY);
        Account assets = new Account("Assets", Account.AccountType.ASSETS);

        Transaction opening = new Transaction(new Date(), "Opening transaction");
        List<Posting> additions = new ArrayList<>(); 
        List<Posting> removals = new ArrayList<>(); 
        
        additions.add(new Posting(assets, new BigDecimal("150.0"), "EUR", opening));
        removals.add(new Posting(equity, new BigDecimal("-150.0"), "EUR", opening));

        additions.add(new Posting(assets, new BigDecimal("150.0"), "EUR", opening));
        removals.add(new Posting(equity, new BigDecimal("-150.0"), "EUR", opening));

        additions.add(new Posting(equity, new BigDecimal("150.0"), "EUR", opening));
        removals.add(new Posting(liabilities, new BigDecimal("-150.0"), "EUR", opening));
        
        opening.addAdditions(additions);
        opening.addRemovals(removals);

        assertTrue(
            opening.checkTransactionBalance(),
            "Transaction Balance should be Zero, so this should be true"
        ); 
        
        assets.addTransaction(opening);
        equity.addTransaction(opening);
        liabilities.addTransaction(opening);
         
        assertEquals(
            new BigDecimal("300.0"),
            assets.getAmount("EUR"),
            "Assets should have a Transaction Amount of 300" 
        );
        
    }

    @Test
    public void testAccountSearch() {
        Account expenses = new Account("Expenses", Account.AccountType.EXPENSES);
        Account utilities = new Account("Utilities", Account.AccountType.EXPENSES);
        Account rent = new Account("Rent", Account.AccountType.EXPENSES);
        Account entertainment = new Account("Entertainment", Account.AccountType.EXPENSES);
        Account music = new Account("Music", Account.AccountType.EXPENSES);
        Account recreation = new Account("Recreation", Account.AccountType.EXPENSES);
        Account travel = new Account("travel", Account.AccountType.EXPENSES);

        utilities.addChild(rent);

        entertainment.addChildren(new ArrayList<>() {{
            add(music);
            add(recreation);
            add(travel);
        }});

        expenses.addChildren(new ArrayList<>() {{
            add(utilities);
            add(entertainment);
        }});

        assertEquals(
            expenses.findAccountByName("Rent"),
            rent,
            "The rent account should be found."
        );

        assertEquals(
            expenses.findAccountByName("rent"),
            rent,
            "Account Rent should be found with lowercase too."
        );

        assertEquals(
            expenses.findAccountByName("Entertainment"),
            entertainment,
            "The entertainment account should be found."
        );

        assertEquals(
            expenses.findAccountByName("entertainment"),
            entertainment,
            "Account Entertainment should be found with lowercase too."
        );


    }
}
