package io.openliberty.guides.eventapp.resources;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.openliberty.guides.eventapp.dao.EventDao;
import io.openliberty.guides.eventapp.models.Event;
import javax.ejb.EJB;

@RequestScoped
@Path("events")
public class EventService {

    @EJB private EventDao eventDAO;

    /**
     * This method creates a new event from the submitted data (name, time and
     * location) by the user.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public void addNewEvent(@FormParam("name") String name, @FormParam("time") String time, @FormParam("location") String location) {
        Event newEvent = new Event(name, location, time);
        this.eventDAO.createEvent(newEvent);        
    }

    /**
     * This method creates a new event from the submitted data (name, time and
     * location) by the user.
     */
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public void updateEvent(@FormParam("name") String name, @FormParam("time") String time, @FormParam("location") String location, @FormParam("id") String id) {
        Event prevEvent = this.eventDAO.readEvent(Integer.parseInt(id));
        prevEvent.setName(name);
        prevEvent.setLocation(location);
        prevEvent.setTime(time);
        this.eventDAO.updateEvent(prevEvent);        
    }

    /**
     * This method deletes a specific existing/stored event 
     */
    @DELETE
    @Path("/delete/{id}")
    @Transactional
    public void deleteEvent(@PathParam("id") int id) {
        this.eventDAO.deleteEvent(this.eventDAO.readEvent(id));
    }

    /**
     * This method displays a specific existing/stored event in Json format
     */
    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Event getEvent(@PathParam("id") int eventId) {
        return this.eventDAO.readEvent(eventId);
    }

    /**
     * This method displays the existing/stored events in Json format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getEvents() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();
        for (Event event : this.eventDAO.readAllEvents()) {
            builder.add("name", event.getName()).add("time", event.getTime()).add("location", event.getLocation()).add("id", event.getId());
            finalArray.add(builder.build());
        }
        return finalArray.build();
    }
}
