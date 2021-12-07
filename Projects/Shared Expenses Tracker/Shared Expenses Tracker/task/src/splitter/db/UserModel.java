package splitter.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserModel implements Serializable {

    private static final long serialVersionUID = 5552347547269388327L;
    @OneToMany(fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE)
    @JoinColumn(name = "UserID")
    public List<TransactionModel> transactions = new ArrayList<>();
    @Id
    @Column(name = "UserID")
    @GeneratedValue
    private Long id;
    private String name;

    public UserModel(String name) {
        this.name = name;
        this.transactions = new ArrayList<>();
    }

    public UserModel() {

    }

    public String getName() {
        return name;
    }

    public List<TransactionModel> getTransactions() {
        return transactions;
    }

    public Long getId() {
        return id;
    }
}
