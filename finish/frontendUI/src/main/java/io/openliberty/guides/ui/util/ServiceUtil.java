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
package io.openliberty.guides.ui.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import io.openliberty.guides.ui.Event;

public class ServiceUtil {

    // Back end service URLs
    private static String port = System.getProperty("backend.port");
    private static String eventServiceURL = "http://localhost:" + port + "/events";

    /**
     * Post event form data to back end service
     */
    public static void submitEventToService(String name, String location, String time) {
        Form form = new Form().param("name", name).param("time", time).param("location",
            location);
        Response response = connectToService(eventServiceURL).post(Entity.form(form));
        response.close();
    }

    /**
     * Post updated event form data to back end service
     */
    public static void submitUpdatedEventToService(String name, String location,
            String time, int id) {
        Form form = new Form().param("name", name).param("time", time).param("location",
            location);
        Entity<Form> entityForm = Entity.form(form);
        Response response = connectToService(eventServiceURL + "/" + id)
            .put(entityForm);
        response.close();
    }

    /**
     * Delete event from back end storage
     */
    public static void deleteEventService(int id) {
        Response response = connectToService(eventServiceURL + "/" + id).delete();
        response.close();
    }

    /**
     * Retrieve list of events from back end storage.
     */
    public static List<Event> retrieveEvents() {
        JsonArray jr = retrieveFromService();
        List<Event> events = jr.stream().map(eventJson -> {
            Event event = new Event(((JsonObject) eventJson).getString("name"),
                ((JsonObject) eventJson).getString("location"),
                ((JsonObject) eventJson).getString("time"));
            event.setId(((JsonObject) eventJson).getInt("id"));
            return event;
        }).collect(Collectors.toList());

        return events;
    }

    /**
     * Retrieve an event by its id.
     */
    public static JsonObject retrieveEventById(int eventId) {
        Response response = connectToService(eventServiceURL + "/" + eventId).get();
        JsonObject jobj = response.readEntity(JsonObject.class);
        response.close();
        return jobj;
    }

    /**
     * Helper method to create a connection to the back end service via the URL.
     */
    private static Builder connectToService(String URL) {
        Client client = ClientBuilder.newClient();
        Builder builder = client.target(URL).request();
        client.close();
        return builder;
    }

    /**
     * Helper method to retrieve the representation of events in Json Array
     * format from the back end service.
     */
    private static JsonArray retrieveFromService() {
        Response response = connectToService(eventServiceURL).get();
        JsonArray jr = response.readEntity(JsonArray.class);
        response.close();
        return jr;
    }
}