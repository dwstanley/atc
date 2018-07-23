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
    @Column(unique=true)
    private String vin;

    @NonNull
    private String name;

    @Enumerated(EnumType.STRING)
    private AcStatus status;

    @NonNull
    @Enumerated(EnumType.STRING)
    private AcType type;

    @NonNull
    @Enumerated(EnumType.STRING)
    private AcSize size;

}

