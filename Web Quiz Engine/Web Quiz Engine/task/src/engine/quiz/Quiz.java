package engine.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Quiz {

    @JsonProperty
    @Column
    private String title;

    @JsonProperty
    @Column
    private String text;

    @JsonProperty
    @ElementCollection
    private List<String> options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ElementCollection
    private List<Integer> answer;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    private Long id;

    @ElementCollection
    @JsonIgnore
    @OneToMany(
        cascade = CascadeType.REMOVE,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    @JoinColumn(name = "QUIZ_ID")
    private List<CompletionMap> completions = new ArrayList<>();

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "USER_ID")
    private Long user_id;

    public Quiz() {
        options = new ArrayList<>();
        answer = new ArrayList<>();
    }

    public Quiz(@NotNull String title,
                @NotNull String text,
                List<String> options,
                List<Integer> answer) {
        this.title = title;
        this.text = text;
        this.options = options != null ? options : new ArrayList<>();
        this.answer = answer != null ? answer : new ArrayList<>();
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public List<Integer> getAnswer() {
        return this.answer;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Long getUserId() { return user_id; }

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setUserId(Long user_id) { this.user_id = user_id; }

    public List<CompletionMap> getCompletions() {
        return this.completions;
    }

    public void setCompletions(CompletionMap completion) {
        this.completions.add(completion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(id, quiz.id) &&
            Objects.equals(text, quiz.getText()) &&
            Objects.equals(title, quiz.getTitle()) &&
            Objects.equals(answer, quiz.getAnswer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, title, answer);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Quiz{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", options='").append(options.stream().map(Object::toString)).append('\'');
        sb.append(", answer='").append(answer.stream().map(Object::toString)).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
