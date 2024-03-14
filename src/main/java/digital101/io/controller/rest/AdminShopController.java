package digital101.io.controller.rest;

import digital101.io.model.ShopDto;
import digital101.io.model.ShopDto;
import digital101.io.service.ShopService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/admin/shops", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminShopController {

    private final ShopService shopService;

    public AdminShopController(final ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping
    public ResponseEntity<ShopDto> create(@RequestBody ShopDto dto) {
        shopService.create(dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopDto> update(@RequestBody ShopDto dto, @PathVariable Long id) {
        shopService.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopDto> getById(@PathVariable Long id) {
        ShopDto dto = shopService.get(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ShopDto>> getAll() {
        List<ShopDto> all = shopService.findAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shopService.delete(id);
        return ResponseEntity.ok(null);
    }

}
