package digital101.io.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QueueDto {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Integer maxQueueSize;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
