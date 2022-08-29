package antifraud.domain.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserNotSaveExceptions extends RuntimeException {

    public UserNotSaveExceptions(String message) {
        super(message);
    }
}