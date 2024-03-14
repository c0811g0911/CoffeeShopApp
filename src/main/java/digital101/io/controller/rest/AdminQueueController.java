package digital101.io.controller.rest;

import digital101.io.model.CustomerDto;
import digital101.io.model.QueueDto;
import digital101.io.service.CustomerService;
import digital101.io.service.QueueService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/admin/queues", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminQueueController {

    private final QueueService queueService;

    public AdminQueueController(final QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping
    public ResponseEntity<QueueDto> create(@RequestBody QueueDto queueDto) {
        queueService.create(queueDto);
        return ResponseEntity.ok(queueDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QueueDto> update(@RequestBody QueueDto queueDto, @PathVariable Long id) {
        queueService.update(id, queueDto);
        return ResponseEntity.ok(queueDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QueueDto> getById(@PathVariable Long id) {
        QueueDto QueueDto = queueService.get(id);
        return ResponseEntity.ok(QueueDto);
    }

    @GetMapping
    public ResponseEntity<List<QueueDto>> getAll() {
        List<QueueDto> all = queueService.findAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        queueService.delete(id);
        return ResponseEntity.ok(null);
    }
}
