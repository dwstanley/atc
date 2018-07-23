package github.dwstanley.atc.controller;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Arrival;
import github.dwstanley.atc.model.Departure;
import github.dwstanley.atc.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                .completeArrival(aircraftVin)
                .orElseThrow(() -> new RuntimeException("Could not complete requested arrival."));
    }

//    @PostMapping(value = "/completeArrival")
//    Aircraft completeArrival(@RequestBody Arrival arrival) {
//        return this.airportService
//                .completeArrival(arrival.getAircraft().getVin())
//                .orElseThrow(() -> new RuntimeException("Could not complete requested arrival."));
//    }

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
                .orElseThrow(() -> new RuntimeException("No pending departures to complete."));
    }

    @GetMapping(value = "/departures")
    List<Departure> departures() {
        return this.airportService.pendingDepartures();
    }

}