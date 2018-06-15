package io.openliberty.guides.eventapp.resources;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.transaction.*;

import io.openliberty.guides.eventapp.models.User;
import io.openliberty.guides.eventapp.dao.EventDao;
import io.openliberty.guides.eventapp.dao.UserDao;
import io.openliberty.guides.eventapp.models.Event;

@RequestScoped
@Path("users")
public class UserService {

    @EJB private UserDao userDAO;
    @EJB private EventDao eventDAO;

    /**
     * This method registers a user to a specific event.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public void addUser(@FormParam("name") String userName, @FormParam("email") String email,  @FormParam("event") String eventId) {
        System.out.println("AddUser: " + userName + ", " + email + ", " + eventId);
        Event targetEvent = this.eventDAO.readEvent(Integer.valueOf(eventId));
        User targetUser = this.userDAO.readUser(userName);
        if (targetUser == null) {
            targetUser = new User(userName, email, "", "normal");
            targetUser.getEvents().add(targetEvent);
            this.userDAO.createUser(targetUser);
        } else {
            targetUser.getEvents().add(targetEvent);
            this.userDAO.updateUser(targetUser);
        }
    }

    /**
     * This method displays a specific user stored in Json format
     */
    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User getUser(@PathParam("name") String userName) {
        return this.userDAO.readUser(userName);
    }

    /**
     * This method displays the list of users stored in Json format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getUsers() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder jArray = Json.createArrayBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();

        for (User user : this.userDAO.readAllUsers()) {
            builder.add("name", user.getName()).add("email", user.getEmail());
            for (Event event : user.getEvents()) {
                jArray.add(Json.createObjectBuilder().add("name", event.getName()).add("time", event.getTime()).add("location", event.getLocation()).build());
            }
            finalArray.add(builder.add("events", jArray.build()).build());
        }
        return finalArray.build();
    }

}
