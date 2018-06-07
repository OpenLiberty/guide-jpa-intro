package io.openliberty.guides.eventapp.ui;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import io.openliberty.guides.eventapp.models.User;
import io.openliberty.guides.eventapp.facelets.PageDispatcher;
import io.openliberty.guides.eventapp.lists.UserList;
import io.openliberty.guides.eventapp.ui.util.ServiceUtil;
import io.openliberty.guides.eventapp.ui.util.SessionUtils;
import io.openliberty.guides.eventapp.resources.UserService;

@ManagedBean
@ViewScoped
public class UserBean {

  private String name;
  private String email;
  private String eventName;
  private int eventId;

  @ManagedProperty(value = "#{pageDispatcher}")
  public PageDispatcher pageDispatcher;

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
    if (current != null){
      return (current.getRole().equals("eventAdministrator"));
    }
    return false;
  }

  public User getUserInSession() {
    String sessionUserName = SessionUtils.getUserObj().getName();
    UserList userlist = UserService.userList;
    User result = userlist.getUser(sessionUserName);
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
    if(getAuthenticated()) {
      setName(getUserInSession().getName());
    }
  }

  public boolean isRegistered(int eventId) {
    if (SessionUtils.getUserObj() != null) {
      User user = getUserInSession();
      for (int userEventID : user.getEvents()) {
        if (userEventID == eventId) {
          return true;
        }
      }
      return false;
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
