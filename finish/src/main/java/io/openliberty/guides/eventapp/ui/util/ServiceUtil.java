package io.openliberty.guides.eventapp.ui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import io.openliberty.guides.eventapp.models.Event;
import io.openliberty.guides.eventapp.models.User;

public class ServiceUtil {

  // Back end service URLs
  private static String port = System.getProperty("default.http.port");
  private static String eventServiceURL = "http://localhost:" + port
      + "/events";
  private static final String userServiceURL = "http://localhost:" + port
      + "/users";

  /**
   * Post event form data to back end service
   */
  public static void submitEventToService(String name, String location,
      String time) {
    Form form = new Form().param("name", name).param("time", time)
                          .param("location", location);
    Response response = connectToService(eventServiceURL).post(Entity.form(form));
    response.close();
  }

  /**
   * Post user form data to back end service
   */
  public static void submitUserToService(String name, String email,
      int eventId) {
    Form form = new Form().param("name", name).param("email", email)
                          .param("event", String.valueOf(eventId));
    Response response = connectToService(userServiceURL).post(Entity.form(form));
    response.close();
  }


  /**
   * Retrieve list of events from back end storage.
   */
  public static List<Event> retrieveEvents() {
    JsonArray jr = retrieveFromService();
    List<Event> events = jr.stream().map(eventJson -> {
      Event event = new Event(((JsonObject) eventJson).getString("name"),
                              ((JsonObject) eventJson).getString("location"),
                              ((JsonObject) eventJson).getString("time"),
                              ((JsonObject) eventJson).getInt("id"));
      JsonArray userJr = ((JsonObject) eventJson).getJsonArray("users");
      event.setUsers((ArrayList<User>) retrieveUsersFromJsonArray(userJr));
      return event;
    }).collect(Collectors.toList());

    return events;
  }

  /**
   * Retrieve an event by its id.
   */

  public static Event retrieveEventById(int eventId) {
    if (eventId < 0) {
      return null;
    }
    List<Event> events = retrieveEvents();
    for (Event e : events) {
      if (e.getId() == eventId) {
        return e;
      }
    }
    return null;
  }

  /**
   * Retrieve user list for an event id.
   */

  public static List<User> retrieveUsersByEventId(int eventId) {
    List<Event> events = retrieveEvents();
    List<User> users = new ArrayList<User>();
    for (Event e : events) {
      if (e.getId() == eventId) {
        users = e.getUsers();
        return users;
      }
    }
    return users;
  }

  /**
   * Helper method to create a connection to the back end service via the URL.
   */
  private static Builder connectToService(String URL) {
    Client client = ClientBuilder.newClient();
    client.property("com.ibm.ws.jaxrs.client.ltpa.handler", "true");
    Builder builder = client.target(URL).request();
    client.close();
    return builder;
  }

  /**
   * Helper method to retrieve user list from Json Array.
   */
  private static List<User> retrieveUsersFromJsonArray(JsonArray userJr) {
    List<User> users = userJr.stream().map(userJson -> {
      User user = new User(((JsonObject) userJson).getString("name"),
                           ((JsonObject) userJson).getString("email"), "", "");
      return user;
    }).collect(Collectors.toList());

    return users;
  }

  /**
   * Helper method to retrieve the representation of events in Json Array format
   * from the back end service.
   */
  private static JsonArray retrieveFromService() {
    Response response = connectToService(eventServiceURL).get();
    JsonArray jr = response.readEntity(JsonArray.class);
    response.close();
    return jr;
  }
}
