package github.dwstanley.atc.service;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.ArrivalRequest;
import github.dwstanley.atc.model.DepartureRequest;

import java.util.List;

public interface AirportService {

    ArrivalRequest requestToLand(Aircraft aircraft);

    DepartureRequest requestToDepart(Aircraft aircraft);

    List<ArrivalRequest> pendingArrivals();

    List<DepartureRequest> pendingDepartures();

    ArrivalRequest nextArrival();

    DepartureRequest nextDeparture();

//    List<Aircraft> listOnGroundAircraft();

    int numOnGroundAircraft();

    int numInAirAircraft();

}
