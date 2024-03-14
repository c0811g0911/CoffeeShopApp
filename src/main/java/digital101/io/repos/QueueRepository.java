package digital101.io.repos;

import digital101.io.domain.Queue;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QueueRepository extends JpaRepository<Queue, Long> {
}
