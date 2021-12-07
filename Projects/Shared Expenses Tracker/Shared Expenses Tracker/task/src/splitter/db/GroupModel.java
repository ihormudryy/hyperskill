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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class GroupModel implements Serializable {

    private static final long serialVersionUID = 5551707547269388327L;

    @ManyToMany(fetch = FetchType.EAGER,
        cascade = CascadeType.DETACH)
    @JoinColumn(name = "GroupID")
    private List<UserModel> users = new ArrayList<>();
    @Id
    @Column(name = "GroupID")
    @GeneratedValue
    private Long id;
    private String name;

    public GroupModel(String name) {
        this.name = name;
    }

    public GroupModel() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

}
