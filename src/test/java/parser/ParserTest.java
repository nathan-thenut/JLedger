package jledger.parser;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import jledger.model.Ledger;
import jledger.model.Account;

public class ParserTest {
   
    @Test
    public void testReadingFile() {
        Parser parser = new Parser();
        Ledger test = parser.parseFile("test.journal");

        Map<Account.AccountType, Account> accounts = test.getAccounts();
        //System.out.println("Accounts: " + accounts);

        assertTrue(accounts.get(Account.AccountType.Expenses).findAccountByName("Recreation") != null,
                "Recreation account should be found");
    }

}
