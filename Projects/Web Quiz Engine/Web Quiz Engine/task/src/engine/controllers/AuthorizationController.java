package engine.controllers;

import engine.model.Message;
import engine.model.Roles;
import engine.user.User;
import engine.user.UserDetailServiceImpl;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
@Validated
public class AuthorizationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);
    private final String EMAIL_REGEX = ".+@.+\\..+";
    private final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    private final int PASSWORD_LENGTH = 5;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public UserDetailServiceImpl userDetailServiceImpl;

    @PostMapping(value="/register", consumes="application/json")
    @Transactional
    public ResponseEntity registerNewUser(@Valid @RequestBody @NotNull User user) {

        if (userDetailServiceImpl.exists(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "The email is already taken by another user!");
        }

        if (!pattern.matcher(user.getEmail()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "Email format is incorrect");
        }

        if (user.getPassword().length() < PASSWORD_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              "Password must be at least 5 characters long");
        }

        userDetailServiceImpl.register(new User(user.getEmail(),
                                                passwordEncoder().encode(user.getPassword()),
                                                Roles.USER));
        return new ResponseEntity("User has been successfully registered.", HttpStatus.OK);
    }
}
