package splitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import splitter.db.TransactionModel;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionModel, Long> {
}
