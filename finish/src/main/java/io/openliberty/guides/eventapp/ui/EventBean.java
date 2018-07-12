package io.openliberty.guides.eventapp.ui;

import java.util.Map;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import io.openliberty.guides.eventapp.dao.EventDao;
import io.openliberty.guides.eventapp.facelets.PageDispatcher;
import io.openliberty.guides.eventapp.models.Event;
import io.openliberty.guides.eventapp.ui.util.ServiceUtil;
import io.openliberty.guides.eventapp.ui.util.TimeMapUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean
@ViewScoped
public class EventBean {

    private String name;
    private String location;
    private String day;
    private String month;
    private String year;
    private String hour;
    private int selectedId;
    private boolean notValidTime;

    @ManagedProperty(value = "#{pageDispatcher}")
    public PageDispatcher pageDispatcher;

    @EJB private EventDao eventDAO;

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getHour() {
        return hour;
    }

    public String getDay() {
        return this.day;
    }

    public String getMonth() {
        return this.month;
    }

    public String getYear() {
        return this.year;
    }

    public boolean getNotValidTime() {
        return notValidTime;
    }

    public PageDispatcher getPageDispatcher() {
        return this.pageDispatcher;
    }

    public void setPageDispatcher(PageDispatcher pageDispatcher) {
        this.pageDispatcher = pageDispatcher;
    }

    /**
     * Helper method to create the time string to be stored at the back end.
     */
    private String createStoredTime() {
        return hour + ", " + month + " " + day + " " + year;
    }

    /**
     * Mapped the time string retrieved from back end service to a user readable
     * format.
     */
    public String showTime(String storedTime) {
        return TimeMapUtil.getMappedDate(storedTime);
    }

    /**
     * Submit event form data to back end service.
     */
    public void submitToService() {
        String time = createStoredTime();
        ServiceUtil.submitEventToService(name, location, time);
        pageDispatcher.showMainPage();
        clear();
    }

    /**
     * Delete event form data to back end service.
     */
    public void deleteFromService(){
        ServiceUtil.deleteEventService(this.selectedId);
        pageDispatcher.showMainPage();
    }

    /**
     * Retrieve the list of events from back end service.
     */
    public static List<Event> retrieveEventList() {
        return ServiceUtil.retrieveEvents();
    }

    /**
     * Set a selected event id.
     */
    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * Remove stored event id.
     */
    public void removeSelectedId() {
        this.selectedId = -1;
    }

    /**
     * Retrieve a selected event with the selected event name.
     */
    public Event retrieveSelectedEvent() {
        return this.eventDAO.readEvent(this.selectedId);
    }

    /**
     * Retrieve a selected event by Id
     */
    public Event retrieveEventByCurrentId(int currentId) {
        return ServiceUtil.retrieveEventById(currentId);
    }

    /**
     * Gets TimeMapUtil map for hours
     */
    public Map<String, Object> getHoursMap() {
        return TimeMapUtil.getHours();
    } 

    /**
     * Gets TimeMapUtil map for days
     */
    public Map<String, Object> getDaysMap() {
        return TimeMapUtil.getDays();
    }

    /**
     * Gets TimeMapUtil map for months
     */
    public Map<String, Object> getMonthsMap() {
        return TimeMapUtil.getMonths();
    }

    /**
     * Gets TimeMapUtil map for years
     */
    public Map<String, Object> getYearsMap() {
        return TimeMapUtil.getYears();
    }

    /**
     * Checks the user input for the event time.
     */
    public void checkTime(ComponentSystemEvent event) throws ParseException {
        String hour = getUnitOfTime(event, "eventHour");
        String day = getUnitOfTime(event, "eventDay");
        String month = getUnitOfTime(event, "eventMonth");
        String year = getUnitOfTime(event, "eventYear");

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a, MMMM d yyyy");
        formatter.setLenient(false);
        Date userDate;

        try {
            userDate = formatter.parse(hour + ", " + month + " " + day + " " + year);
            if (userDate.before(new Date())) {
                allowSubmission(event, false);
                addErrorMessage(event, "Choose a valid time");
                displayError(true);
            } else {
                allowSubmission(event, true);
                displayError(false);
            }
        } catch (Exception e) {
            allowSubmission(event, false);
            addErrorMessage(event, "Choose a valid time");
            displayError(true);
        }
    }

    /**
     * Displays the error message if time is not valid
     */
    public void displayError(boolean display) {
        notValidTime = display;
    }

    /**
     * Adds "Choose a valid time" message after selectOptions in user interface.
     */
    private void addErrorMessage(ComponentSystemEvent event, String errorMessage) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(errorMessage);
        HtmlPanelGroup divEventTime = (HtmlPanelGroup) event.getComponent().findComponent("eventform:eventTime");
        context.addMessage(divEventTime.getClientId(context), message);
    }

    /**
     * Gets 'selectOptions' for a specific unit of time from user interface
     */
    private UIInput getUnitOfTimeOptions(ComponentSystemEvent event, String unit) {
        UIComponent components = event.getComponent();
        UIInput unitOptions = (UIInput) components.findComponent("eventform:" + unit);
        return unitOptions;
    }

    /**
     * Gets a unit of time from the submitted values, like hour and year
     */
    private String getUnitOfTime(ComponentSystemEvent event, String unit) {
        UIInput unitOptions = getUnitOfTimeOptions(event, unit);
        return (String) unitOptions.getLocalValue();
    }

    /**
     * Allow if user can submit or not.
     */
    private void allowSubmission(ComponentSystemEvent event, boolean allowSubmission) {
        UIComponent components = event.getComponent();
        HtmlInputHidden formInput = (HtmlInputHidden) components.findComponent("eventform:eventSubmit");
        formInput.setValid(allowSubmission);
    }

    /**
     * Helper method to clean the bean after form submission.
     */
    private void clear() {
        setName(null);
        setLocation(null);
        setDay(null);
        setMonth(null);
        setYear(null);
        setHour(null);
    }
}
