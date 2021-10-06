package platform.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import platform.model.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code, java.lang.String> {
}
