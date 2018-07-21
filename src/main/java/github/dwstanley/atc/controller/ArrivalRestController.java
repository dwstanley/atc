//package github.dwstanley.atc.controller;
//
//import github.dwstanley.atc.exception.ArrivalNotFoundException;
//import github.dwstanley.atc.model.Aircraft;
//import github.dwstanley.atc.model.Arrival;
//import github.dwstanley.atc.repository.ArrivalRepository;
//import github.dwstanley.atc.service.AirportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/atc")
//public class ArrivalRestController {
//
//    private final ArrivalRepository arrivalRepository;
//    private final AirportService airportService;
//
//    @Autowired
//    ArrivalRestController(ArrivalRepository arrivalRepository, AirportService airportService) {
//        this.arrivalRepository = arrivalRepository;
//        this.airportService = airportService;
//    }
//
//    @GetMapping(value = "/arrivals/{id}")
//    Arrival findArrival(@PathVariable Long id) {
//        return this.arrivalRepository
//                .findById(id)
//                .orElseThrow(() -> new ArrivalNotFoundException(id));
//    }
//
////    @PostMapping
////    ResponseEntity<?> add(@RequestBody Aircraft aircraft) {
////        airportService.requestToLand(aircraft);
////
////        return this.accountRepository
////                .findByUsername(userId)
////                .map(account -> {
////                    Bookmark result = this.bookmarkRepository.save(new Bookmark(account,
////                            input.getUri(), input.getDescription()));
////
////                    URI location = ServletUriComponentsBuilder
////                            .fromCurrentRequest()
////                            .path("/{id}")
////                            .buildAndExpand(result.getId())
////                            .toUri();
////
////                    return ResponseEntity.created(location).build();
////                })
////                .orElse(ResponseEntity.noContent().build());
////    }
//
//    @PostMapping(path = "/arrivals")
//    void add(@RequestBody Aircraft aircraft) {
//        airportService.requestToLand(aircraft);
//    }
//
////    @GetMapping("/arrived/{arrivalId}")
////    void arrived(@PathVariable Long arrivalId) {
////
////    }
//
//}
