package org.posgres.resource;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.posgres.model.Person;
import org.posgres.service.PersonService;

import java.util.List;
import java.util.Map;

@QuarkusTest
public class PersonResourceTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonResource personResource;

    @BeforeEach
    public void setup() {
        PanacheMock.mock(Person.class);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void listAllPersons_success() {
        var person = getPersonForTest();
        Mockito.when(Person.listAll()).thenReturn(List.of(person));

        given().when()
                .get("/people")
                .then()
                .statusCode(200)
                .body("[0].name", is("Ada"));
    }

    @Test
    public void listAllPersons_emptyList() {
        Mockito.when(Person.listAll()).thenReturn(List.of());

        given().when()
                .get("/people")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void getPersonById_success() {
        Mockito.when(Person.findById(1L)).thenReturn(getPersonForTest());

        given().when()
                .get("/people/1")
                .then()
                .statusCode(200)
                .body("name", is("Ada"));
    }

    @Test
    public void getPersonById_noDataInPerson() {
        Mockito.when(Person.findById(1L)).thenReturn(new Person());

        given().when()
                .get("/people/1")
                .then()
                .statusCode(200)
                .body("name", nullValue());
    }

    @Test
    @Disabled
    void addPerson_persist() {
        // Given
        Person person = new Person();
        person.id = 1L;
        person.name = "test";
        person.age = 20;
        Mockito.when(personService.addPerson(person)).thenReturn(person);
        var payload = Map.of("name",person.name,"age",person.age);

        // When
        Person actualResult = personResource.addPerson(person);

        // Then
        Assertions.assertEquals("test", actualResult.name);
        given().contentType(JSON).body(payload)
                .when()
                .post("/people")
                .then()
                .statusCode(200)
                .body("name", is("test"));
    }

    private Person getPersonForTest() {
        var person = new Person();
        person.id = 1L;
        person.name = "Ada";
        person.age = 36;

        return person;
    }
}
