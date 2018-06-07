package io.openliberty.guides.eventapp.models;

import java.util.ArrayList;
import javax.persistence.*;

@Entity
@Table(name = "Event", schema="JPAINTRO")
@NamedQuery(name="Event.findAll",query="SELECT e FROM Event e")
//WHERE e.name LIKE '%Ahmad%'
public class Event {

  @Id
  @Column(name="eventId")
  private int id;
  @Column(name="eventName")
	private String name;
  @Column(name="eventLocation")
	private String location;
  @Column(name="eventTime")
	private String time;
  @Transient
	private ArrayList<User> users;

	public Event(String name, String location, String time, int id) {
		users = new ArrayList<User>();
		this.name = name;
		this.location = location;
		this.time = time;
		this.id = id;
	}

	public Event() {}

	public int getId() {
		return id;
	}

  public void setId(int id){
    this.id = id;
  }

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getTime() {
		return time;
	}

	public void addUser(User user) {
		users.add(user);
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

  @Override
  public String toString() {
    return "Event [id=" + id + ", location=" + location
        + ", name=" + name + ", time=" + time + "]";
  }
}
