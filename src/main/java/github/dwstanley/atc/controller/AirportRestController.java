//package github.dwstanley.atc.controller;
//
//
//import github.dwstanley.atc.model.Aircraft;
//import github.dwstanley.atc.model.Arrival;
//import github.dwstanley.atc.service.AirportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/atc")
//public class AirportRestController {
//
//    @Autowired
//    private AircraftRepository aircraftRepository;
//
//    @Autowired
//    private AirportService airportService;
//
//    @RequestMapping(value = "/aircraft", method = RequestMethod.GET)
//    @ResponseBody
//    public List<Aircraft> findAllGroundedAircraft() {
//        List<Aircraft> aircraftList = aircraftRepository.findAll();
//        return aircraftList;
//    }
//
//    @RequestMapping(value = "/requestToLand", method = RequestMethod.GET)
//    @ResponseBody
//    public Arrival requestToLand(@RequestParam(name="aircraftName") String aircraftName) {
//        return airportService.requestToLand();
//    }
//
//    @PostMapping
//    ResponseEntity<?> add(@RequestBody Bookmark input) {
//        this.validateUser(userId);
//
//        return this.accountRepository
//                .findByUsername(userId)
//                .map(account -> {
//                    Bookmark result = this.bookmarkRepository.save(new Bookmark(account,
//                            input.getUri(), input.getDescription()));
//
//                    URI location = ServletUriComponentsBuilder
//                            .fromCurrentRequest()
//                            .path("/{id}")
//                            .buildAndExpand(result.getId())
//                            .toUri();
//
//                    return ResponseEntity.created(location).build();
//                })
//                .orElse(ResponseEntity.noContent().build());
//    }
//
//    }
