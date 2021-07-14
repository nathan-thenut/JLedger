package parser;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.Ledger;
import model.Account;

public class ParserTest {
   
    @Test
    public void testReadingFile() {
        Parser parser = new Parser();
        Ledger test = parser.parseFile("test/accounts.journal");
        test.updateBalances();

        Map<Account.AccountType, Account> accounts = test.getAccounts();

        assertTrue(accounts.size() == 6, "There should be 6 main accounts");

        assertTrue(accounts.get(Account.AccountType.Expenses).findAccountByName("Recreation") != null,
                "Recreation account should be found");

        assertTrue(accounts.get(Account.AccountType.Expenses).findAccountByName("Phone/Internet") != null,
                "Phone/Internet account should be found");
        
        assertTrue(accounts.get(Account.AccountType.Expenses).findAccountByName("Wu Shu I") != null,
                "Wu Shu I account should be found");

        assertTrue(accounts.get(Account.AccountType.Assets).getAmount("EUR").equals(new BigDecimal("736.64")));

        assertTrue(accounts.get(Account.AccountType.Liabilities).getAmount("EUR").equals(new BigDecimal("-161.16")));
        assertTrue(accounts.get(Account.AccountType.Expenses).getAmount("EUR").equals(new BigDecimal("7.40")));

    }

}
