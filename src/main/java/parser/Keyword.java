package parser;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public enum Keyword {
    
    ACCOUNT("account", "^account"),
    //TODO: implement default year and currency
    DEFAULT_YEAR("default year", "^Y[0-9]{4}"),
    DEFAULT_CURRENCY("default currency", ""),
    TRANSACTION_BEGINNING("transaction beginning", "^[0-9]{4}[/-][0-9]{2}[/-][0-9]{2}");

    private String name;
    private Pattern pattern;

    Keyword(String name, String pattern) {
        this.name = name;
        this.pattern = Pattern.compile(pattern);
    }

    
    public String getName() {
        return this.name;
    }

    public Pattern getPattern() {
        return this.pattern;
    }


    public List<String> findMatches(List<String> strings) {
            
        return strings.stream()
            .filter(this.pattern.asPredicate())
            .collect(Collectors.toList());

    } 

}
