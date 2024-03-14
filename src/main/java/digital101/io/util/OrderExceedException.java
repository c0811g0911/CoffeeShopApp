package digital101.io.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderExceedException extends RuntimeException {

    public OrderExceedException() {
        super();
    }

    public OrderExceedException(final String message) {
        super(message);
    }

}
