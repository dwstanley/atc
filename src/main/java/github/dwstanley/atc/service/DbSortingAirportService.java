package github.dwstanley.atc.service;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Arrival;
import github.dwstanley.atc.model.Departure;
import github.dwstanley.atc.repository.AircraftRepository;
import github.dwstanley.atc.repository.ArrivalRepository;
import github.dwstanley.atc.repository.DepartureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Function;

@Service
public class DbSortingAirportService implements AirportService {

    private final PriorityBlockingQueue<Arrival> arrivals = new PriorityBlockingQueue<>(100, Comparator.comparingLong(Arrival::getTimestamp));
//    private final PriorityBlockingQueue<Departure> departures = new PriorityBlockingQueue<>();
    private final AircraftQueue<Departure> departures = new AircraftQueue<>(Departure::getTimestamp, Departure::getAircraft);

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private ArrivalRepository arrivalRepository;

    @Autowired
    private DepartureRepository departureRepository;

    @Override
    public Arrival requestToLand(String aircraftVin) {
        // todo throw exception if not found
        // todo throw exception if in status that does not support landing
        Aircraft aircraft = aircraftRepository.findByVin(aircraftVin);
        aircraft.setStatus("Arriving");
        aircraftRepository.save(aircraft);

        Arrival arrival = arrivalRepository.save(new Arrival(aircraft));
        arrivals.add(arrival);

        return arrival;
    }

    @Override
    public Departure requestToDepart(String aircraftVin) {
        // todo throw exception if not found
        // todo throw exception if in status that does not support landing
        Aircraft aircraft = aircraftRepository.findByVin(aircraftVin);
        aircraft.setStatus("Departing");
        aircraftRepository.save(aircraft);

        Departure departure = departureRepository.save(new Departure(aircraft));
        departures.add(departure);

        return departure;
    }

    @Override
    public List<Arrival> pendingArrivals() {
        return new ArrayList<>(arrivals);
    }

    @Override
    public List<Departure> pendingDepartures() {
        return departures.pending();
    }

    @Override
    public Optional<Arrival> nextArrival() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Optional<Aircraft> completeArrival(String aircraftVin) {
        // todo throw exception if not found
        // todo throw exception if in status that does not support landing
        Aircraft aircraft = aircraftRepository.findByVin(aircraftVin);
        aircraft.setStatus("Landed");
        aircraftRepository.save(aircraft);

        Arrival arrival = arrivalRepository.findByAircraftVin(aircraftVin);
        arrivalRepository.delete(arrival);

        return Optional.ofNullable(aircraft);
    }

    @Override
    public Optional<Aircraft> departNext() {
        Optional<Aircraft> aircraftOptional = departures.poll().map(Departure::getAircraft);
        aircraftOptional.ifPresent(aircraft -> {
            aircraft.setStatus("UNKNOWN");
            aircraftRepository.save(aircraft);
        });
        return aircraftOptional;
    }

    @Override
    public Departure nextDeparture() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    List<Aircraft> listOnGroundAircraft();

    @Override
    public int numOnGroundAircraft() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int numInAirAircraft() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
