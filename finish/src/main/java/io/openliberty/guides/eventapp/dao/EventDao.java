package io.openliberty.guides.eventapp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import io.openliberty.guides.eventapp.models.Event;

@Stateless
public class EventDao {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public void createEvent(Event event) {
        this.em.persist(event);
    }

    public Event readEvent(int eventId) {
        return this.em.find(Event.class, eventId);
    }

    public void updateEvent(Event event) {
        this.em.merge(event);
    }

    public void deleteEvent(Event event) {
        this.em.remove(event);
    }

    public List<Event> readEventsByLocationTime(String location, String time) {
        TypedQuery<Event> query = this.em.createNamedQuery("Event.findByLocationTime", Event.class).setParameter("location", location).setParameter("time", time);
        return query.getResultList();
    }

    public List<Event> readAllEvents() {
        return this.em.createNamedQuery("Event.findAll", Event.class).getResultList();
    }
}
