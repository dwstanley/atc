//package github.dwstanley.atc.service;
//
//import github.dwstanley.atc.model.Aircraft;
//import github.dwstanley.atc.model.Arrival;
//import github.dwstanley.atc.model.Departure;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface AirportServiceCopy {
//
//    Arrival requestToLand(String aircraftVin);
//    void requestToLand(Aircraft aircraft);
//
//    Departure requestToDepart(Aircraft aircraft);
//
//    List<Arrival> pendingArrivals();
//    List<Aircraft> pendingArrivals();
//
//    List<Departure> pendingDepartures();
//
//    Optional<Aircraft> nextArrival();
//
//    Departure nextDeparture();
//
////    List<Aircraft> listOnGroundAircraft();
//
//    int numOnGroundAircraft();
//
//    int numInAirAircraft();
//
//    Optional<Aircraft> completeArrival();
//
//}
