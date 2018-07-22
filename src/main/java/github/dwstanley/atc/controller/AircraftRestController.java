//package github.dwstanley.atc.controller;
//
//import github.dwstanley.atc.model.Aircraft;
//import github.dwstanley.atc.repository.AircraftRepository;
//import github.dwstanley.atc.service.AirportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/atc")
//public class AircraftRestController {
//
//    private final AircraftRepository aircraftRepository;
//    private final AirportService airportService;
//
//    @Autowired
//    AircraftRestController(AircraftRepository aircraftRepository, AirportService airportService) {
//        this.aircraftRepository = aircraftRepository;
//        this.airportService = airportService;
//    }
//
//    @GetMapping(value = "/aircraft/{id}")
//    Aircraft findAircraft(@PathVariable Long id) {
//        return this.aircraftRepository
//                .findById(id).orElseThrow(() -> new RuntimeException("Requested aircraft is unknown or has not landed yet."));
////                .orElseThrow(() -> new ArrivalNotFoundException(arrivalId));
//    }
//
//    @PostMapping(path = "/aircraft")
//    void add(@RequestBody Aircraft aircraft) {
//        this.aircraftRepository.save(aircraft);
//    }
//
//    @GetMapping(value = "/aircraft")
//    List<Aircraft> findAll() {
//        return this.aircraftRepository.findAll();
//    }
//
//    @PostMapping(path = "/arrivals")
//    void requestArrival(@RequestBody Aircraft aircraft) {
//        this.airportService.requestToLand(aircraft);
//    }
//
//    @GetMapping(value = "/arrivals")
//    List<Aircraft> pendingArrivals() {
//        return airportService.pendingArrivals();
//    }
//
//    @GetMapping(value = "/arrivals/completeNext")
//    Aircraft completeNext() {
//        return this.airportService
//                .completeArrival()
//                .orElseThrow(() -> new RuntimeException("No pending arrival to complete."));
//    }
//
//
//    // /status/{aircraftSerialNumber}
//    // unknown, landed, arriving, departing
//    // aircraft cannot be in both the arriving and departing queues
//
//}
