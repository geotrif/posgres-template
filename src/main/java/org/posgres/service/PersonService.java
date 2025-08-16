package org.posgres.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.posgres.model.Person;

import java.util.List;

/**
 * -- 1. Create the schema for your app user (if missing)
 * CREATE SCHEMA IF NOT EXISTS app AUTHORIZATION app_user;
 *
 * -- 2. Make sure the user can use and create objects in it
 * GRANT USAGE, CREATE ON SCHEMA app TO app_user;
 *
 * -- 3. Optional: make sure future objects are accessible
 * ALTER DEFAULT PRIVILEGES IN SCHEMA app GRANT ALL ON TABLES TO app_user;
 * ALTER DEFAULT PRIVILEGES IN SCHEMA app GRANT ALL ON SEQUENCES TO app_user;
 *
 * Don't create the database, it will be created by the app, just define the fields in the entity and extend PanacheEntity
 */
@ApplicationScoped
public class PersonService {

    public List<Person> listAllPersons() {
        return Person.listAll();
    }

    public Person getPersonById(Long id) {
        return Person.findById(id);
    }

    @Transactional
    public Person addPerson(Person person) {
        Person.persist(person);

        return person;
    }

    @Transactional
    public Person updatePerson(Long id, Person person) {
        Person entity = Person.findById(id);

        if (entity == null) {
            throw new NotFoundException();
        }

        entity.name = person.name;
        entity.age = person.age;

        return entity;
    }

    @Transactional
    public void deleteUserById(Long id) {
        Person.deleteById(id);
    }
}
