package github.dwstanley.atc;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.repository.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static github.dwstanley.atc.model.AcSize.LARGE;
import static github.dwstanley.atc.model.AcSize.SMALL;
import static github.dwstanley.atc.model.AcType.EMERGENCY;
import static github.dwstanley.atc.model.AcType.PASSENGER;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final AircraftRepository aircraftRepository;

    @Autowired
    public DatabaseLoader(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        aircraftRepository.save(new Aircraft("Aircraft 1", PASSENGER, SMALL));
        aircraftRepository.save(new Aircraft("Aircraft 2", PASSENGER, LARGE));
        aircraftRepository.save(new Aircraft("Aircraft 3", EMERGENCY, SMALL));
    }
}