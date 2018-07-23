package github.dwstanley.atc;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.repository.AircraftRepository;
import github.dwstanley.atc.repository.ArrivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static github.dwstanley.atc.model.AcSize.LARGE;
import static github.dwstanley.atc.model.AcSize.SMALL;
import static github.dwstanley.atc.model.AcType.*;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final AircraftRepository aircraftRepository;
    private final ArrivalRepository arrivalRepository;

    @Autowired
    public DatabaseLoader(AircraftRepository aircraftRepository, ArrivalRepository arrivalRepository) {
        this.aircraftRepository = aircraftRepository;
        this.arrivalRepository = arrivalRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        aircraftRepository.save(new Aircraft("011", "Passenger Small", PASSENGER, SMALL));
        aircraftRepository.save(new Aircraft("111", "Passenger Small", PASSENGER, SMALL));
        aircraftRepository.save(new Aircraft("012", "Passenger Large", PASSENGER, LARGE));

        aircraftRepository.save(new Aircraft("021", "Emergency Small", EMERGENCY, SMALL));
        aircraftRepository.save(new Aircraft("022", "Emergency Large", EMERGENCY, LARGE));

        aircraftRepository.save(new Aircraft("031", "VIP Small", VIP, SMALL));
        aircraftRepository.save(new Aircraft("032", "VIP Large", VIP, LARGE));

        aircraftRepository.save(new Aircraft("041", "Cargo Small", CARGO, SMALL));
        aircraftRepository.save(new Aircraft("042", "Cargo Large", CARGO, LARGE));

//        Aircraft aircraft = aircraftRepository.save(new Aircraft("0003", "Aircraft 3", EMERGENCY, SMALL));
//        arrivalRepository.save(new Arrival(aircraft));

    }
}