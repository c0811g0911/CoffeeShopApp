package digital101.io.service;

import digital101.io.domain.Menu;
import digital101.io.domain.MenuItem;
import digital101.io.domain.Order;
import digital101.io.model.MenuItemDto;
import digital101.io.repos.MenuItemRepository;
import digital101.io.repos.MenuRepository;
import digital101.io.repos.OrderRepository;
import digital101.io.util.NotFoundException;
import digital101.io.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public MenuItemService(final MenuItemRepository menuItemRepository,
                           final MenuRepository menuRepository, final OrderRepository orderRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    public List<MenuItemDto> findAll() {
        final List<MenuItem> items = menuItemRepository.findAll(Sort.by("id"));
        return items.stream()
                .map(item -> mapToDTO(item, new MenuItemDto()))
                .toList();
    }

    public MenuItemDto get(final Long id) {
        return menuItemRepository.findById(id)
                .map(item -> mapToDTO(item, new MenuItemDto()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MenuItemDto menuItemDto) {
        final MenuItem menuItem = new MenuItem();
        mapToEntity(menuItemDto, menuItem);
        return menuItemRepository.save(menuItem).getId();
    }

    public void update(final Long id, final MenuItemDto menuItemDto) {
        final MenuItem menuItems = menuItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(menuItemDto, menuItems);
        menuItemRepository.save(menuItems);
    }

    public void delete(final Long id) {
        menuItemRepository.deleteById(id);
    }

    private MenuItemDto mapToDTO(final MenuItem menuItem, final MenuItemDto menuItemDto) {
        menuItemDto.setId(menuItem.getId());
        menuItemDto.setName(menuItem.getName());
        menuItemDto.setPrice(menuItem.getPrice());
        menuItemDto.setCurrency(menuItem.getCurrency());
        menuItemDto.setCreatedAt(menuItem.getCreatedAt());
        menuItemDto.setUpdatedAt(menuItem.getUpdatedAt());
        menuItemDto.setMenuId(menuItem.getMenu() == null ? null : menuItem.getMenu().getId());
        return menuItemDto;
    }

    private MenuItem mapToEntity(final MenuItemDto menuItemDto, final MenuItem menuItem) {
        menuItem.setName(menuItemDto.getName());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setCurrency(menuItemDto.getCurrency());
        menuItem.setCreatedAt(menuItemDto.getCreatedAt());
        menuItem.setUpdatedAt(menuItemDto.getUpdatedAt());
        final Menu menu = menuItemDto.getMenuId() == null ? null : menuRepository.findById(menuItemDto.getMenuId())
                .orElseThrow(() -> new NotFoundException("menu not found"));
        menuItem.setMenu(menu);
        return menuItem;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Order order = orderRepository.findFirstByMenuItem(menuItem);
        if (order != null) {
            referencedWarning.setKey("menuItems.orders.menuItem.referenced");
            referencedWarning.addParam(order.getId());
            return referencedWarning;
        }
        return null;
    }

}
