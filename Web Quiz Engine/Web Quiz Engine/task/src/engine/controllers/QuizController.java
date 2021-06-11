package engine.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.content.CompletedQuiz;
import engine.content.Content;
import engine.model.Answer;
import engine.model.Message;
import engine.quiz.CompletionMap;
import engine.quiz.CompletionMapRepository;
import engine.quiz.CompletionMapService;
import engine.quiz.Quiz;
import engine.quiz.QuizService;
import engine.user.User;
import engine.user.UserDetailServiceImpl;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public QuizService quizService;

    @Autowired
    public UserDetailServiceImpl userDetailService;

    @Autowired
    public CompletionMapService completionMapService;

    @Autowired
    public CompletionMapRepository completionMapRepository;

    /*
        I will leave authentication part commented out
        as my own requirements interpretation that only user who created a quiz
        should have access to solve it and fetch information about it.

        Maybe it would be useful for someone who want to make granular access
        management.
     */
    @GetMapping(value="/{id}")
    public ResponseEntity getQuizById(@PathVariable("id") int id,
                                      Authentication authentication) throws JsonProcessingException {
        User user = (User) authentication.getPrincipal();
        Quiz quiz = quizService.find(id);
        if (Objects.equals(quiz, null)) {
            return new ResponseEntity<String>("Quiz was not found.", HttpStatus.NOT_FOUND);
        } //else if (user.getId()
         //              .equals(quiz.getUserId())) {
            return ResponseEntity.ok(objectMapper
                                         .writerWithDefaultPrettyPrinter()
                                         .writeValueAsString(quiz));
        //} else {
        //    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
        //                                      "Unauthorized access");
        //}
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity createQuiz(@Valid @RequestBody @NotNull Quiz quiz,
                                     Authentication authentication) {
        if (Objects.equals(quiz.getText(), null) ||
            Objects.equals(quiz.getTitle(), null) ||
            Objects.equals(quiz.getText(), "") ||
            Objects.equals(quiz.getTitle(), "") ||
            quiz.getOptions().size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "Something wrong with JSON input");
        } else {
            User user = (User) authentication.getPrincipal();
            user.setQuiz(quiz);
            quiz.setUserId(user.getId());
            quizService.addQuiz(quiz);
            userDetailService.save(user);
            return ResponseEntity.ok(quiz);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllExistingQuizzes(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           Authentication authentication)
            throws JsonProcessingException {

        User user = (User) authentication.getPrincipal();
        Content content = new Content();
        content.setContent(quizService.findAll(page, pageSize, sortBy));
        content.setSize(pageSize);
        content.setSort(Sort.by(sortBy));
        return new ResponseEntity(objectMapper
                                      .writerWithDefaultPrettyPrinter()
                                      .writeValueAsString(content),
                                  new HttpHeaders(),
                                  HttpStatus.OK);
    }

    @GetMapping(value="/completed")
    public ResponseEntity<?> getAllCompletedQuizzes(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(defaultValue = "completedAt") String sortBy,
                                           Authentication authentication)
            throws JsonProcessingException {

        User user = (User) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());
        CompletedQuiz completed = new CompletedQuiz(page, pageSize, Sort.by(sortBy).descending());
        completed.setContent(completionMapService.findAllByUser(user, pageable));
        return new ResponseEntity(objectMapper
                                      .writerWithDefaultPrettyPrinter()
                                      .writeValueAsString(completed),
                                  new HttpHeaders(),
                                  HttpStatus.OK);
    }

    @PostMapping(value="/{id}/solve")
    public Message createQuiz(@PathVariable("id") Long id,
                              @Valid @RequestBody Answer answers,
                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Quiz quiz = quizService.find(id);
        logger.info("Solve quiz #" + id + " with answers "
                        + answers.getAnswer()
                        + " and expected answers " + quiz.getAnswer());
        if (Objects.equals(answers.getAnswer(), null)) {
            logger.error("Failed to parse JSON input");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse JSON input");
        } //else if (user.getId()
          //             .equals(quiz.getUserId())) {
            if (Arrays.equals(
                quiz.getAnswer().stream().sorted().toArray(),
                answers.getAnswer().stream().sorted().toArray())){

                CompletionMap solved = new CompletionMap(quiz.getId(), new Date());
                solved.setUser(user);
                quiz.setCompletions(solved);
                completionMapService.save(solved);
                quizService.saveQuiz(quiz);
                return new Message(true);
            } else {
                return new Message(false);
            }
        //} else {
        //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        //                                      "Unauthorized access");
        //}
    }

    @DeleteMapping(value="deleteAll")
    public ResponseEntity deleteAllQuizzes(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        user.getQuizzes().forEach(quizService::delete);
        return ResponseEntity.ok("All quizzes for your account were successfully deleted.");
    }

    @DeleteMapping(value="{id}")
    public ResponseEntity deleteQuiz(@PathVariable("id") Long id,
                                     Authentication authentication) throws JsonProcessingException {
        User user = (User) authentication.getPrincipal();
        Optional quiz = quizService.findById(id);
        if (Objects.equals(quiz.isEmpty(), true)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                              "No content was found");
        } else if (!quizService.find(id).getUserId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                              "Access forbidden");
        } else {
            quizService.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
