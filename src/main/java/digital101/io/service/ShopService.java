package digital101.io.service;

import digital101.io.domain.Menu;
import digital101.io.domain.Queue;
import digital101.io.domain.Shop;
import digital101.io.model.ShopDto;
import digital101.io.repos.MenuRepository;
import digital101.io.repos.QueueRepository;
import digital101.io.repos.ShopRepository;
import digital101.io.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final QueueRepository queuesRepository;

    public ShopService(final ShopRepository shopRepository,
                       final MenuRepository menuRepository, final QueueRepository queuesRepository) {
        this.shopRepository = shopRepository;
        this.menuRepository = menuRepository;
        this.queuesRepository = queuesRepository;
    }

    public List<ShopDto> findAll() {
        final List<Shop> shopList = shopRepository.findAll(Sort.by("id"));
        return shopList.stream()
                .map(shop -> mapToDTO(shop, new ShopDto()))
                .toList();
    }

    public ShopDto get(final Long id) {
        return shopRepository.findById(id)
                .map(shop -> mapToDTO(shop, new ShopDto()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ShopDto shopDto) {
        final Shop shop = new Shop();
        mapToEntity(shopDto, shop);
        return shopRepository.save(shop).getId();
    }

    public void update(final Long id, final ShopDto shopDto) {
        final Shop shop = shopRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(shopDto, shop);
        shopRepository.save(shop);
    }

    public void delete(final Long id) {
        shopRepository.deleteById(id);
    }

    private ShopDto mapToDTO(final Shop shop, final ShopDto shopDto) {
        shopDto.setId(shop.getId());
        shopDto.setName(shop.getName());
        shopDto.setDescription(shop.getDescription());
        shopDto.setAddress(shop.getAddress());
        shopDto.setPhone(shop.getPhone());
        shopDto.setUserName(shop.getUserName());
        shopDto.setPassword(shop.getPassword());
        shopDto.setLongitude(shop.getLongitude());
        shopDto.setLatitude(shop.getLatitude());
        shopDto.setCreatedAt(shop.getCreatedAt());
        shopDto.setUpdatedAt(shop.getUpdatedAt());
        shopDto.setMenu(shop.getMenu() == null ? null : shop.getMenu().getId());
        shopDto.setQueue(shop.getQueue() == null ? null : shop.getQueue().getId());
        return shopDto;
    }

    private Shop mapToEntity(final ShopDto shopDto, final Shop shop) {
        shop.setName(shopDto.getName());
        shop.setDescription(shopDto.getDescription());
        shop.setAddress(shopDto.getAddress());
        shop.setPhone(shopDto.getPhone());
        shop.setUserName(shopDto.getUserName());
        shop.setPassword(shopDto.getPassword());
        shop.setLongitude(shopDto.getLongitude());
        shop.setLatitude(shopDto.getLatitude());
        shop.setCreatedAt(shopDto.getCreatedAt());
        shop.setUpdatedAt(shopDto.getUpdatedAt());
        final Menu menu = shopDto.getMenu() == null ? null : menuRepository.findById(shopDto.getMenu())
                .orElseThrow(() -> new NotFoundException("menu not found"));
        shop.setMenu(menu);
        final Queue queue = shopDto.getQueue() == null ? null : queuesRepository.findById(shopDto.getQueue())
                .orElseThrow(() -> new NotFoundException("queue not found"));
        shop.setQueue(queue);
        return shop;
    }

    public boolean userNameExists(final String userName) {
        return shopRepository.existsByUserNameIgnoreCase(userName);
    }

    public boolean menuExists(final Long id) {
        return shopRepository.existsByMenuId(id);
    }

    public boolean queueExists(final Long queueId) {
        Queue queue = queuesRepository.findById(queueId).orElseThrow(NotFoundException::new);
        return shopRepository.existsByQueue(queue);
    }

}
