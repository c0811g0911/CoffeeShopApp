package digital101.io.service;

import digital101.io.domain.Menu;
import digital101.io.domain.MenuItem;
import digital101.io.domain.Shop;
import digital101.io.model.MenuDto;
import digital101.io.repos.MenuItemRepository;
import digital101.io.repos.MenuRepository;
import digital101.io.repos.ShopRepository;
import digital101.io.util.NotFoundException;
import digital101.io.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuService(final MenuRepository menuRepository,
                       final ShopRepository shopRepository, final MenuItemRepository menuItemRepository) {
        this.menuRepository = menuRepository;
        this.shopRepository = shopRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuDto> findAll() {
        final List<Menu> menus = menuRepository.findAll(Sort.by("id"));
        return menus.stream()
                .map(menu -> mapToDTO(menu, new MenuDto()))
                .toList();
    }

    public MenuDto get(final Long id) {
        return menuRepository.findById(id)
                .map(menus -> mapToDTO(menus, new MenuDto()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MenuDto menuDto) {
        final Menu menu = new Menu();
        mapToEntity(menuDto, menu);
        return menuRepository.save(menu).getId();
    }

    public void update(final Long id, final MenuDto menuDto) {
        final Menu menu = menuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(menuDto, menu);
        menuRepository.save(menu);
    }

    public void delete(final Long id) {
        menuRepository.deleteById(id);
    }

    private MenuDto mapToDTO(final Menu menu, final MenuDto menuDto) {
        menuDto.setId(menu.getId());
        menuDto.setStatus(menu.getStatus());
        menuDto.setCreatedAt(menu.getCreatedAt());
        menuDto.setUpdatedAt(menu.getUpdatedAt());
        return menuDto;
    }

    private Menu mapToEntity(final MenuDto menuDto, final Menu menu) {
        menu.setStatus(menuDto.getStatus());
        menu.setCreatedAt(menuDto.getCreatedAt());
        menu.setUpdatedAt(menuDto.getUpdatedAt());
        return menu;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Menu menu = menuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Shop shops = shopRepository.findFirstByMenu(menu);
        if (shops != null) {
            referencedWarning.setKey("menus.shops.menu.referenced");
            referencedWarning.addParam(shops.getId());
            return referencedWarning;
        }
        final MenuItem menuMenuItems = menuItemRepository.findFirstByMenu(menu);
        if (menuMenuItems != null) {
            referencedWarning.setKey("menus.menuItems.menu.referenced");
            referencedWarning.addParam(menuMenuItems.getId());
            return referencedWarning;
        }
        return null;
    }

}
