package digital101.io.repos;

import digital101.io.domain.Customer;
import digital101.io.domain.MenuItem;
import digital101.io.domain.Order;
import digital101.io.domain.Queue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByMenuItem(MenuItem menuItems);

    Order findFirstByCustomer(Customer customers);

    List<Order> findByQueueAndStatus(Queue queue, String status);
}
