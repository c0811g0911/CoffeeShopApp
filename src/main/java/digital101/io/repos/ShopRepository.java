package digital101.io.repos;


import digital101.io.domain.Menu;
import digital101.io.domain.Queue;
import digital101.io.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShopRepository extends JpaRepository<Shop, Long> {

    Shop findFirstByMenu(Menu menus);

    Shop findFirstByQueue(Queue queues);

    boolean existsByUserNameIgnoreCase(String userName);

    boolean existsByMenuId(Long id);

    boolean existsByQueue(Queue queue);

}
