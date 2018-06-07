package io.openliberty.guides.eventapp.resources;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.openliberty.guides.eventapp.lists.EventList;
import io.openliberty.guides.eventapp.models.Event;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.security.Principal;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@DeclareRoles({"registeredUser", "eventAdministrator"})
@RequestScoped
@Path("events")
public class EventService {

  public static EventList eventList;
  public static int counter = 0;


   @Context
   private SecurityContext securityContext;


  @Inject
  public void setController() {
    eventList = new EventList();
  }

  public static EventList getController() {
    return eventList;
  }


  @GET
  @Produces(value = "text/plain")
  @Path("token")
  @RolesAllowed({"registeredUser", "eventAdministrator"})
  public String getList() {
    // retrieve the authentication scheme that was used(e.g. BASIC)
    String authnScheme = securityContext.getAuthenticationScheme();
    // retrieve the name of the Principal that invoked the resource
    String username = securityContext.getUserPrincipal().getName();
    // check if the current user is in Role1
    Boolean isUserInRole = securityContext.isUserInRole("eventAdministrator");
    String result = "";

    if (isUserInRole) {
      result = authnScheme + " " + username + " " + "eventAdministrator";
    } else {
      result = authnScheme + " " + username + " " + "registeredUser";
    }

    return result;
  }

  /**
   * This method creates a new event from the submitted data (name, time and
   * location) by the user.
   */
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void addNewEvent(@FormParam("name") String name,
      @FormParam("time") String time, @FormParam("location") String location) {
    eventList.addEvent(name, time, location, counter);
    EventService.counter++;
  }

  /**
   * This method displays the existing/stored events in Json format
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonArray getEvents() {
    return eventList.getEvents();
  }

  /**
   * This method displays a specific existing/stored event in Json format
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{name}")
  public Event getEventByName(@PathParam("name") String eventName) {
    return eventList.getEventByName(eventName);
  }

}
