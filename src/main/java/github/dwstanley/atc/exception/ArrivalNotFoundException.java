package github.dwstanley.atc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArrivalNotFoundException extends RuntimeException {
    public ArrivalNotFoundException(Long arrivalId) {
        super("could not find user '" + arrivalId + "'.");
    }
}