package engine.quiz;

import engine.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionMapRepository extends PagingAndSortingRepository<CompletionMap, Long> {
    Page<CompletionMap> findAllByUser(User user_id, Pageable pageable);
}