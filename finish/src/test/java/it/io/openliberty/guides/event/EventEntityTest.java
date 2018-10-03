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

import static org.junit.Assert.*;

import java.util.HashMap;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import io.openliberty.guides.models.Event;

public class EventEntityTest {

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

    private static String baseUrl;
    private static String port;

    private static final String EVENTS = "events";

    private Form form;
    private Client client;
    private WebTarget webTarget;
    private Response response;
    private HashMap<String, String> eventForm;
    private HashMap<String, String> actualDataStored;
    private Event e;

    @BeforeClass
    public static void oneTimeSetup() {
        port = System.getProperty("liberty.test.port");
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
    public void testCRUD() {
        sendForm(eventForm, baseUrl + EVENTS);
        JsonObject event = getTestEvent();
        actualDataStored.put(event.getString(JSONFIELD_NAME), EVENT_NAME);
        actualDataStored.put(event.getString(JSONFIELD_LOCATION), EVENT_LOCATION);
        actualDataStored.put(event.getString(JSONFIELD_TIME), EVENT_TIME);
        assertData(actualDataStored);

        eventForm.put(JSONFIELD_NAME, UPDATE_EVENT_NAME);
        eventForm.put(JSONFIELD_LOCATION, UPDATE_EVENT_LOCATION);
        eventForm.put(JSONFIELD_TIME, UPDATE_EVENT_TIME);
        updateForm(eventForm, baseUrl + EVENTS + "/" + event.getInt("id"));

        e = new Event(UPDATE_EVENT_NAME, UPDATE_EVENT_LOCATION, UPDATE_EVENT_TIME);
        event = getTestEvent();
        actualDataStored.put(event.getString(JSONFIELD_NAME), UPDATE_EVENT_NAME);
        actualDataStored.put(event.getString(JSONFIELD_LOCATION), UPDATE_EVENT_LOCATION);
        actualDataStored.put(event.getString(JSONFIELD_TIME), UPDATE_EVENT_TIME);
        assertData(actualDataStored);

        deleteForm(baseUrl + EVENTS + "/" + event.getInt("id"));
        assertNull(getTestEvent());
    }

    private void sendForm(HashMap<String, String> formDataMap, String url) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(url);
        response = webTarget.request().post(Entity.form(form));
        form = new Form();
    }

    private void deleteForm(String url) {
        webTarget = client.target(url);
        response = webTarget.request().delete();
        form = new Form();
    }

    private void updateForm(HashMap<String, String> formDataMap, String url) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(url);
        response = webTarget.request().put(Entity.form(form));
        form = new Form();
    }

    private JsonObject getTestEvent() {
        webTarget = client.target(baseUrl + EVENTS);
        response = webTarget.request().get();
        JsonArray eventsArray = response.readEntity(JsonArray.class);
        JsonObject event = findTestEvent(eventsArray);
        return event;
    }

    private JsonObject findTestEvent(JsonArray events) {
        for (int i = 0; i < events.size(); i++) {
            JsonObject testEvent = events.getJsonObject(i);
            Event test = new Event(testEvent.getString("name"),
                    testEvent.getString("location"),
                    testEvent.getString("time"));
            if (test.equals(e))
                return testEvent;
        }
        return null;
    }

    private void assertData(HashMap<String, String> testedData) {
        testedData.forEach((actual, expected) -> {
            assertEquals(expected, actual);
        });
    }

    @After
    public void teardown() {
        response.close();
        client.close();
    }

}
	