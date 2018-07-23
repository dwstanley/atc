package github.dwstanley.atc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArrivalNotFoundException extends RuntimeException {
    public ArrivalNotFoundException(String aircraftVin) {
        super("could not find arrival for aircraft vin: '" + aircraftVin + "'.");
    }
}