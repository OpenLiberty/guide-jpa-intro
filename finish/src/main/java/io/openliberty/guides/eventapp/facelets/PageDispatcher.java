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


    public void showEventForm() {
        pageLoader.setContent("content/eventForm.xhtml");
        pageLoader.setCurrentPage("Event Creation");
    }

    public void showMainPage() {
        pageLoader.setContent("content/mainPage.xhtml");
        pageLoader.setCurrentPage("Events");
    }

    public void showEditPage(){
        pageLoader.setContent("content/updateEventForm.xhtml");
        pageLoader.setCurrentPage("Edit Event");
    }

}
