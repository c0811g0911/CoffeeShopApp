package digital101.io.controller.rest;

import digital101.io.model.*;
import digital101.io.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

    private final CustomerService customerService;

    private final ShopService shopService;

    private final OrderService orderService;

    private final MenuItemService menuItemService;

    public CustomerResource(final CustomerService customerService, final ShopService shopService, final OrderService orderService, final MenuItemService menuItemService) {
        this.customerService = customerService;
        this.shopService = shopService;
        this.orderService = orderService;
        this.menuItemService = menuItemService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> getCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.create(customerDto));
    }

    @GetMapping("/shops")
    public ResponseEntity<List<ShopDto>> getShopList() {
        return ResponseEntity.ok(shopService.findAll());
    }

    @GetMapping("/shops/{id}/menu-items")
    public ResponseEntity<List<MenuItemDto>> getMenuListByShop(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getListItems(id));
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDto> placeOrder(
            @RequestBody @Valid OrderDto orderDto) {
        orderDto = orderService.placeOrderByCustomer(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @PutMapping("/queues/{orderId}")
    public ResponseEntity<OrderDto> existQueue(@PathVariable Long orderId) {
        final OrderDto orderDto = orderService.existQueue(orderId);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }
}
