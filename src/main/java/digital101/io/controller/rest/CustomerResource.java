package digital101.io.controller.rest;

import digital101.io.model.CustomerDto;
import digital101.io.model.OrderDto;
import digital101.io.model.ShopDto;
import digital101.io.service.CustomerService;
import digital101.io.service.OrderService;
import digital101.io.service.ShopService;
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

    public CustomerResource(final CustomerService customerService, final ShopService shopService, final OrderService orderService) {
        this.customerService = customerService;
        this.shopService = shopService;
        this.orderService = orderService;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> getCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.create(customerDto));
    }

    @GetMapping("/shops")
    public ResponseEntity<List<ShopDto>> getShopList() {
        return ResponseEntity.ok(shopService.findAll());
    }

    @PutMapping("/orders")
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
