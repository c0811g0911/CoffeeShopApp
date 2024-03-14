package digital101.io.repos;

import digital101.io.domain.MenuItem;
import digital101.io.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    MenuItem findFirstByMenu(Menu menus);

}
