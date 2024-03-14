package digital101.io.controller.rest;

import digital101.io.model.CustomerDto;
import digital101.io.model.MenuDto;
import digital101.io.service.CustomerService;
import digital101.io.service.MenuService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/admin/menus", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {

    private final MenuService menuService;

    public AdminMenuController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuDto> createNewMenu(@RequestBody MenuDto menuDto) {
        menuService.create(menuDto);
        return ResponseEntity.ok(menuDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDto> updateMenu(@RequestBody MenuDto menuDto, @PathVariable Long id) {
        menuService.update(id, menuDto);
        return ResponseEntity.ok(menuDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> getMenuById(@PathVariable Long id) {
        MenuDto menuDto = menuService.get(id);
        return ResponseEntity.ok(menuDto);
    }

    @GetMapping
    public ResponseEntity<List<MenuDto>> getAll() {
        List<MenuDto> all = menuService.findAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.ok(null);
    }
}
