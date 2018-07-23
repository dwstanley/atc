package github.dwstanley.atc.repository;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Departure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;

@RepositoryRestResource(excerptProjection = DepartureRepository.DepartureProjection.class)
public interface DepartureRepository extends JpaRepository<Departure, Long> {

    @Projection(name = "inlineAircraft", types = {Departure.class})
    interface DepartureProjection {
        Long getTimestamp();

        Aircraft getAircraft();
    }

}