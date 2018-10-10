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

import static org.junit.Assert.assertNull;

import java.util.HashMap;
import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Form;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import io.openliberty.guides.jpa.models.Event;

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
        actualDataStored.put(event.getString(JSONFIELD_LOCATION),
                UPDATE_EVENT_LOCATION);
        actualDataStored.put(event.getString(JSONFIELD_TIME), UPDATE_EVENT_TIME);
        assertData(actualDataStored);

        deleteForm(baseUrl + EVENTS + "/" + event.getInt("id"));
        assertNull(getTestEvent());
    }

    @After
    public void teardown() {
        response.close();
        client.close();
    }

}
