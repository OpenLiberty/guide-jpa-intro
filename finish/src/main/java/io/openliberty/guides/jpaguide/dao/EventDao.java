package io.openliberty.guides.jpaguide.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import io.openliberty.guides.jpaguide.models.Event;

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

    public List<Event> readAllEvents() {
        return this.em.createNamedQuery("Event.findAll", Event.class).getResultList();
    }
}
