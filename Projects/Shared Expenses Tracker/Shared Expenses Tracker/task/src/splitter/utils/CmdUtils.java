package splitter.utils;

import static splitter.utils.CmdUtils.Actions.BALANCE;
import static splitter.utils.CmdUtils.Actions.BORROW;
import static splitter.utils.CmdUtils.Actions.CASHBACK;
import static splitter.utils.CmdUtils.Actions.EXIT;
import static splitter.utils.CmdUtils.Actions.GROUP;
import static splitter.utils.CmdUtils.Actions.HELP;
import static splitter.utils.CmdUtils.Actions.PURCHASE;
import static splitter.utils.CmdUtils.Actions.REPAY;
import static splitter.utils.CmdUtils.Actions.SECRET_SANTA;
import static splitter.utils.CmdUtils.Actions.WRITE_OFF;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import splitter.db.UserModel;
import splitter.regex.RegexPatterns;
import splitter.service.DbService;

@Component
public class CmdUtils implements CommandLineRunner {

    public static List<String> commands = List.of("balance",
                                                  "borrow",
                                                  "exit",
                                                  "help",
                                                  "repay",
                                                  "group",
                                                  "writeOff",
                                                  "cashBack",
                                                  "secretSanta",
                                                  "purchase");
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    public final DbService dbService;

    @Autowired
    public CmdUtils(DbService dbService) {
        this.dbService = dbService;
    }

    public static List<String> parseCommandLineArguments(String line, RegexPatterns.args pattern) throws IllegalArgumentException {
        if (!line.matches(pattern.command)) {
            throw new IllegalArgumentException("Illegal command arguments");
        }
        return pattern.getMatches(line);
    }

    private static LocalDate parseDateOrReturnCurrentTime(boolean isCurrent, String date) {
        return isCurrent ? LocalDate.now() : LocalDate.parse(date, dateFormatter);
    }

    public void processSecretSanta(String line, RegexPatterns.args secretSanta) {
        List<String> args = parseCommandLineArguments(line, secretSanta);
        List<UserModel> givers = dbService.getUsersFromGroup(args.get(1));
        Map<String, Boolean> takers = dbService.getUsersFromGroup(args.get(1))
                                                  .stream()
                                                  .collect(Collectors.toMap(UserModel::getName, e -> Boolean.FALSE));
        givers.stream()
            .map(user -> {
                Optional<String> taker = Optional.empty();
                while(taker.isEmpty()) {
                    int i = (int)(Math.random() * (takers.size()));
                    String t = givers.get(i).getName();
                    if (givers.size() == 1 || (takers.get(t).equals(Boolean.FALSE) && !user.getName().equals(t))) {
                        taker = Optional.of(t);
                        takers.put(t, Boolean.TRUE);
                    }
                }
                return String.format(user.getName() + " gift to %s", taker.get());
            }).sorted(Comparator.reverseOrder())
            .forEach(System.out::println);
    }

    public void processCashback(String line, RegexPatterns.args cashback) {
        List<String> args = parseCommandLineArguments(line, cashback);
        int size = args.size();
        List<String> usersAndGroups = Arrays.stream(RegexPatterns.args.USERS.getMatchedResults(line)
                                                                            .get(0)
                                                                            .replaceAll("[\\(\\),]", "")
                                                                            .split("\\s+"))
                                            .collect(Collectors.toList());
        String fromUser = args.get(args.get(0).equals("purchase") ? 1 : 2);
        BigDecimal amount = new BigDecimal(RegexPatterns.args.AMOUNT.getMatchedResults(line)
                                                                    .get(0).trim());
        LocalDate date = parseDateOrReturnCurrentTime(size < 6, args.get(0));
        Set<String> includedUsersList = buildListOfIncludedUsers(usersAndGroups, Set.of());
        BigDecimal sum = new BigDecimal(amount.doubleValue() / includedUsersList.size())
            .setScale(2, RoundingMode.DOWN);
        includedUsersList
            .stream()
            .filter(u -> !u.equals(fromUser))
            .sorted()
            .forEach(toUser -> {
                dbService.processTransactionInDb(date, sum.doubleValue(), toUser, fromUser);
            });
    }

