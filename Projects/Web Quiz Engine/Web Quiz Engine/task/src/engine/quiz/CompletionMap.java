package engine.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import engine.user.User;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CompletionMap {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Id
    private Long uid;

    @JsonIgnore
    @ManyToOne
    private User user;

    private Long id;
    private Date completedAt;

    public CompletionMap() {
    }

    public CompletionMap(Long id, Date date) {
        this.id = id;
        this.completedAt = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}
