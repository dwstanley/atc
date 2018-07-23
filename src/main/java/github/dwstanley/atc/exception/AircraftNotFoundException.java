package github.dwstanley.atc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AircraftNotFoundException extends RuntimeException {
    public AircraftNotFoundException(String aircraftVin) {
        super("could not find aircraft for vin: '" + aircraftVin + "'.");
    }
}