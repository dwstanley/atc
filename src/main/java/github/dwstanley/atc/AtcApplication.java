package github.dwstanley.atc;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.repository.AircraftRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static github.dwstanley.atc.model.AcSize.SMALL;
import static github.dwstanley.atc.model.AcType.PASSENGER;

@SpringBootApplication
public class AtcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcApplication.class, args);
    }

    @Bean
    CommandLineRunner init(AircraftRepository aircraftRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Aircraft aircraft = aircraftRepository.save(new Aircraft(null, "TestAircraft", PASSENGER, SMALL));
            }
        };
    }

}
