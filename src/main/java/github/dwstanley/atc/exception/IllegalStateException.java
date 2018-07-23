package github.dwstanley.atc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IllegalStateException extends RuntimeException {
    public IllegalStateException(Object allowed, Object actual) {
        super("Cannot complete transaction, plane was expected to be in one of the following states "
                + allowed + " but was: " + actual);
    }
}