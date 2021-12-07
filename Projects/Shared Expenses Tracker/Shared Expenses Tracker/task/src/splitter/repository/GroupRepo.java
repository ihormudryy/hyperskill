package splitter.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import splitter.db.GroupModel;

@Repository
public interface GroupRepo extends JpaRepository<GroupModel, Long> {
    Optional<GroupModel> findByName(String name);
}
