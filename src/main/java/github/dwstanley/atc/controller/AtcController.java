package github.dwstanley.atc.controller;

import github.dwstanley.atc.EventHandler;
import github.dwstanley.atc.exception.ArrivalNotFoundException;
import github.dwstanley.atc.exception.DepartureNotFoundException;
import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Arrival;
import github.dwstanley.atc.model.Departure;
import github.dwstanley.atc.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/atc")
public class AtcController {

    private final AirportService airportService;
    private final EventHandler eventHandler;

    @Autowired
    AtcController(AirportService airportService, EventHandler eventHandler) {
        this.airportService = airportService;
        this.eventHandler = eventHandler;
    }

    @GetMapping(value = "/completeArrival")
    Aircraft completeArrival(@RequestParam("aircraftVin") String aircraftVin) {
        Aircraft aircraft = this.airportService
                .arrive(aircraftVin)
                .orElseThrow(() -> new ArrivalNotFoundException(aircraftVin));
        eventHandler.updateAircraft(aircraft);
        return aircraft;
    }

    @GetMapping(value = "/requestArrival")
    Arrival requestArrival(@RequestParam("aircraftVin") String aircraftVin) {
        Arrival arrival = this.airportService.requestToLand(aircraftVin);
        eventHandler.updateAircraft(arrival.getAircraft());
        return arrival;
    }

    @GetMapping(value = "/requestDeparture")
    Departure requestDeparture(@RequestParam("aircraftVin") String aircraftVin) {
        Departure departure = this.airportService.requestToDepart(aircraftVin);
        eventHandler.updateAircraft(departure.getAircraft());
        return departure;
    }

    @GetMapping(value = "/departNext")
    Aircraft departNext() {
        Aircraft aircraft = this.airportService
                .departNext()
                .orElseThrow(DepartureNotFoundException::new);
        eventHandler.updateAircraft(aircraft);
        return aircraft;
    }

    @GetMapping(value = "/departures")
    List<Departure> departures() {
        return this.airportService.pendingDepartures();
    }

}