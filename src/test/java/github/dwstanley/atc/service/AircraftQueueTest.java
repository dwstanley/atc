package github.dwstanley.atc.service;

import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Arrival;
import github.dwstanley.atc.model.Departure;
import org.junit.Before;
import org.junit.Test;

import static github.dwstanley.atc.model.AcSize.LARGE;
import static github.dwstanley.atc.model.AcSize.SMALL;
import static github.dwstanley.atc.model.AcType.*;
import static github.dwstanley.atc.model.AcType.CARGO;
import static org.junit.Assert.*;

public class AircraftQueueTest {

    private AircraftQueue<Departure> departureQueue;
    private AircraftQueue<Arrival> arrivalQueue;

    private Aircraft passengerSmall;
    private Aircraft passengerSmall2;
    private Aircraft passengerLarge;
    private Aircraft emergencySmall;
    private Aircraft emergencyLarge;
    private Aircraft vipSmall;
    private Aircraft vipLarge;
    private Aircraft cargoSmall;
    private Aircraft cargoLarge;

    @Before
    public void setUp() throws Exception {

        this.departureQueue =  new AircraftQueue<>(Departure::getTimestamp, Departure::getAircraft);
        this.arrivalQueue =  new AircraftQueue<>(Arrival::getTimestamp, Arrival::getAircraft);

        this.passengerSmall = new Aircraft("011", "Passenger Small", PASSENGER, SMALL);
        this.passengerSmall2 = new Aircraft("111", "Passenger Small", PASSENGER, SMALL);
        this.passengerLarge = new Aircraft("012", "Passenger Large", PASSENGER, LARGE);
        this.emergencySmall = new Aircraft("021", "Emergency Small", EMERGENCY, SMALL);
        this.emergencyLarge = new Aircraft("022", "Emergency Large", EMERGENCY, LARGE);
        this.vipSmall = new Aircraft("031", "VIP Small", VIP, SMALL);
        this.vipLarge = new Aircraft("032", "VIP Large", VIP, LARGE);
        this.cargoSmall = new Aircraft("041", "Cargo Small", CARGO, SMALL);
        this.cargoLarge = new Aircraft("042", "Cargo Large", CARGO, LARGE);

    }

    @Test
    public void whenAllAircraftAreAdded_thenCorrectOrder() {
        departureQueue.add(new Departure(cargoSmall));
        departureQueue.add(new Departure(cargoLarge));
        departureQueue.add(new Departure(passengerSmall));
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        departureQueue.add(new Departure(passengerSmall2));
        departureQueue.add(new Departure(passengerLarge));
        departureQueue.add(new Departure(vipSmall));
        departureQueue.add(new Departure(vipLarge));
        departureQueue.add(new Departure(emergencySmall));
        departureQueue.add(new Departure(emergencyLarge));

        assertEquals(9, departureQueue.size());
        assertEquals(9, departureQueue.pending().size());
        assertEquals(emergencyLarge, departureQueue.peek().get().getAircraft());

        assertEquals(emergencyLarge, departureQueue.poll().get().getAircraft());
        assertEquals(emergencySmall, departureQueue.poll().get().getAircraft());
        assertEquals(vipLarge, departureQueue.poll().get().getAircraft());
        assertEquals(vipSmall, departureQueue.poll().get().getAircraft());
        assertEquals(passengerLarge, departureQueue.poll().get().getAircraft());
        assertEquals(passengerSmall, departureQueue.poll().get().getAircraft());
        assertEquals(passengerSmall2, departureQueue.poll().get().getAircraft());
        assertEquals(cargoLarge, departureQueue.poll().get().getAircraft());
        assertEquals(cargoSmall, departureQueue.poll().get().getAircraft());
    }

    @Test
    public void whenAllAircraftAreAddedToArrivals_thenCorrectOrder() {
        arrivalQueue.add(new Arrival(cargoSmall));
        arrivalQueue.add(new Arrival(cargoLarge));
        arrivalQueue.add(new Arrival(passengerSmall));
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        arrivalQueue.add(new Arrival(passengerSmall2));
        arrivalQueue.add(new Arrival(passengerLarge));
        arrivalQueue.add(new Arrival(vipSmall));
        arrivalQueue.add(new Arrival(vipLarge));
        arrivalQueue.add(new Arrival(emergencySmall));
        arrivalQueue.add(new Arrival(emergencyLarge));

        assertEquals(9, arrivalQueue.size());
        assertEquals(9, arrivalQueue.pending().size());
        assertEquals(emergencyLarge, arrivalQueue.peek().get().getAircraft());

        assertEquals(emergencyLarge, arrivalQueue.poll().get().getAircraft());
        assertEquals(emergencySmall, arrivalQueue.poll().get().getAircraft());
        assertEquals(vipLarge, arrivalQueue.poll().get().getAircraft());
        assertEquals(vipSmall, arrivalQueue.poll().get().getAircraft());
        assertEquals(passengerLarge, arrivalQueue.poll().get().getAircraft());
        assertEquals(passengerSmall, arrivalQueue.poll().get().getAircraft());
        assertEquals(passengerSmall2, arrivalQueue.poll().get().getAircraft());
        assertEquals(cargoLarge, arrivalQueue.poll().get().getAircraft());
        assertEquals(cargoSmall, arrivalQueue.poll().get().getAircraft());
    }

}