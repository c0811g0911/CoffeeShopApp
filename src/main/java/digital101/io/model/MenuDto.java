package digital101.io.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MenuDto {

    private Long id;

    @Size(max = 255)
    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
