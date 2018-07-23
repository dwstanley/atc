package github.dwstanley.atc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DepartureNotFoundException extends RuntimeException {
    public DepartureNotFoundException() {
        super("No pending departure found to complete.");
    }
}