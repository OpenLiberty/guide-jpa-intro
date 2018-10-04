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

    protected void sendForm(HashMap<String, String> formDataMap, String url) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(url);
        response = webTarget.request().post(Entity.form(form));
        form = new Form();
    }

    protected void deleteForm(String url) {
        webTarget = client.target(url);
        response = webTarget.request().delete();
        form = new Form();
    }

    protected void updateForm(HashMap<String, String> formDataMap, String url) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(url);
        response = webTarget.request().put(Entity.form(form));
        form = new Form();
    }

    protected JsonObject getTestEvent() {
        webTarget = client.target(baseUrl + EVENTS);
        response = webTarget.request().get();
        JsonArray eventsArray = response.readEntity(JsonArray.class);
        JsonObject event = findTestEvent(eventsArray);
        return event;
    }

    protected JsonObject findTestEvent(JsonArray events) {
        for (int i = 0; i < events.size(); i++) {
            JsonObject testEvent = events.getJsonObject(i);
            Event test = new Event(testEvent.getString("name"),
                    testEvent.getString("location"),
                    testEvent.getString("time"));
            if (test.equals(e)){
                return testEvent;
            }
        }
        return null;
    }

    protected void assertData(HashMap<String, String> testedData) {
        testedData.forEach((actual, expected) -> {
            assertEquals(expected, actual);
        });
    }

}