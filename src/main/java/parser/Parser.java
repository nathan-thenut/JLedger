package parser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Ledger;
import model.Account;
import model.Posting;
import model.Transaction;

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

    public List<String> readFileIntoList(String filename) {
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
        List<String> lines = readFileIntoList(filename);

        parseAccounts(ledger, lines);
       
        LOG.debug("Accounts: " + ledger.getAccounts());

        parseTransactions(ledger, lines);

        return ledger;
    }

    private void parseAccounts(Ledger ledger, List<String> lines) {
       
        List<String> matches = Keyword.ACCOUNT.findMatches(lines);
        
        LOG.debug("Lines: " + lines);
        LOG.debug("Matches: " + matches);


        for (String match : matches) {
            ledger.findOrAddAccount(match.substring(8));
        }

    }

    private void parseTransactions(Ledger ledger, List<String> lines) {
        List<String> matches = Keyword.TRANSACTION_BEGINNING.findMatches(lines);

        for (String match : matches) {
            LOG.debug("Found Transaction: " + match);
            // convert date to iso-date
            LocalDate transactionDate = LocalDate.parse(match.substring(0,10).replace("/", "-"));
            String description = match.substring(match.indexOf(' '));
            LOG.debug("With date: " + transactionDate);
            LOG.debug("With description: " + description);
            Transaction currentTransaction = new Transaction(transactionDate, description);
            List<Posting> additions = new ArrayList<>();
            List<Posting> removals = new ArrayList<>();
            //set posting index to the index of the first posting
            int postingIndex = lines.indexOf(match) + 1;
            while (postingIndex < lines.size() && lines.get(postingIndex).contains(":")) {
                String[] accountAndAmount = lines.get(postingIndex).trim().split("\\s{2,}");
                //TODO: assure that there are only two elements in the array
                Account currentAccount = ledger.findOrAddAccount(accountAndAmount[0]);
                //TODO: order of amount and currency can also be reversed
                String[] amountAndCurrency = accountAndAmount[1].split("\\s");
                if (accountAndAmount[1].contains("=")) {
                    amountAndCurrency = accountAndAmount[1].split("=")[1].trim().split("\\s");
                }
                BigDecimal amount = new BigDecimal(amountAndCurrency[0]);
                String currency = amountAndCurrency[1];
                Posting currentPosting = new Posting(currentAccount, amount, currency, currentTransaction);
                if (currentPosting.getType().equals(Posting.PostingType.ADDITION)) {
                    additions.add(currentPosting);
                } else {
                    removals.add(currentPosting);
                }
                currentAccount.addTransaction(currentTransaction);
                postingIndex++;
            }
            currentTransaction.addAdditions(additions);
            currentTransaction.addRemovals(removals);
        }
    }
}
