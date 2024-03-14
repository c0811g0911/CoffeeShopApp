package digital101.io.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShopServiceTimeException extends RuntimeException {

    public ShopServiceTimeException() {
        super();
    }

    public ShopServiceTimeException(final String message) {
        super(message);
    }

}
