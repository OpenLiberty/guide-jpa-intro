package io.openliberty.guides.eventapp.ui;

import java.util.Objects;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import io.openliberty.guides.eventapp.models.Event;
import io.openliberty.guides.eventapp.models.User;
import io.openliberty.guides.eventapp.dao.EventDao;
import io.openliberty.guides.eventapp.dao.UserDao;
import io.openliberty.guides.eventapp.facelets.PageDispatcher;
import io.openliberty.guides.eventapp.ui.util.ServiceUtil;
import io.openliberty.guides.eventapp.ui.util.SessionUtils;

@ManagedBean
@ViewScoped
public class UserBean {

    private String name;
    private String email;
    private String eventName;
    private int eventId;

    @ManagedProperty(value = "#{pageDispatcher}")
    public PageDispatcher pageDispatcher;

    @EJB private UserDao userDAO;
    @EJB private EventDao eventDAO;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getEventName() {
        return eventName;
    }

    public int getEventId() {
        return eventId;
    }

    public boolean getAuthenticated() {
        return (SessionUtils.getUserObj() != null);
    }

    public boolean getRoleIsAdmin() {
        User current = SessionUtils.getUserObj();
        if (current != null) {
            return (current.getRole().equals("eventAdministrator"));
        }
        return false;
    }

    public User getUserInSession() {
        String sessionUserName = SessionUtils.getUserObj().getName();
        User result = this.userDAO.readUser(sessionUserName);
        return result;
    }

    // submit User form data to JAX-RS service
    public void submitToService() {
        System.out.println("Submitting user form " + name + email + eventId);
        ServiceUtil.submitUserToService(name, email, eventId);
        pageDispatcher.showMainPage();
        clear();
    }

    public void submitToService(int eventId) {
        if (SessionUtils.getUserObj() != null) {
            ServiceUtil.submitUserToService(getUserInSession().getName(), getUserInSession().getEmail(), eventId);
            pageDispatcher.showUserProfile();
            clear();
        } else {
            pageDispatcher.showUserForm();
            clear();
        }
    }

    public void registerUser() {
        if (getAuthenticated()) {
            setName(getUserInSession().getName());
        }
    }

    public boolean isRegistered(int eventId) {
        if (SessionUtils.getUserObj() != null) {
          User user = getUserInSession();
          Event event = this.eventDAO.readEvent(eventId);
          return user.getEvents().stream().anyMatch(e -> Objects.equals(e, event));
        }
        return false;
      }

    public PageDispatcher getPageDispatcher() {
        return pageDispatcher;
    }

    public void setPageDispatcher(PageDispatcher pageDispatcher) {
        this.pageDispatcher = pageDispatcher;
    }

    public void valueChangeMethod(ValueChangeEvent e) {
        String eventId = (String) e.getNewValue();
        setEventId(Integer.valueOf(eventId));
    }

    private void clear() {
        setName(null);
        setEmail(null);
        setEventName(null);
    }
}
