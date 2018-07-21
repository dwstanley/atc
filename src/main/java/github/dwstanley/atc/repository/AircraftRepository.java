package github.dwstanley.atc.repository;

import github.dwstanley.atc.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
