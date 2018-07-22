package github.dwstanley.atc.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Aircraft {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @Enumerated(EnumType.STRING)
    private AcType type;

    @NonNull
    @Enumerated(EnumType.STRING)
    private AcSize size;
//    private String lastKnownLocation;
}

