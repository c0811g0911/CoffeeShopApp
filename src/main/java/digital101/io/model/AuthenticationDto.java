package digital101.io.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AuthenticationDto {

    private ShopDto info;
    private String refreshToken;
    private String accessToken;

}
