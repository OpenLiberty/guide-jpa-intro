package io.openliberty.guides.eventapp.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
@NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name LIKE :name")
public class User {

    @Id @Column(name = "userName")
    private String name;
    @Column(name = "userEmail")
    private String email;
    @Column(name = "userPassword")
    private String password;
    @Column(name = "userRole")
    private String role;

    @ManyToMany(mappedBy = "users")
    private Set<Event> events = new HashSet<Event>();

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String toString() {
        return this.name + " " + this.password + " " + this.email + " " + this.role + " "
                + Arrays.toString(this.events.toArray());
    }

}
