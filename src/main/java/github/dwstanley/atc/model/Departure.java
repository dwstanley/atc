package github.dwstanley.atc.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Departure {
    @Id
    @GeneratedValue
    private Long id;

    private Long timestamp;

    @ManyToOne
    private Aircraft aircraft;

    public Departure(Aircraft aircraft) {
        this.aircraft = aircraft;
        this.timestamp = System.currentTimeMillis();
    }

}
