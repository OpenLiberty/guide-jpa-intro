package io.openliberty.guides.jpaguide.models;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "Event")
@NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e")
public class Event implements Serializable{
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id @Column(name = "eventId")
    private int id;

    @Column(name = "eventLocation")
    private String location;
    @Column(name = "eventTime")
    private String time;
    @Column(name = "eventName")
    private String name;

    public Event() { }

    public Event(String name, String location, String time) {
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
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
        if(name == null) {
            if(other.name != null)
                return false;
        }else if(!name.equals(other.name))
            return false;    
 
        return true;
    }

    @Override
    public String toString() {
        return "Event [name=" + name + ", location=" + location + ", time=" + time + "]";
    }
}
