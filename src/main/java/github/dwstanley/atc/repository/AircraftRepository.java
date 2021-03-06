package github.dwstanley.atc.repository;

import github.dwstanley.atc.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    Aircraft findByVin(String vin);
}
