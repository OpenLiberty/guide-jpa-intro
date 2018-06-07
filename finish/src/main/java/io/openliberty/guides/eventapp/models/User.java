package io.openliberty.guides.eventapp.models;

import java.util.ArrayList;
import java.util.Arrays;

public class User {
  private String name;
  private String email;
  private String password;
  private String role;
  private ArrayList<Integer> events;

  public User(String name, String email, String password, String role) {
      events = new ArrayList<Integer>();
      this.name = name;
      this.email = email;
      this.password = password;
      this.role = role;
    }

  public void setRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void addEvent(int eventId) {
    events.add(eventId);
  }

  public ArrayList<Integer> getEvents() {
    return events;
  }

  public String toString() {
    return this.name + " " + this.password + " " + this.email + " " + this.role + " "
        + Arrays.toString(this.events.toArray());
  }

}
