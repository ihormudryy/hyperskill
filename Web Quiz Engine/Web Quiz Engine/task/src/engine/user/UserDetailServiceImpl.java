package engine.user;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    public UserDetailServiceImpl() {}

    public boolean exists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void register(User user) {

        userRepository.save(user);
    }

    public void save(User user) {

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUserEntity = userRepository.findByEmail(username);
        if (optionalUserEntity.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found.");
        }
        return optionalUserEntity.get();
    }
}
