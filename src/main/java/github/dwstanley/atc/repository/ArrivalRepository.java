package github.dwstanley.atc.repository;

import github.dwstanley.atc.model.Arrival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArrivalRepository extends JpaRepository<Arrival, Long> {
}
