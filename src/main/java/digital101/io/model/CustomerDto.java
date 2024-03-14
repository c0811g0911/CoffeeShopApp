package digital101.io.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class CustomerDto {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String address;

    @NotNull
    @Size(max = 255)
    private String phone;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String userName;

    @NotNull
    @Size(max = 255)
    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
