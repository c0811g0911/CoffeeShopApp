package digital101.io.controller.rest;

import digital101.io.model.AuthenticationDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> login() {
        return ResponseEntity.ok(AuthenticationDto.builder()
//                        .accessToken()
//                        .refreshToken()
//                        .info()
                .build());
    }
}
