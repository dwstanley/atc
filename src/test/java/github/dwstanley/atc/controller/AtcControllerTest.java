package github.dwstanley.atc.controller;

import github.dwstanley.atc.AtcApplication;
import github.dwstanley.atc.model.AcStatus;
import github.dwstanley.atc.model.Aircraft;
import github.dwstanley.atc.model.Arrival;
import github.dwstanley.atc.repository.AircraftRepository;
import github.dwstanley.atc.repository.ArrivalRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;

import static github.dwstanley.atc.model.AcSize.SMALL;
import static github.dwstanley.atc.model.AcType.PASSENGER;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtcApplication.class)
@WebAppConfiguration
public class AtcControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private ArrivalRepository arrivalRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.arrivalRepository.deleteAll();
        this.aircraftRepository.deleteAll();
        this.aircraftRepository.save(new Aircraft("011", "Passenger Small", PASSENGER, SMALL));
    }

    @Test
    public void whenCompleteArrivalWithInvalidVin_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/atc/completeArrival?aircraftVin=999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCompleteArrivalWithIncorrectStatus_thenReturnInternalError() throws Exception {

        Aircraft aircraft = aircraftRepository.findByVin("011");

        aircraft.setStatus(null);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/completeArrival?aircraftVin=011"))
                .andExpect(status().is5xxServerError());

        aircraft.setStatus(AcStatus.UNKNOWN);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/completeArrival?aircraftVin=011"))
                .andExpect(status().is5xxServerError());


        aircraft.setStatus(AcStatus.LANDED);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/completeArrival?aircraftVin=011"))
                .andExpect(status().is5xxServerError());

        aircraft.setStatus(AcStatus.DEPARTING);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/completeArrival?aircraftVin=011"))
                .andExpect(status().is5xxServerError());

    }

    @Test
    public void whenCompleteArrivalWithMissingArrival_thenReturnNotFound() throws Exception {

        Aircraft aircraft = aircraftRepository.findByVin("011");
        aircraft.setStatus(AcStatus.ARRIVING);
        aircraftRepository.save(aircraft);

        mockMvc.perform(get("/atc/completeArrival?aircraftVin=011"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void whenCompleteArrivalWithCorrectStatus_thenReturnLandedAircraft() throws Exception {

        Aircraft aircraft = aircraftRepository.findByVin("011");
        aircraft.setStatus(AcStatus.ARRIVING);
        aircraftRepository.save(aircraft);
        arrivalRepository.save(new Arrival(aircraft));

        mockMvc.perform(get("/atc/completeArrival?aircraftVin=011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print())
                .andExpect(jsonPath("$.vin", is(aircraft.getVin())))
                .andExpect(jsonPath("$.status", is("LANDED")));

    }

    @Test
    public void whenRequestArrivalWithInvalidVin_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/atc/requestArrival?aircraftVin=999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestArrivalWithIncorrectStatus_thenReturnInternalError() throws Exception {

        Aircraft aircraft = aircraftRepository.findByVin("011");

        aircraft.setStatus(AcStatus.ARRIVING);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/requestArrival?aircraftVin=011"))
                .andExpect(status().is5xxServerError());


        aircraft.setStatus(AcStatus.LANDED);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/requestArrival?aircraftVin=011"))
                .andExpect(status().is5xxServerError());

        aircraft.setStatus(AcStatus.DEPARTING);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/requestArrival?aircraftVin=011"))
                .andExpect(status().is5xxServerError());

    }

    @Test
    public void whenRequestArrivalWithCorrectStatus_thenReturnArrival() throws Exception {

        Aircraft aircraft = aircraftRepository.findByVin("011");

        aircraft.setStatus(AcStatus.UNKNOWN);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/requestArrival?aircraftVin=011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print())
                .andExpect(jsonPath("$.aircraft.vin", is(aircraft.getVin())))
                .andExpect(jsonPath("$.aircraft.status", is("ARRIVING")));

        assertNotNull(arrivalRepository.findByAircraftVin("011"));

    }

    @Test
    public void whenRequestArrivalWithNullStatus_thenReturnArrival() throws Exception {

        Aircraft aircraft = aircraftRepository.findByVin("011");

        aircraft.setStatus(null);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/requestArrival?aircraftVin=011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print())
                .andExpect(jsonPath("$.aircraft.vin", is(aircraft.getVin())))
                .andExpect(jsonPath("$.aircraft.status", is("ARRIVING")));

        assertNotNull(arrivalRepository.findByAircraftVin("011"));

    }

    @Test
    public void whenRequestDepartureWithIncorrectStatus_thenReturnInternalError() throws Exception {

        Aircraft aircraft = aircraftRepository.findByVin("011");

        aircraft.setStatus(AcStatus.ARRIVING);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/requestDeparture?aircraftVin=011"))
                .andExpect(status().is5xxServerError());

        aircraft.setStatus(AcStatus.DEPARTING);
        aircraftRepository.save(aircraft);
        mockMvc.perform(get("/atc/requestDeparture?aircraftVin=011"))
                .andExpect(status().is5xxServerError());

    }

    @Test
    public void whenDepartNextWithNothingPending_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/atc/departNext"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenRequestDepartureWithCorrectStatus_thenReturnDeparture() throws Exception {

        Aircraft aircraft = aircraftRepository.findByVin("011");

        aircraft.setStatus(AcStatus.LANDED);
        aircraftRepository.save(aircraft);

        // request departure
        mockMvc.perform(get("/atc/requestDeparture?aircraftVin=011"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print())
                .andExpect(jsonPath("$.aircraft.vin", is(aircraft.getVin())))
                .andExpect(jsonPath("$.aircraft.status", is("DEPARTING")));


        // remove departure
        mockMvc.perform(get("/atc/departNext"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print())
                .andExpect(jsonPath("$.vin", is(aircraft.getVin())))
                .andExpect(jsonPath("$.status", is("UNKNOWN")));

    }

}