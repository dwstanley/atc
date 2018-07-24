package github.dwstanley.atc;

import github.dwstanley.atc.model.Aircraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static github.dwstanley.atc.WebSocketConfiguration.MESSAGE_PREFIX;

@Component
@RepositoryEventHandler(Aircraft.class)
public class EventHandler {

    private final SimpMessagingTemplate websocket;

    private final EntityLinks entityLinks;

    @Autowired
    public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleAfterCreate
    public void newAircraft(Aircraft aircraft) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newAircraft", getPath(aircraft));
    }

    @HandleAfterDelete
    public void deleteAircraft(Aircraft aircraft) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteAircraft", getPath(aircraft));
    }

    @HandleAfterSave
    public void updateAircraft(Aircraft aircraft) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateAircraft", getPath(aircraft));
    }

    private String getPath(Aircraft aircraft) {
        return this.entityLinks.linkForSingleResource(aircraft.getClass(),
                aircraft.getId()).toUri().getPath();
    }

}