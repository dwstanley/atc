package github.dwstanley.atc.repository;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Arrival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;

@RepositoryRestResource(excerptProjection = ArrivalRepository.ArrivalProjection.class)
public interface ArrivalRepository extends JpaRepository<Arrival, Long> {

    @Projection(name = "inlineAircraft", types = {Arrival.class})
    interface ArrivalProjection {
        Long getTimestamp();

        Aircraft getAircraft();
    }

    Arrival findByAircraftVin(String vin);

}