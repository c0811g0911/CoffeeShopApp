package digital101.io.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDto {

    private Long id;

    @NotNull
    private Integer quantity;

    @NotNull
    @Size(max = 255)
    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long menuItemId;

    private Long customerId;
}
