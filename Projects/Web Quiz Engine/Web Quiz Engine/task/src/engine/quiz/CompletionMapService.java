package engine.quiz;

import engine.user.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CompletionMapService {

    @Autowired
    CompletionMapRepository completionMapRepository;

    public void save(CompletionMap map) {
        completionMapRepository.save(map);
    }

    public List<CompletionMap> findAllByUser(User user_id, Pageable pageable) {
        return completionMapRepository.findAllByUser(user_id, pageable).getContent();
    }
}
