package io.openliberty.guides.eventapp.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "Event")
@NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e")
@NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name LIKE :name")
@NamedQuery(name = "Event.findByLocationTime", query = "SELECT e FROM Event e WHERE e.location LIKE :location AND e.time LIKE :time")
public class Event implements Serializable{
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id @Column(name = "eventId") private int id;

    @Column(name = "eventLocation")
    private String location;
    @Column(name = "eventTime")
    private String time;

    @Column(name = "eventName")
    private String name;

    @ManyToMany(cascade = { 
                CascadeType.PERSIST, 
                CascadeType.MERGE})
    @JoinTable(name = "event_user",
        joinColumns = @JoinColumn(name = "eventId"),
        inverseJoinColumns = @JoinColumn(name = "userName"))
    private Set<User> users = new HashSet<User>();

    public Event() { }

    public Event(String name, String location, String time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Event [name=" + name + ", location=" + location + ", time=" + time + "]";
    }
}
