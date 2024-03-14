package digital101.io.service;

import digital101.io.domain.*;
import digital101.io.model.OrderDto;
import digital101.io.repos.CustomerRepository;
import digital101.io.repos.MenuItemRepository;
import digital101.io.repos.OrderRepository;
import digital101.io.repos.QueueRepository;
import digital101.io.util.NotFoundException;
import digital101.io.util.OrderExceedException;
import digital101.io.util.ShopServiceTimeException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final CustomerRepository customerRepository;

    private final QueueRepository queueRepository;

    public OrderService(final OrderRepository orderRepository,
                        final MenuItemRepository menuItemRepository,
                        final CustomerRepository customerRepository,
                        final QueueRepository queueRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.customerRepository = customerRepository;
        this.queueRepository = queueRepository;
    }

    public List<OrderDto> findAll() {
        final List<Order> orderList = orderRepository.findAll(Sort.by("id"));
        return orderList.stream()
                .map(order -> mapToDTO(order, new OrderDto()))
                .toList();
    }

    public OrderDto get(final Long id) {
        return orderRepository.findById(id)
                .map(orders -> mapToDTO(orders, new OrderDto()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OrderDto orderDto) {
        final Order order = new Order();
        mapToEntity(orderDto, order);
        return orderRepository.save(order).getId();
    }

    public void update(final Long id, final OrderDto orderDto) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDto, order);
        orderRepository.save(order);
    }

    public void delete(final Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDto mapToDTO(final Order order, final OrderDto orderDto) {
        orderDto.setId(order.getId());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setStatus(order.getStatus());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setUpdatedAt(order.getUpdatedAt());
        orderDto.setMenuItemId(order.getMenuItem() == null ? null : order.getMenuItem().getId());
        orderDto.setCustomerId(order.getCustomer() == null ? null : order.getCustomer().getId());
        return orderDto;
    }

    private Order mapToEntity(final OrderDto orderDto, final Order order) {
        order.setQuantity(orderDto.getQuantity());
        order.setStatus(orderDto.getStatus());
        order.setCreatedAt(orderDto.getCreatedAt());
        order.setUpdatedAt(orderDto.getUpdatedAt());
        final MenuItem menuItem = orderDto.getMenuItemId() == null ? null : menuItemRepository.findById(orderDto.getMenuItemId())
                .orElseThrow(() -> new NotFoundException("menuItem not found"));
        order.setMenuItem(menuItem);
        final Customer customer = orderDto.getCustomerId() == null ? null : customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        order.setCustomer(customer);

        return order;
    }

    public OrderDto placeOrderByCustomer(OrderDto orderDto){

        //TODO: get customerId from token
        orderDto.setCustomerId(1l);//fake customer id

        Long itemId = orderDto.getMenuItemId();
        MenuItem item = menuItemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Menu Item not found"));
        Shop shop = item.getMenu().getShop();
        LocalTime now = LocalTime.now();

        LocalTime openTime = LocalTime.parse(shop.getOpenTime());
        LocalTime closedTime = LocalTime.parse(shop.getClosedTime());

        if(now.isBefore(openTime) || now.isAfter(closedTime)){
            throw new ShopServiceTimeException("not in service time");
        }

        Queue queue = shop.getQueue();

        List<Order> listInProgressOrderInQueue = orderRepository.findByQueueAndStatus(queue, "IN_PROGRESS");
        if(listInProgressOrderInQueue.size() >= queue.getMaxQueueSize()){
            throw new OrderExceedException("queue is maximum processing, please try it later");
        }

        Order order = new Order();
        mapToDTO(order, orderDto);
        order.setStatus("IN_PROGRESS");
        order.setQueue(queue);
        order =orderRepository.save(order);
        orderDto.setId(order.getId());
        return orderDto;
    }

    public OrderDto existQueue(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus("EXISTED");

        order = orderRepository.save(order);
        OrderDto orderDto = new OrderDto();
        mapToDTO(order,orderDto);

        return orderDto;
    }

}
