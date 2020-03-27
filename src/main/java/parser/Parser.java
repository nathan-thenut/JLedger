package jledger.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

        List<Account> accounts = parseAccounts(lines);

        return ledger;
    }


    private List<Account> parseAccounts(List<String> lines) {
        List<Account> result = new ArrayList<>();
       
        List<String> matches = Keyword.ACCOUNT.findMatches(lines);

        for (String match : matches) {
            
        }

        return result;
    }
}
