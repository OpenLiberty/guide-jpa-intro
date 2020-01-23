// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2019 IBM Corporation and others.
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

    private static final String JSONFIELD_LOCATION = "location";
    private static final String JSONFIELD_NAME = "name";
    private static final String JSONFIELD_TIME = "time";
    private static final String JSONFIELD_ID = "id";
    private static final String EVENT_TIME = "12:00 PM, January 1 2018";
    private static final String EVENT_LOCATION = "IBM";
    private static final String EVENT_NAME = "JPA Guide";
    private static final String UPDATE_EVENT_TIME = "12:00 PM, February 1 2018";
    private static final String UPDATE_EVENT_LOCATION = "IBM Updated";
    private static final String UPDATE_EVENT_NAME = "JPA Guide Updated";
    
    private static final int NO_CONTENT_CODE = Status.NO_CONTENT.getStatusCode();
    private static final int NOT_FOUND_CODE = Status.NOT_FOUND.getStatusCode();

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

        eventForm.put(JSONFIELD_NAME, EVENT_NAME);
        eventForm.put(JSONFIELD_LOCATION, EVENT_LOCATION);
        eventForm.put(JSONFIELD_TIME, EVENT_TIME);
    }

    @Test
    // tag::testInvalidRead[]
    public void testInvalidRead() {
        assertEquals("Reading an event that does not exist should return an empty list",
            true, getIndividualEvent(-1).isEmpty());
    }
    // end::testInvalidRead[]

    @Test
    // tag::testInvalidDelete[]
    public void testInvalidDelete() {
        int deleteResponse = deleteRequest(-1);
        assertEquals("Trying to delete an event that does not exist should return the "
            + "HTTP response code " + NOT_FOUND_CODE, NOT_FOUND_CODE, deleteResponse);
    }
    // end::testInvalidDelete[]

    @Test
    // tag::testInvalidUpdate[]
    public void testInvalidUpdate() {
        int updateResponse = updateRequest(eventForm, -1);
        assertEquals("Trying to update an event that does not exist should return the "
            + "HTTP response code " + NOT_FOUND_CODE, NOT_FOUND_CODE, updateResponse);
    }
    // end::testInvalidUpdate[]
    
    @Test
    // tag::testReadIndividualEvent[]
    public void testReadIndividualEvent() {
        int postResponse = postRequest(eventForm);
        assertEquals("Creating an event should return the HTTP reponse code " +  
            NO_CONTENT_CODE, NO_CONTENT_CODE, postResponse);

        Event e = new Event(EVENT_NAME, EVENT_LOCATION, EVENT_TIME);
        JsonObject event = findEvent(e);
        event = getIndividualEvent(event.getInt("id"));
        assertData(event, EVENT_NAME, EVENT_LOCATION, EVENT_TIME);

        int deleteResponse = deleteRequest(event.getInt("id"));
        assertEquals("Deleting an event should return the HTTP response code " + 
            NO_CONTENT_CODE, NO_CONTENT_CODE, deleteResponse);
    }
    // end::testReadIndividualEvent[]
    
    @Test
    // tag::testCURD[]
    public void testCRUD() {
        int eventCount = getRequest().size();
        int postResponse = postRequest(eventForm);
        assertEquals("Creating an event should return the HTTP reponse code " + 
            NO_CONTENT_CODE, NO_CONTENT_CODE, postResponse);
     
        Event e = new Event(EVENT_NAME, EVENT_LOCATION, EVENT_TIME);
        JsonObject event = findEvent(e);
        assertData(event, EVENT_NAME, EVENT_LOCATION, EVENT_TIME);

        eventForm.put(JSONFIELD_NAME, UPDATE_EVENT_NAME);
        eventForm.put(JSONFIELD_LOCATION, UPDATE_EVENT_LOCATION);
        eventForm.put(JSONFIELD_TIME, UPDATE_EVENT_TIME);
        int updateResponse = updateRequest(eventForm, event.getInt("id"));
        assertEquals("Updating an event should return the HTTP response code " + 
            NO_CONTENT_CODE, NO_CONTENT_CODE, updateResponse);
        
        e = new Event(UPDATE_EVENT_NAME, UPDATE_EVENT_LOCATION, UPDATE_EVENT_TIME);
        event = findEvent(e);
        assertData(event, UPDATE_EVENT_NAME, UPDATE_EVENT_LOCATION, UPDATE_EVENT_TIME);

        int deleteResponse = deleteRequest(event.getInt("id"));
        assertEquals("Deleting an event should return the HTTP response code " + 
            NO_CONTENT_CODE, NO_CONTENT_CODE, deleteResponse);
        assertEquals("Total number of events stored should be the same after testing "
            + "CRUD operations.", eventCount, getRequest().size());
    }
    // end::testCURD[]
    
    @After
    public void teardown() {
        response.close();
        client.close();
    }

}
