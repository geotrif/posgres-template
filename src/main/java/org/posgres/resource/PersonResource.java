package org.posgres.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.posgres.model.Person;
import org.posgres.service.PersonService;

import java.util.List;


/**
 Create a person
 curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"Ada Lovelace\",\"age\":36}" http://localhost:8080/people

 Get all people
 curl http://localhost:8080/people

 Get person by ID (example: ID = 1)
 curl http://localhost:8080/people/1

 Update person (example: ID = 1)
 curl -X PUT -H "Content-Type: application/json" -d "{\"name\":\"Ada Lovelace\",\"age\":37}" http://localhost:8080/people/1

 Delete person (example: ID = 1)
 curl -X DELETE http://localhost:8080/people/1
 */
@Path("/people")
public class PersonResource {

    @Inject
    private PersonService personService;

    @GET
    public List<Person> listAllPersons() {
        return personService.listAllPersons();
    }

    @GET
    @Path("/{id}")
    public Person getPersonById(@PathParam("id") Long id) {
        return personService.getPersonById(id);
    }

    @POST
    public Person addPerson(Person person) {
        return personService.addPerson(person);
    }

    @PUT
    @Path("/{id}")
    public Person updatePerson(@PathParam("id") Long id, Person person) {
        return personService.updatePerson(id, person);
    }

    @DELETE
    @Path("/{id}")
    public void deleteUserById(@PathParam("id") Long id) {
        personService.deleteUserById(id);
    }
}
