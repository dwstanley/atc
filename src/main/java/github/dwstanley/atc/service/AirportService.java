package github.dwstanley.atc.service;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Arrival;
import github.dwstanley.atc.model.Departure;

import java.util.List;
import java.util.Optional;

public interface AirportService {

    Arrival requestToLand(String aircraftVin);

    Departure requestToDepart(String aircraftVin);

    List<Arrival> pendingArrivals();

    List<Departure> pendingDepartures();

    Optional<Arrival> nextArrival();

    Departure nextDeparture();

//    List<Aircraft> listOnGroundAircraft();

    int numOnGroundAircraft();

    int numInAirAircraft();

    Optional<Aircraft> completeArrival(String aircraftVin);

    Optional<Aircraft> departNext();

}
