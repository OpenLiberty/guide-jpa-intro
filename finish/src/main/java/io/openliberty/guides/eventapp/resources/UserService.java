package io.openliberty.guides.eventapp.resources;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.json.JsonArray;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Context;

import io.openliberty.guides.eventapp.models.User;
import io.openliberty.guides.eventapp.lists.EventList;
import io.openliberty.guides.eventapp.lists.UserList;
import io.openliberty.guides.eventapp.models.Event;

@RequestScoped
@Path("users")
public class UserService {

  @Context
  private SecurityContext securityContext;

  public static UserList userList = new UserList();

  private EventList eventList;

  public UserService() {
    eventList = EventService.eventList;
  }
  
  /**
   * This method registers a user to a specific event.
   */
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void addUser(@FormParam("name") String name,
      @FormParam("email") String email, @FormParam("event") String eventId) {
    User targetUser = userList.getUser(name);
    int targetEventId = Integer.valueOf(eventId);
    Event targetEvent = eventList.getEventById(targetEventId);
    System.out.println("target event id " + eventId);
    
    // when the user is not yet found in the userlist
    if (targetUser == null) {
      User newNonLogInUser = new User(name, email, "", "normal");
      userList.addUser(newNonLogInUser);
      eventList.addUserToEvent(newNonLogInUser, targetEventId);
    }
    // when the user is found in the userlist
    else {
      if (targetEvent.getUsers().contains(targetUser)) {
        // avoid repeatedly add the same user to the same event
        return; 
      }
      eventList.addUserToEvent(targetUser, targetEventId);
    }
    userList.addEventToUser(name, email, targetEventId);
  }
  
  /**
   * This method displays the name and role of a user as well as the authentication type 
   */
  @GET
  @Produces(value = "text/plain")
  @Path("token")
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
   * This method displays the list of users stored in Json format
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonArray getUsers() {
    return userList.getUsersJson();
  }

  /**
   * This method displays a specific user stored in Json format
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{name}")
  public User getUser(@PathParam("name") String username) {
    return userList.getUser(username);
  }

}
