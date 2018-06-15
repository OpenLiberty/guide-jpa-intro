package io.openliberty.guides.eventapp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.openliberty.guides.eventapp.models.User;

@Stateless
public class UserDao {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public void createUser(User user) {
        this.em.persist(user);
    }

    public User readUser(String userName) {
        return this.em.find(User.class, userName);
    }

    public void updateUser(User user) {
        this.em.merge(user);
    }

    public void deleteUser(User user) {
        this.em.remove(user);
    }

    public List<User> readAllUsers() {
        return this.em.createNamedQuery("User.findAll", User.class).getResultList();
    }
}
