package io.openliberty.guides.eventapp.lists;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import io.openliberty.guides.eventapp.models.User;

@ManagedBean
@ApplicationScoped
@SessionScoped
public class UserList {

  private static ArrayList<User> users = new ArrayList<>();

  public void addUser(User newUser) {
    users.add(newUser);
    //System.out.println("add user" + user.toString());
  }

  public User getUser(String name) {
    for (User user : users) {
      if (user.getName().equals(name)) {
        //System.out.println("get user" + user.toString());
        return user;
      }
    }
    return null;
  }

  public ArrayList<User> getUsersList() {
    return users;
  }

  public JsonArray getUsersJson() {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    JsonArrayBuilder jArray = Json.createArrayBuilder();
    JsonArrayBuilder finalArray = Json.createArrayBuilder();
    for (User user : users) {
      builder.add("name", user.getName()).add("password", user.getPassword())
             .add("email", user.getEmail());
      for (int eventId : user.getEvents()) {
        jArray.add(Json.createObjectBuilder().add("EventID", eventId).build());
      }
      finalArray.add(builder.add("events", jArray.build()).build());
    }
    return finalArray.build();
  }

  public void addEventToUser(String name, String email, int eventId) {
    User user = this.getUser(name);
    if (user != null){
      user.setEmail(email);
      user.addEvent(eventId);
      System.out.println("add event "+ eventId + " to user" + user.toString());
    }
  
  }

}
