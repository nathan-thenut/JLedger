package jledger.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jledger.model.Ledger;
import jledger.model.Account;

/*
 * This class represents a parser for ledger files
 */
public class Parser {
    
    private final Logger LOG = LoggerFactory.getLogger(Parser.class);
    
    private Charset charset;

    public Parser() {
        this.charset = StandardCharsets.UTF_8;
    }

    public Parser(Charset charset) {
        this.charset = charset;
    }

    public List<String> readFile(String filename) {
        List<String> result = new ArrayList<>();
        try {
            result = Files.readAllLines(Paths.get(filename), this.charset);
        } catch (IOException e) {
            LOG.error("An error occured while trying to parse " + filename, e);
        }
        return result;
    }


    public Ledger parseFile(String filename) {
        
        Ledger ledger = new Ledger("test");
        List<String> lines = readFile(filename);

        Map<Account.AccountType, Account> accounts = parseAccounts(lines);
       
        System.out.println("Accounts: " + accounts);

        ledger.updateAccounts(accounts);

        return ledger;
    }

    private Map<Account.AccountType, Account> parseAccounts(List<String> lines) {
        Map<Account.AccountType, Account> result = new HashMap<>();
       
        List<String> matches = Keyword.ACCOUNT.findMatches(lines);
        
        System.out.println("Lines: " + lines);
        System.out.println("Matches: " + matches);


        for (String match : matches) {
            String[] accounts = match.substring(8).split(":");
            Account.AccountType type = Account.AccountType.valueOfString(accounts[0]);

            
            Account root = result.get(type);

            if (root == null) {
                root = new Account(accounts[0], type);
                result.put(type, root);
            }

            for (int i = 1; i < accounts.length; i++) {
                Account current = root.findAccountByName(accounts[i]);
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
        }
        
        System.out.println("Result: " + result);
        return result;
    }
}
