package org.posgres.resource;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

@QuarkusTest
public class PersonResourceIntegrationTest {

    @Test
    @TestTransaction // runs in a tx that is rolled back after the test
    @Disabled
    void crud_flow() {
        // create
        var id = given()
                .contentType(JSON)
                .body(Map.of("name", "Ada Lovelace", "age", 36))
                .when().post("/people")
                .then().statusCode(200)
                .extract().jsonPath().getLong("id");

        // read all
        given().when().get("/people")
                .then().statusCode(200)
                .body("size()", is(1));

        // read one
        given().when().get("/people/" + id)
                .then().statusCode(200)
                .body("name", is("Ada Lovelace"));

        // update
        given().contentType(JSON)
                .body(Map.of("name", "Ada", "age", 37))
                .when().put("/people/" + id)
                .then().statusCode(200)
                .body("age", is(37));

        // delete
        given().when().delete("/people/" + id)
                .then().statusCode(204);

        // list empty again (same tx still open)
        given().when().get("/people")
                .then().statusCode(200)
                .body("size()", is(0));
    }
}