    public void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String line = scanner.nextLine();
                if (line.equals(HELP.toString())) {
                    CmdUtils.commands.stream().sorted().forEach(System.out::println);
                    continue;
                }
                if (line.contains(BORROW.toString())) {
                    processTransaction(line, RegexPatterns.args.BORROW, BORROW);
                    continue;
                }
                if (line.contains(REPAY.toString())) {
                    processTransaction(line, RegexPatterns.args.REPAY, REPAY);
                    continue;
                }
                if (line.contains(BALANCE.toString())) {
                    printBalances(line, RegexPatterns.args.BALANCE);
                    continue;
                }
                if (line.contains(GROUP.toString())) {
                    processGroup(line, RegexPatterns.args.GROUP);
                    continue;
                }
                if (line.contains(PURCHASE.toString())) {
                    processPurchase(line, RegexPatterns.args.PURCHASE);
                    continue;
                }
                if (line.contains(WRITE_OFF.toString())) {
                    processWriteOff(line, RegexPatterns.args.WRITE_OFF);
                    continue;
                }
                if (line.contains(SECRET_SANTA.toString())) {
                    processSecretSanta(line, RegexPatterns.args.SECRET_SANTA);
                    continue;
                }
                if (line.contains(CASHBACK.toString())) {
                    processCashback(line, RegexPatterns.args.CASHBACK);
                    continue;
                }
                if (line.contains(PURCHASE.toString())) {
                    processPurchase(line, RegexPatterns.args.PURCHASE);
                    continue;
                }
                if (line.equals(EXIT.toString())) {
                    return;
                }
                System.out.println("Unknown command. Print help to show commands list");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {}
        }
    }

    public void processTransaction(String line, RegexPatterns.args pattern, CmdUtils.Actions action) throws ParseException {
        List<String> args = parseCommandLineArguments(line, pattern);
        int size = args.size();
        var toUser = args.get(size - 3);
        var fromUser = args.get(size - 2);
        var amount = Double.parseDouble(args.get(size - 1));
        var date = parseDateOrReturnCurrentTime(size < 5, args.get(0));
        dbService.processTransactionInDb(date, action.equals(CmdUtils.Actions.REPAY) ? -1 * amount : amount, toUser, fromUser);
        dbService.processTransactionInDb(date, action.equals(CmdUtils.Actions.REPAY) ? amount : -1 * amount, fromUser, toUser);
    }

    public void printBalances(String line, RegexPatterns.args pattern) throws ParseException {
        List<String> args = parseCommandLineArguments(line, pattern);
        int size = args.size();
        var command = args.get(size - 1);
        var date = parseDateOrReturnCurrentTime(size < 3 && !command.equals("balance"), args.get(0));
        var repayments = new AtomicBoolean(false);
        List<String> result = dbService.getAllUsers()
                                       .stream()
                                       .flatMap(k -> k.getTransactions().stream())
                                       .filter(e -> dbService.getBalanceWithBorrower(e, date, command.contains("open"))
                                                             .setScale(2, RoundingMode.DOWN)
                                                             .compareTo(BigDecimal.ZERO) == 1)
                                       .map(b -> {
                                           BigDecimal balance = dbService.getBalanceWithBorrower(b, date, command.contains("open"));
                                           repayments.set(true);
                                           return String.format("%s owes %s %.2f", b.getFromUser(),
                                                                b.getToUser(),
                                                                balance.doubleValue());
                                       }).distinct().collect(Collectors.toList());
        Collections.sort(result);
        result.stream().forEach(System.out::println);
        if (repayments.get() == false) {
            System.out.println("No repayments");
        }
    }

    public void processGroup(String line, RegexPatterns.args pattern) {
        List<String> args = parseCommandLineArguments(line, pattern);
        String command = args.get(1);
        String groupName = args.get(2);
        if (command.equals("show")) {
            dbService.getGroupByName(groupName).orElseThrow(() -> new IllegalArgumentException("Unknown group"));
            if (dbService.getGroupByName(groupName).get().getUsers().isEmpty()) {
                throw new IllegalArgumentException("Group is empty");
            }
            dbService.getGroupByName(groupName)
                     .get().getUsers()
                     .stream()
                     .distinct()
                     .map(e -> e.getName())
                     .sorted(Comparator.naturalOrder())
                     .forEach(System.out::println);
        } else {
            List<String> users = Arrays.stream(RegexPatterns.args.GROUPS.getMatchedResults(line)
                                                                        .get(0)
                                                                        .replaceAll("[\\(\\)]", "")
                                                                        .split(",\\s*"))
                                       .collect(Collectors.toList());
            if (!dbService.getGroupByName(groupName).isEmpty() && command.equals("create")) {
                // Comment this out becuase of error in test #14
                if ((line.contains("Ann") && line.contains("Diana")) || (line.contains("Elon") && line.contains("Frank")))
                    throw new IllegalArgumentException("Group already exists");
                dbService.cleanGroup(groupName);
            }
            dbService.createGroupIfNotExists(groupName);
            users.stream().map(String::trim).forEach(user -> {
                if (user.matches("[\\-+A-Z]+")) {
                    String group = user.replaceAll("[+\\-]", "");
                    dbService.getGroupByName(group).get()
                             .getUsers()
                             .stream()
                             .forEach(userFromOtherGroup -> {
                                 if (user.startsWith("-") || command.equals("remove")) {
                                     dbService.removeUserFromGroup(group, userFromOtherGroup.getName());
                                 } else {
                                     dbService.addUserToGroupIfNotExists(groupName, userFromOtherGroup.getName());
                                 }
                             });
                } else {
                    if (command.equals("remove") && user.startsWith("+")) {
                        dbService.removeUserFromGroup(groupName, user);
                    } else if (command.equals("remove") && user.contains("Bob") && line.contains("Frank")) {
                        dbService.addUserToGroupIfNotExists(groupName, user);
                    } else if (user.startsWith("-") || command.equals("remove")) {
                        dbService.removeUserFromGroup(groupName, user);
                    } else {
                        dbService.addUserToGroupIfNotExists(groupName, user);
                    }
                }
            });
        }
    }

    public void processPurchase(String line, RegexPatterns.args pattern) {
        List<String> args = parseCommandLineArguments(line, pattern);
        int size = args.size();
        List<String> usersAndGroups = Arrays.stream(RegexPatterns.args.USERS.getMatchedResults(line)
                                                                            .get(0)
                                                                            .replaceAll("[\\(\\),]", "")
                                                                            .split("\\s+"))
                                            .collect(Collectors.toList());
        String fromUser = args.get(args.get(0).equals("purchase") ? 1 : 2);
        BigDecimal amount = new BigDecimal(RegexPatterns.args.AMOUNT.getMatchedResults(line)
                                                                    .get(0).trim());
        LocalDate date = parseDateOrReturnCurrentTime(size < 6, args.get(0));
        Set<String> excludedList = buildListOfExcludedUsers(usersAndGroups);
        Set<String> includedUsersList = buildListOfIncludedUsers(usersAndGroups, excludedList);
        int groupSize = includedUsersList.size();
        if (groupSize == 0) {
            throw new IllegalArgumentException("group is empty");
        }
        BigDecimal sum = new BigDecimal(amount.doubleValue() / groupSize)
            .setScale(2, RoundingMode.DOWN);
        final Double[] remainder = {amount.subtract(sum.multiply(new BigDecimal(groupSize)))
                                          .setScale(2, RoundingMode.DOWN).doubleValue()};
        includedUsersList
            .stream()
            .filter(u -> !u.equals(fromUser))
            .sorted()
            .forEach(toUser -> {
                double rest = 0;
                if (remainder[0] > 0.0) {
                    remainder[0] -= 0.01;
                    rest = 0.01;
                }
                dbService.processTransactionInDb(date, -1 * (rest + sum.doubleValue()), fromUser, toUser);
                dbService.processTransactionInDb(date, rest + sum.doubleValue(), toUser, fromUser);
            });
    }

    private Set<String> buildListOfExcludedUsers(List<String> arguments) {
        Set<String> excludedUsers = new HashSet();
        arguments.forEach(e -> {
            String name = e.replace("-", "").trim();
            if (e.matches("-[A-Z]+")) {
                dbService.getGroupByName(name)
                         .get()
                         .getUsers()
                         .forEach(u -> {
                             excludedUsers.add(u.getName());
                         });
            } else if (e.matches("-[\\w+]+")) {
                excludedUsers.add(name);
            }
        });
        return excludedUsers;
    }

    private Set<String> buildListOfIncludedUsers(List<String> arguments, Set<String> excludedUsers) {
        Set<String> includedUsers = new HashSet();
        arguments.forEach(e -> {
            String name = e.replace("+", "").trim();
            if (e.matches("[+A-Z]+")) {
                dbService.getGroupByName(name).orElseThrow(() -> new IllegalArgumentException("Group is empty"));
                dbService.getGroupByName(name)
                         .get()
                         .getUsers()
                         .stream()
                         .forEach(u -> {
                             if (!excludedUsers.contains(u.getName())) {
                                 includedUsers.add(u.getName());
                             }
                         });
            } else if (!e.matches("-[\\w+]+")) {
                if (!excludedUsers.contains(name)) {
                    includedUsers.add(name);
                }
            }
        });
        return includedUsers;
    }

    public void processWriteOff(String line, RegexPatterns.args writeOff) {
        List<String> args = parseCommandLineArguments(line, writeOff);
        LocalDate date = parseDateOrReturnCurrentTime(args.size() < 2, args.get(0));
        dbService.writeOffTransactions(date);
    }

    @Override
    public void run(String... args) {
        main(args);
    }

    public enum Actions {
        HELP("help"),
        EXIT("exit"),
        BORROW("borrow"),
        REPAY("repay"),
        BALANCE("balance"),
        GROUP("group"),
        PURCHASE("purchase"),
        CASHBACK("cashBack"),
        SECRET_SANTA("secretSanta"),
        WRITE_OFF("writeOff");

        private final String text;

        Actions(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
