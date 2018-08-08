// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.jpaguide.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
