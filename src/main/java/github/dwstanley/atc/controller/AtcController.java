package github.dwstanley.atc.controller;

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

    @Autowired
    AtcController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping(value = "/completeArrival")
    Aircraft completeArrival(@RequestParam("aircraftVin") String aircraftVin) {
        return this.airportService
                .arrive(aircraftVin)
                .orElseThrow(() -> new ArrivalNotFoundException(aircraftVin));
    }

    @GetMapping(value = "/requestArrival")
    Arrival requestArrival(@RequestParam("aircraftVin") String aircraftVin) {
        return this.airportService.requestToLand(aircraftVin);
    }

    @GetMapping(value = "/requestDeparture")
    Departure requestDeparture(@RequestParam("aircraftVin") String aircraftVin) {
        return this.airportService.requestToDepart(aircraftVin);
    }

    @GetMapping(value = "/departNext")
    Aircraft departNext() {
        return this.airportService
                .departNext()
                .orElseThrow(DepartureNotFoundException::new);
    }

    @GetMapping(value = "/departures")
    List<Departure> departures() {
        return this.airportService.pendingDepartures();
    }

}