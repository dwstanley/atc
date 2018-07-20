package github.dwstanley.atc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartureRequest {
    private Long id;
    private Long timestamp;
    private Aircraft aircraft;
}
