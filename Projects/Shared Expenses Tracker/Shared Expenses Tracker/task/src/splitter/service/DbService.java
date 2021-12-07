package splitter.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import splitter.db.GroupModel;
import splitter.db.TransactionModel;
import splitter.db.UserModel;
import splitter.repository.GroupRepo;
import splitter.repository.TransactionRepo;
import splitter.repository.UserRepo;

@Service
public class DbService {

    private final UserRepo userRepository;
    private final GroupRepo groupRepository;
    private final TransactionRepo transactionRepository;

    @Autowired
    public DbService(UserRepo userRepository, GroupRepo projectRepository, TransactionRepo transactionRepository) {
        this.userRepository = userRepository;
        this.groupRepository = projectRepository;
        this.transactionRepository = transactionRepository;
    }

    public Optional<GroupModel> getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    private UserModel createUserIfNotExists(String name) {
        if (userRepository.findByName(name).isEmpty()) {
            return userRepository.save(new UserModel(name));
        } else {
            return userRepository.findByName(name).get();
        }
    }

    public GroupModel createGroupIfNotExists(String name) {
        if (groupRepository.findByName(name).isEmpty()) {
            return groupRepository.save(new GroupModel(name));
        } else {
            return groupRepository.findByName(name).get();
        }
    }

    @Transactional
    public void removeUserFromGroup(String groupName, String username) {
        String userToRemove = username.replaceAll("[+\\-]", "");
        UserModel user = createUserIfNotExists(userToRemove);
        GroupModel group = groupRepository.findByName(groupName)
                                          .orElseThrow(() -> new IllegalArgumentException(groupName + " not found"));
        group.getUsers().remove(user);
        groupRepository.save(group);
    }

    @Transactional
    public void addUserToGroupIfNotExists(String groupName, String username) {
        String userToAdd = username.replace("+", "");
        createUserIfNotExists(userToAdd);
        GroupModel group = createGroupIfNotExists(groupName);
        group.getUsers()
             .add(userRepository.findByName(userToAdd).get());
        groupRepository.save(group);
    }

    @Transactional
    public void processTransactionInDb(LocalDate date, double amount,
                                       String from, String to) {
        var transactionIn = new TransactionModel(amount, date, from, to);
        UserModel user = createUserIfNotExists(from);
        transactionRepository.save(transactionIn);
        userRepository.save(user);
        transactionIn.setUser(user);
        user.getTransactions().add(transactionIn);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void writeOffTransactions(LocalDate afterDate) {
        getAllUsers()
            .stream()
            .flatMap(u -> u.getTransactions().stream())
            .forEach(tx -> {
                LocalDate time = tx.getDateOfTransaction();
                if (time.isBefore(afterDate) || time.isEqual(afterDate)) {
                    transactionRepository.delete(tx);
                }
            });
    }

    public BigDecimal getBalanceWithBorrower(TransactionModel transaction, LocalDate byDate, boolean isOpen) {
        userRepository.findByName(transaction.getToUser())
                      .orElseThrow(() -> new RuntimeException(transaction.getToUser() + " was not found"));
        return userRepository.findByName(transaction.getFromUser())
                             .get()
                             .getTransactions()
                             .stream()
                             .filter(t -> transaction.getToUser().equals(t.getToUser()))
                             .filter(t -> {
                                 if (isOpen) {
                                     return t.getDateOfTransaction().isBefore(byDate.withDayOfMonth(1));
                                 } else {
                                     return t.getDateOfTransaction().isBefore(byDate) || t.getDateOfTransaction().isEqual(byDate);
                                 }
                             })
                             .map(v -> new BigDecimal(v.getAmount()))
                             .reduce(BigDecimal.ZERO, (subtotal, element) -> subtotal.add(element));
    }

    public void cleanGroup(String groupName) {
        GroupModel group = groupRepository.findByName(groupName)
                                          .orElseThrow(() -> new IllegalArgumentException(groupName + " not found"));
        group.setUsers(new ArrayList<>());
        groupRepository.save(group);
    }

    public List<UserModel> getUsersFromGroup(String s) {
        return groupRepository.findByName(s).orElseThrow(() -> new RuntimeException(s + " does not exist")).getUsers();
    }
}
