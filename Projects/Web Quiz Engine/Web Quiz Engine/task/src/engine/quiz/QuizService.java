package engine.quiz;

import engine.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QuizService {

    @Autowired
    public QuizRepository quizRepository;

    public QuizService(){}

    public void addQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public void saveQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public Quiz find(long id) {
        return quizRepository.findById(id).isPresent() ? quizRepository.findById(id).get() : null;
    }

    public List<Quiz> findAll() {
        return (List<Quiz>) quizRepository.findAll();
    }

    public List<Quiz> findAll(int pageNo,
                              int pageSize,
                              String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Quiz> pagedResult = quizRepository.findAll(pageable);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Quiz>();
        }
    }

    public List<CompletionMap> findAllCompleted(User user,
                                                Integer page,
                                                Integer pageSize,
                                                Sort sortBy) {
        Pageable pageable = PageRequest.of(page, pageSize, sortBy);
        List<CompletionMap> completed = new ArrayList<>();
        user.getQuizzes().forEach(quiz -> {
            completed.addAll(quiz.getCompletions());
        });
        return completed;
    }

    public List<Quiz> findByUserId(Long id) {
        List<Quiz> quizzes = new ArrayList<Quiz>();
        quizRepository.findAll().forEach(e -> { if (e.getUserId().equals(id)) quizzes.add(e); });
        return quizzes;
    }

    public Long count() {
        return quizRepository.count();
    }

    public void delete(Quiz quiz) {
        quizRepository.delete(quiz);
    }

    public Optional findById(Long id) {
        return quizRepository.findById(id);
    }

    public void delete(Long id) {
        Optional quiz = quizRepository.findById(id);
        if (quiz.isPresent()) {
            quizRepository.delete(quizRepository.findById(id).get());
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content was found");
        }
    }

    public void deleteAll() {
        quizRepository.deleteAll();
    }
}
