package splitter.db;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class TransactionModel implements Serializable {

    private static final long serialVersionUID = 5551707547269388427L;
    LocalDate dateOfTransaction;
    double amount;
    String fromUser;
    String toUser;
    @Id
    @Column(name = "TransactionID")
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "UserID")
    private UserModel user;

    public TransactionModel(double amount, LocalDate date, String from, String toUser) {
        this.dateOfTransaction = date;
        this.amount = amount;
        this.fromUser = from;
        this.toUser = toUser;
    }

    public TransactionModel() {

    }

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(LocalDate date) {
        this.dateOfTransaction = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String from) {
        this.fromUser = from;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String to) {
        this.toUser = to;
    }

    public Long getId() {
        return id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
