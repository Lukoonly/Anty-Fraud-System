package antifraud.domain.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictDataException extends RuntimeException {

    public ConflictDataException(String message) {
        super(message);
    }
}
