// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.resources;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import io.openliberty.guides.dao.EventDao;
import io.openliberty.guides.models.Event;

@RequestScoped
@Path("events")
public class EventResource {

    @Inject
    private EventDao eventDAO;

    /**
     * This method creates a new event from the submitted data (name, time and
     * location) by the user.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public void addNewEvent(@FormParam("name") String name,
        @FormParam("time") String time, @FormParam("location") String location) {
        Event newEvent = new Event(name, location, time);
        for (Event event : eventDAO.readAllEvents()) {
            if (event.equals(newEvent)) {
                return;
            }
        }

        eventDAO.createEvent(newEvent);
    }

    /**
     * This method creates a new event from the submitted data (name, time and
     * location) by the user.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public void updateEvent(@FormParam("name") String name,
        @FormParam("time") String time, @FormParam("location") String location,
        @PathParam("id") int id) {
        Event prevEvent = eventDAO.readEvent(id);

        for (Event event : eventDAO.readAllEvents()) {
            if (event.equals(new Event(name, location, time))) {
                return;
            }
        }

        prevEvent.setName(name);
        prevEvent.setLocation(location);
        prevEvent.setTime(time);

        eventDAO.updateEvent(prevEvent);
    }

    /**
     * This method deletes a specific existing/stored event
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteEvent(@PathParam("id") int id) {
        eventDAO.deleteEvent(eventDAO.readEvent(id));
    }

    /**
     * This method displays a specific existing/stored event in Json format
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonObject getEvent(@PathParam("id") int eventId) {

        JsonObjectBuilder builder = Json.createObjectBuilder();
        Event event = eventDAO.readEvent(eventId);
        builder.add("name", event.getName()).add("time", event.getTime())
               .add("location", event.getLocation()).add("id", event.getId());
        return builder.build();
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
        for (Event event : eventDAO.readAllEvents()) {
            builder.add("name", event.getName()).add("time", event.getTime())
                   .add("location", event.getLocation()).add("id", event.getId());
            finalArray.add(builder.build());
        }
        return finalArray.build();
    }
}
