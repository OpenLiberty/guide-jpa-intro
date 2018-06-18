package it.io.openliberty.guides.eventapp.tests;

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
import org.junit.Test;

public class EventAppTest {

    public static final String EVENTS_URL = "http://localhost:9080/events";

    public static final String JSONFIELD_LOCATION = "location";
    public static final String JSONFIELD_NAME = "name";
    public static final String JSONFIELD_EVENT = "event";
    public static final String JSONFIELD_TIME = "time";
    public static final String EVENT_TIME = "12:00 PM, January 1 2018";
    public static final String EVENT_LOCATION = "IBM";
    public static final String EVENT_NAME = "JPA Guide";
    public static final String EVENT_ID = "0";

    private Form form;
    private Client client;
    private WebTarget webTarget;
    private Response response;
    private HashMap<String, String> eventForm;
    private HashMap<String, String> userForm;
    private HashMap<String, String> actualDataStored;

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
    }

    @Test
    public void runTestsInOrder() {
        testCreatingNewEvent();
        testShowingExistingEvents();
    }

    public void testCreatingNewEvent() {
        JsonObject event = sendForm(eventForm, EVENTS_URL);
        actualDataStored.put(event.getString(JSONFIELD_NAME), EVENT_NAME);
        actualDataStored.put(event.getString(JSONFIELD_LOCATION), EVENT_LOCATION);
        actualDataStored.put(event.getString(JSONFIELD_TIME), EVENT_TIME);
        assertData(actualDataStored);
    }

    public void testShowingExistingEvents() {
        webTarget = client.target(EVENTS_URL);
        response = webTarget.request().get();
        JsonArray events = response.readEntity(JsonArray.class);
        actualDataStored.put(getJsonFieldValue(events, JSONFIELD_NAME), EVENT_NAME);
        actualDataStored.put(getJsonFieldValue(events, JSONFIELD_LOCATION), EVENT_LOCATION);
        assertData(actualDataStored);
    }

    private JsonObject sendForm(HashMap<String, String> formDataMap, String url) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(url);
        response = webTarget.request().post(Entity.form(form));
        webTarget = client.target(EVENTS_URL);
        response = webTarget.request().get();
        JsonArray eventsArray = response.readEntity(JsonArray.class);
        JsonObject event = eventsArray.getJsonObject(0);
        form = new Form();
        return event;
    }

    private String getJsonFieldValue(JsonArray array, String field) {
        return array.getJsonObject(0).getString(field);
    }

    private JsonArray getJsonArray(JsonArray array, String field) {
        return array.getJsonObject(0).getJsonArray(field);
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
