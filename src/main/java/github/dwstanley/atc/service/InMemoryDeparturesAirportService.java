package github.dwstanley.atc.service;

import github.dwstanley.atc.exception.AircraftNotFoundException;
import github.dwstanley.atc.exception.ArrivalNotFoundException;
import github.dwstanley.atc.model.AcStatus;
import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Arrival;
import github.dwstanley.atc.model.Departure;
import github.dwstanley.atc.repository.AircraftRepository;
import github.dwstanley.atc.repository.ArrivalRepository;
import github.dwstanley.atc.repository.DepartureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static github.dwstanley.atc.model.AcStatus.*;

@Service
public class InMemoryDeparturesAirportService implements AirportService {

    private final AircraftRepository aircraftRepository;

    private final ArrivalRepository arrivalRepository;

    private final DepartureRepository departureRepository;

    private final AircraftQueue<Departure> departures = new AircraftQueue<>(Departure::getTimestamp, Departure::getAircraft);

    @Autowired
    public InMemoryDeparturesAirportService(AircraftRepository aircraftRepository,
                                            ArrivalRepository arrivalRepository,
                                            DepartureRepository departureRepository) {
        this.aircraftRepository = aircraftRepository;
        this.arrivalRepository = arrivalRepository;
        this.departureRepository = departureRepository;
    }

    @Override
    public Arrival requestToLand(String aircraftVin) {

        Aircraft aircraft = getAircraftOrThrowException(aircraftVin);
        verifyStateOrThrowException(aircraft.getStatus(), UNKNOWN);

        aircraft.setStatus(ARRIVING);
        aircraftRepository.save(aircraft);

        return arrivalRepository.save(new Arrival(aircraft));
    }

    @Override
    public Departure requestToDepart(String aircraftVin) {

        Aircraft aircraft = getAircraftOrThrowException(aircraftVin);
        verifyStateOrThrowException(aircraft.getStatus(), LANDED, UNKNOWN);

        aircraft.setStatus(AcStatus.DEPARTING);
        aircraftRepository.save(aircraft);

        Departure departure = departureRepository.save(new Departure(aircraft));
        departures.add(departure);

        return departure;
    }

    @Override
    public List<Arrival> pendingArrivals() {
        return arrivalRepository.findAll();
    }

    @Override
    public List<Departure> pendingDepartures() {
        return departures.pending();
    }

    @Override
    public Optional<Aircraft> arrive(String aircraftVin) {

        Aircraft aircraft = getAircraftOrThrowException(aircraftVin);
        verifyStateOrThrowException(aircraft.getStatus(), ARRIVING);
        Arrival arrival = getArrivalOrThrowException(aircraftVin);

        aircraft.setStatus(LANDED);
        aircraftRepository.save(aircraft);
        arrivalRepository.delete(arrival);

        return Optional.ofNullable(aircraft);
    }

    @Override
    public Optional<Aircraft> departNext() {
        Optional<Aircraft> aircraftOptional = departures.poll().map(Departure::getAircraft);
        aircraftOptional.ifPresent(aircraft -> {
            aircraft.setStatus(AcStatus.UNKNOWN);
            aircraftRepository.save(aircraft);
        });
        return aircraftOptional;
    }

    private Aircraft getAircraftOrThrowException(String aircraftVin) {
        Aircraft aircraft = aircraftRepository.findByVin(aircraftVin);
        if (null == aircraft) {
            throw new AircraftNotFoundException(aircraftVin);
        }
        return aircraft;
    }

    private Arrival getArrivalOrThrowException(String aircraftVin) {
        Arrival arrival = arrivalRepository.findByAircraftVin(aircraftVin);
        if (null == arrival) {
            throw new ArrivalNotFoundException(aircraftVin);
        }
        return arrival;
    }

    private void verifyStateOrThrowException(AcStatus actual, AcStatus... allowed) {
        if (null == actual) {
            actual = UNKNOWN;
        }
        if (!Arrays.asList(allowed).contains(actual)) {
            throw new IllegalArgumentException(
                    "Cannot complete transaction, plane was expected to be in one of the following states "
                            + allowed + " but was: " + actual);
        }
    }

}
