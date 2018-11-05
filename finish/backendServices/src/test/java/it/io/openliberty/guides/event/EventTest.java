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
package it.io.openliberty.guides.event;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import io.openliberty.guides.models.Event;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventTest {

    private WebTarget webTarget;

    protected Form form;
    protected Client client;
    protected Response response;
    protected HashMap<String, String> eventForm;
    protected HashMap<String, String> actualDataStored;
    protected Event e;

    protected static String baseUrl;
    protected static String port;
    protected static final String EVENTS = "events";

    private static Logger LOGGER = Logger.getLogger(EventTest.class.getName());

    /**
     *  Makes a POST request to the /events endpoint
     */
    protected void postRequest(HashMap<String, String> formDataMap) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(baseUrl + EVENTS);
        response = webTarget.request().post(Entity.form(form));
        form = new Form();
    }

    /**
     *  Makes a PUT request to the /events/{eventId} endpoint
     */
    protected void updateRequest(HashMap<String, String> formDataMap, int eventId) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(baseUrl + EVENTS + "/" + eventId);
        response = webTarget.request().put(Entity.form(form));
        form = new Form();
    }
    
    /**
     *  Makes a DELETE request to /events/{eventId} endpoint 
     */
    protected void deleteRequest(int eventId) {
        webTarget = client.target(baseUrl + EVENTS + "/" + eventId);
        response = webTarget.request().delete();
    }
    
    /**
     *  Makes a GET request to the /events endpoint and returns result in a JsonArray
     */
    protected JsonArray getRequest() {
        webTarget = client.target(baseUrl + EVENTS);
        response = webTarget.request().get();
        return response.readEntity(JsonArray.class);
    }

    /**
     *  Makes a GET request to the /events/{eventId} endpoint and returns a JsonObject
     */ 
    protected JsonObject getIndividualEvent(int eventId) {
        webTarget = client.target(baseUrl + EVENTS + "/" + eventId);
        response = webTarget.request().get();
        return response.readEntity(JsonObject.class);
    }
    
    /**
     *  Makes a GET request to the /events endpoint and returns the test event created
     *  in EventEntityTest.java
     */
    protected JsonObject getTestEvent() {
        LOGGER.log(Level.SEVERE, "HERE: " + e);
        JsonArray events = getRequest();
        for (int i = 0; i < events.size(); i++) {
            JsonObject testEvent = events.getJsonObject(i);
            Event test = new Event(testEvent.getString("name"),
                    testEvent.getString("location"), testEvent.getString("time"));
            if (test.equals(e)) {
                return testEvent;
            }
        }
        return null;
    }

    /**
     *  Asserts values in hashmap are equal
     */
    protected void assertData(HashMap<String, String> testedData) {
        testedData.forEach((actual, expected) -> {
            assertEquals(expected, actual);
        });
    }

}