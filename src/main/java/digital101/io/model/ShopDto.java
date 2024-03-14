package digital101.io.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ShopDto {

    private Long id;

    @Size(max = 255)
    private String name;

    private String description;

    private String address;

    @Size(max = 255)
    private String phone;

    @NotNull
    @Size(max = 255)
    @ShopsUserNameUnique
    private String userName;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ShopsMenuUnique
    private Long menu;

    @ShopsQueueUnique
    private Long queue;


    @NotNull
    private String openTime;

    @NotNull
    private String closedTime;

}
