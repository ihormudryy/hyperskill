package splitter.regex;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexPatterns {

    public static final String date = "[0-9.]*\\s?";
    public static final String sum = "\\s+[0-9.]+\\s+";
    public static final String digit = "[\\d.]+";
    public static final String argument = "[\\w+]+\\s+";
    public static final String users = "\\([\\-+\\w+\\s+,]+\\)";
    public static final String groups = "\\([\\-+\\w+\\s*,\\)]+";
    public static final String action = date + argument + argument + argument + digit;
    public static final String purchase = date + argument + argument + argument + digit + "\\s*" + users;
    public static final String cashback = date + argument + argument + argument + digit + "\\s*" + groups;
    public static final String group = String.format("(group\\s+[show]+\\s+[A-Z]+)|(group\\s+[create|add|remove]+\\s+[A-Z]+\\s+%s)", groups);
    public static final String balance = date + "[\\w+]+\\s?[open|close]*";
    public static final String secretSanta = "secretSanta\\s+[A-Z]+";
    public static final String writeOff = date + "writeOff";

    public enum args {
        BORROW(RegexPatterns.action, Pattern.compile(RegexPatterns.action)),
        REPAY(RegexPatterns.action, Pattern.compile(RegexPatterns.action)),
        GROUP(RegexPatterns.group, Pattern.compile(RegexPatterns.group)),
        GROUPS(RegexPatterns.groups, Pattern.compile(RegexPatterns.groups)),
        PURCHASE(RegexPatterns.purchase, Pattern.compile(RegexPatterns.purchase)),
        BALANCE(RegexPatterns.balance, Pattern.compile(RegexPatterns.balance)),
        USERS(RegexPatterns.users, Pattern.compile(RegexPatterns.users)),
        AMOUNT(RegexPatterns.sum, Pattern.compile(RegexPatterns.sum)),
        CASHBACK(RegexPatterns.cashback, Pattern.compile(RegexPatterns.cashback)),
        WRITE_OFF(RegexPatterns.writeOff, Pattern.compile(RegexPatterns.writeOff)),
        SECRET_SANTA(RegexPatterns.secretSanta, Pattern.compile(RegexPatterns.secretSanta));

        public String command;
        public Pattern pattern;

        args(String command, Pattern pattern) {
            this.command = command;
            this.pattern = pattern;
        }

        public List<String> getMatches(String text) {
            return Arrays.stream(text.split("\\s+"))
                         .collect(Collectors.toList());
        }

        public List<String> getMatchedResults(String text) {
            return pattern.matcher(text)
                          .results()
                          .map(r -> r.group())
                          .collect(Collectors.toList());
        }
    }

}
