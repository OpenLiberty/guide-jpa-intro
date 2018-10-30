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
package io.openliberty.guides.client;

import javax.enterprise.context.Dependent;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;

import javax.json.JsonObject;
import javax.json.JsonArray;
import java.util.List;

@Dependent
@RegisterRestClient
@RegisterProvider(UnknownUrlExceptionMapper.class)
@Path("/events")
public interface EventClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getEvents() throws UnknownUrlException;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getEvent(@PathParam("id") int eventId) throws UnknownUrlException;

    @DELETE
    @Path("/{id}")
    public void deleteEvent(@PathParam("id") int eventId) throws UnknownUrlException;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addEvent(@FormParam("name") String name,
        @FormParam("time") String time, @FormParam("location") String location) throws 
        UnknownUrlException;

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void updateEvent(@FormParam("name") String name,
        @FormParam("time") String time, @FormParam("location") String location, 
        @PathParam("id") int id) throws UnknownUrlException;

}
