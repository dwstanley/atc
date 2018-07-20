package github.dwstanley.atc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrivalRequest {
    private Long id;
    private Long timestamp;
    private Aircraft aircraft;
}
