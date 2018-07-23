//package github.dwstanley.atc.service;
//
//import github.dwstanley.atc.model.Aircraft;
//import github.dwstanley.atc.model.Departure;
//import github.dwstanley.atc.repository.AircraftRepository;
//import github.dwstanley.atc.repository.ArrivalRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.PriorityBlockingQueue;
//
//@Service
//public class BasicAirportService implements AirportService {
//
//    private final AircraftQueue arrivals = new AircraftQueue();
//
//    private final PriorityBlockingQueue<Departure> departures = new PriorityBlockingQueue<>();
//
//    @Autowired
//    private AircraftRepository aircraftRepository;
//
//    @Autowired
//    private ArrivalRepository arrivalRepository;
//
//    @Override
//    public void requestToLand(Aircraft aircraft) {
//        // todo - verify aircraft is not already landed or departing
//        arrivals.add(aircraft);
//    }
//
//    // public Arrival requestToLand(Aircraft aircraft) {
////        Arrival arrival = arrivalRepository.save(new Arrival(null, System.currentTimeMillis(), aircraft));
////        arrivals.add(new FifoAircraft(aircraft));
////        return arrival;
////    }
//
//    @Override
//    public Departure requestToDepart(Aircraft aircraft) {
//        // todo - verify aircraft is already landed
////        departures.add()
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    @Override
//    public List<Aircraft> pendingArrivals() {
//        return arrivals.pending();
//    }
//
//    @Override
//    public List<Departure> pendingDepartures() {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    @Override
//    public Optional<Aircraft> nextArrival() {
//        return arrivals.peek();
//    }
//
//    @Override
//    public Optional<Aircraft> completeArrival() {
//        Optional<Aircraft> aircraftOptional = arrivals.poll();
//        aircraftOptional.ifPresent(aircraft -> aircraftRepository.save(aircraft));
//        return aircraftOptional;
//    }
//
////    @Override
////    public Optional<Aircraft> completeDeparture() {
////        Optional<Aircraft> aircraftOptional = departures.poll();
////        aircraftOptional.ifPresent(aircraft -> aircraftRepository.delete(aircraft));
////        return aircraftOptional;
////    }
//
//    @Override
//    public Departure nextDeparture() {
//        return departures.peek();
//    }
//
////    List<Aircraft> listOnGroundAircraft();
//
//    @Override
//    public int numOnGroundAircraft() {
//        return aircraftRepository.findAll().size();
//    }
//
//    @Override
//    public int numInAirAircraft() {
//        return arrivals.size();
//    }
//
//}
