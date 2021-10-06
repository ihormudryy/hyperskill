package engine.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import engine.model.Roles;
import engine.quiz.Quiz;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class User implements UserDetails {

    private final int PASSWORD_LENGTH = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UID", unique = true)
    private Long id;

    @JsonIgnore
    private Roles role;
    @JsonIgnore
    private boolean isEnabled = true;
    @JsonIgnore
    private boolean isCredentialsNonExpired = true;
    @JsonIgnore
    private boolean isAccountNonExpired = true;
    @JsonIgnore
    private boolean isAccountNonLocked = true;

    @JsonProperty("email")
    @Email(regexp = ".+@.+\\..+")
    @Column(name = "email", unique = true)
    @NotBlank(message = "Email can not be empty!")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotBlank(message = "Password can not be empty!")
    @Size(min = PASSWORD_LENGTH)
    private String password;

    @OneToMany(
        cascade = CascadeType.REMOVE,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    @JoinColumn(
        name = "USER_ID")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Quiz> quizzes = new ArrayList<>();

    public User() {}

    public User(User user, Roles role) {
        this.email = user.getEmail();
        this.password = user.password;
        this.role = role;
    }

    public User(@Email String email,
                @Size(min=5) String password,
                Roles role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    @Transactional
    public List<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes.addAll(quizzes);
    }

    public void setQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
    }

    public Quiz getQuiz(int i) {
        return this.quizzes.get(i);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() { return isEnabled; }

    public void setPassword(String password) {
        this.password = password;
    }

}
