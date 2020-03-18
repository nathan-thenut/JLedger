package jledger.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LedgerTest {
    
    @Test 
    public void testOpeningTransaction() {
        Ledger testLedger = new Ledger("test");
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
}
