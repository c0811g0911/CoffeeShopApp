package digital101.io.service;


import digital101.io.domain.Queue;
import digital101.io.domain.Shop;
import digital101.io.model.QueueDto;
import digital101.io.repos.QueueRepository;
import digital101.io.repos.ShopRepository;
import digital101.io.util.NotFoundException;
import digital101.io.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QueueService {

    private final QueueRepository queuesRepository;
    private final ShopRepository shopRepository;

    public QueueService(final QueueRepository queuesRepository,
                        final ShopRepository shopRepository) {
        this.queuesRepository = queuesRepository;
        this.shopRepository = shopRepository;
    }

    public List<QueueDto> findAll() {
        final List<Queue> queueList = queuesRepository.findAll(Sort.by("id"));
        return queueList.stream()
                .map(queue -> mapToDTO(queue, new QueueDto()))
                .toList();
    }

    public QueueDto get(final Long id) {
        return queuesRepository.findById(id)
                .map(queues -> mapToDTO(queues, new QueueDto()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final QueueDto queueDto) {
        final Queue queue = new Queue();
        mapToEntity(queueDto, queue);
        return queuesRepository.save(queue).getId();
    }

    public void update(final Long id, final QueueDto queueDto) {
        final Queue queue = queuesRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(queueDto, queue);
        queuesRepository.save(queue);
    }

    public void delete(final Long id) {
        queuesRepository.deleteById(id);
    }

    private QueueDto mapToDTO(final Queue queue, final QueueDto queueDto) {
        queueDto.setId(queue.getId());
        queueDto.setName(queue.getName());
        queueDto.setMaxQueueSize(queue.getMaxQueueSize());
        queueDto.setCreatedAt(queue.getCreatedAt());
        queueDto.setUpdatedAt(queue.getUpdatedAt());
        return queueDto;
    }

    private Queue mapToEntity(final QueueDto queueDto, final Queue queues) {
        queues.setName(queueDto.getName());
        queues.setMaxQueueSize(queueDto.getMaxQueueSize());
        queues.setCreatedAt(queueDto.getCreatedAt());
        queues.setUpdatedAt(queueDto.getUpdatedAt());
        return queues;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Queue queues = queuesRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Shop queueShops = shopRepository.findFirstByQueue(queues);
        if (queueShops != null) {
            referencedWarning.setKey("queues.shops.queue.referenced");
            referencedWarning.addParam(queueShops.getId());
            return referencedWarning;
        }
        return null;
    }

}
