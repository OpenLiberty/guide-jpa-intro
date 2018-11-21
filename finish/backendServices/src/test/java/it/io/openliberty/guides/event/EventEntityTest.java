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
import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import io.openliberty.guides.event.models.Event;

public class EventEntityTest extends EventTest {

    public static final String JSONFIELD_LOCATION = "location";
    public static final String JSONFIELD_NAME = "name";
    public static final String JSONFIELD_TIME = "time";
    public static final String JSONFIELD_ID = "id";
    public static final String EVENT_TIME = "12:00 PM, January 1 2018";
    public static final String EVENT_LOCATION = "IBM";
    public static final String EVENT_NAME = "JPA Guide";
    public static final String UPDATE_EVENT_TIME = "12:00 PM, February 1 2018";
    public static final String UPDATE_EVENT_LOCATION = "IBM Updated";
    public static final String UPDATE_EVENT_NAME = "JPA Guide Updated";

    @BeforeClass
    public static void oneTimeSetup() {
        port = System.getProperty("backend.http.port");
        baseUrl = "http://localhost:" + port + "/";
    }

    @Before
    public void setup() {
        form = new Form();
        client = ClientBuilder.newClient();
        client.register(JsrJsonpProvider.class);

        eventForm = new HashMap<String, String>();
        actualDataStored = new HashMap<String, String>();

        eventForm.put(JSONFIELD_NAME, EVENT_NAME);
        eventForm.put(JSONFIELD_LOCATION, EVENT_LOCATION);
        eventForm.put(JSONFIELD_TIME, EVENT_TIME);

        e = new Event(EVENT_NAME, EVENT_LOCATION, EVENT_TIME);
    }

    @Test
    public void testInvalidRead() {
        assertEquals("Reading an event that does not exist should return an empty list",
            getIndividualEvent(-1).isEmpty(), true);
    }

    @Test
    public void testInvalidDelete() {
        int responseStatus = deleteRequest(-1);
        assertEquals("Attempting to delete an event that does not exist should return "
            + "the HTTP response code " + Status.NOT_FOUND.getStatusCode(), 
            responseStatus, Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testInvalidUpdate() {
        int updateResponseStatus = updateRequest(eventForm, -1);
        assertEquals("Attempting to update an event that does not exist should return "
            + "the HTTP response code " + Status.NOT_FOUND.getStatusCode(), 
            updateResponseStatus, Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testReadIndividualEvent() {
        int postResponseStatus = postRequest(eventForm);
        assertEquals("Creating an event should return the HTTP reponse code " + 
            Status.NO_CONTENT.getStatusCode(), postResponseStatus, 
            Status.NO_CONTENT.getStatusCode());
        JsonObject event = getTestEvent();
        event = getIndividualEvent(event.getInt("id"));

        actualDataStored.put(event.getString(JSONFIELD_NAME), EVENT_NAME);
        actualDataStored.put(event.getString(JSONFIELD_LOCATION), EVENT_LOCATION);
        actualDataStored.put(event.getString(JSONFIELD_TIME), EVENT_TIME);
        assertData(actualDataStored);

        int deleteResponseStatus = deleteRequest(event.getInt("id"));
        assertEquals("Deleting an event should return the HTTP response code " + 
            Status.NO_CONTENT.getStatusCode(), deleteResponseStatus, 
            Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testCRUD() {
        int eventCount = getRequest().size();
        int postResponseStatus = postRequest(eventForm);
        assertEquals("Creating an event should return the HTTP reponse code " + 
            Status.NO_CONTENT.getStatusCode(), postResponseStatus, 
            Status.NO_CONTENT.getStatusCode());
     
        JsonObject event = getTestEvent();
        actualDataStored.put(event.getString(JSONFIELD_NAME), EVENT_NAME);
        actualDataStored.put(event.getString(JSONFIELD_LOCATION), EVENT_LOCATION);
        actualDataStored.put(event.getString(JSONFIELD_TIME), EVENT_TIME);
        assertData(actualDataStored);

        eventForm.put(JSONFIELD_NAME, UPDATE_EVENT_NAME);
        eventForm.put(JSONFIELD_LOCATION, UPDATE_EVENT_LOCATION);
        eventForm.put(JSONFIELD_TIME, UPDATE_EVENT_TIME);
        int updateResponseStatus = updateRequest(eventForm, event.getInt("id"));
        assertEquals("Updating an event should return the HTTP response code " + 
            Status.NO_CONTENT.getStatusCode(), updateResponseStatus, 
            Status.NO_CONTENT.getStatusCode());
        
        e = new Event(UPDATE_EVENT_NAME, UPDATE_EVENT_LOCATION, UPDATE_EVENT_TIME);
        event = getTestEvent();
        actualDataStored.put(event.getString(JSONFIELD_NAME), UPDATE_EVENT_NAME);
        actualDataStored.put(event.getString(JSONFIELD_LOCATION),
                UPDATE_EVENT_LOCATION);
        actualDataStored.put(event.getString(JSONFIELD_TIME), UPDATE_EVENT_TIME);
        assertData(actualDataStored);

        int deleteResponseStatus = deleteRequest(event.getInt("id"));
        assertEquals("Deleting an event should return the HTTP response code " + 
            Status.NO_CONTENT.getStatusCode(), deleteResponseStatus, 
            Status.NO_CONTENT.getStatusCode());
        assertEquals("Total number of events stored should be the same after testing "
            + "CRUD operations.", getRequest().size(), eventCount);
    }

    @After
    public void teardown() {
        response.close();
        client.close();
    }

}
