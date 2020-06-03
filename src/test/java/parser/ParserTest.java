package parser;

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

        Map<Account.AccountType, Account> accounts = test.getAccounts();

        assertTrue(accounts.get(Account.AccountType.Expenses).findAccountByName("Recreation") != null,
                "Recreation account should be found");

        assertTrue(accounts.get(Account.AccountType.Expenses).findAccountByName("Phone/Internet") != null,
                "Phone/Internet account should be found");
        
        assertTrue(accounts.get(Account.AccountType.Expenses).findAccountByName("Wu Shu I") != null,
                "Wu Shu I account should be found");

    }

}
