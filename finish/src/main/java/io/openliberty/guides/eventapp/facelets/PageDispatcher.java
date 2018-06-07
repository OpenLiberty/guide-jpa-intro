package io.openliberty.guides.eventapp.facelets;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class PageDispatcher {

  @ManagedProperty(value = "#{pageLoader}")
  public PageLoader pageLoader;

  String currentRole = null;

  public PageLoader getPageLoader() {
    return pageLoader;
  }

  public void setPageLoader(PageLoader pageLoader) {
    this.pageLoader = pageLoader;
  }

  public String getCurrentRole() {
    return currentRole;
  }

  public void showEventForm() {
    pageLoader.setContent("content/eventForm.xhtml");
    pageLoader.setCurrentPage("Event Creation");
  }

  public void showDetails() {
    pageLoader.setContent("content/eventDetail.xhtml");
    pageLoader.setCurrentPage("Event Details");
  }

  public void showUserForm() {
    pageLoader.setContent("content/userForm.xhtml");
    pageLoader.setCurrentPage("Registration");
  }

  public void showUserProfile() {
      pageLoader.setContent("content/userProfile.xhtml");
      pageLoader.setCurrentPage("User Profile");
  }

  public void showMainPage() {
    pageLoader.setContent("content/mainPage.xhtml");
    pageLoader.setCurrentPage("Events");
  }

  public void showLogin() {
      pageLoader.setContent("content/logInSection.xhtml");
      pageLoader.setCurrentPage("Login");
  }

  public void showLogError() {
      pageLoader.setContent("content/logErrorSection.xhtml");
      pageLoader.setCurrentPage("Login Error");
  }

  public void showLogBlocked() {
      pageLoader.setContent("content/logBlockedSection.xhtml");
      pageLoader.setCurrentPage("Login Blocked");
  }

}
