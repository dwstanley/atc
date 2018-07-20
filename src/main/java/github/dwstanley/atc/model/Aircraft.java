package github.dwstanley.atc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aircraft {
    private Long id;
    private String name;
    private AcType type;
    private AcSize size;
}
