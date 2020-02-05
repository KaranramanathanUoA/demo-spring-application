package com.example.demoProject.demo;

import com.example.demoProject.demo.model.Car;
import com.example.demoProject.demo.repository.CarRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoApplication.class)
@ActiveProfiles({ "integration" })
public class DemoApplicationIntegrationTests {

    @Value("${local.server.port}")
    private int portNumber;

    @BeforeAll
    public void setUp() {
        port = portNumber;
        baseURI = "http://localhost/demo";
    }

    @Test
    public void TestGetAllCarsReturnsListOfVehiclesOk() {
        ValidatableResponse response = given().when().get("/cars").then();
        System.out.println("'GetAllCarsReturnsListOfVehiclesOk()' response:\n" + response.extract().asString());

        response.assertThat().statusCode(HttpStatus.OK.value()).body("content.size()", greaterThan(2))
                .body(containsString("Nissan Mazda")).body("model[1]", equalTo("Subaru Impreza"));
    }

    @Test
    public void TestGetCarByIdReturnsCorrectCarRecord(){
        ValidatableResponse response = given().when().get("/cars/3").then();
        System.out.println("'TestGetCarByIdReturnsCorrectCarRecord()' response:\n" + response.extract().asString());

        response.assertThat().statusCode(HttpStatus.OK.value()).body(containsString("Toyota Corolla"))
                .body("color", equalTo("White"));
    }

    @Test
    public void TestGetCarReturnsErrorWithInvalidVehicleId(){
        ValidatableResponse response = given().when().get("/cars/11").then();

        System.out.println("'TestGetCarReturnsErrorWithInvalidVehicleId()' response:\n" + response.extract().asString());

        response.assertThat().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).body("message",
                containsString("Car with the provided vehicleId does not exist!"));
    }

    @Test
    public void TestAddCarReturnsOkAndAddsCarToDB() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("vehicleId", 5);
        requestBody.put("color", "Maroon");
        requestBody.put("model", "Jaguar X6");

        String jsonString = requestBody.toString();

        ValidatableResponse response = given().contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(jsonString).when().post("/cars").then();

        System.out.println("'TestAddCarReturnsOkAndAddsCarToDB()' response:\n" + response.extract().asString());

        response.assertThat().statusCode(HttpStatus.OK.value()).body("model", equalTo("Jaguar X6"))
                .body("color", equalTo("Maroon"));
    }

    @Test
    public void TestAddCarReturnsErrorWhenPassingAlreadyExistingCarRecordAsParameter() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("vehicleId", 1);
        requestBody.put("color", "Blue");
        requestBody.put("model", "Nissan Mazda");

        String jsonString = requestBody.toString();

        ValidatableResponse response = given().contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(jsonString).when().post("/cars").then();

        System.out.println("'TestAddCarReturnsErrorWhenPassingAlreadyExistingCarRecordAsParameter()' response:\n" + response.extract().asString());

        response.assertThat().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).body("message",
                containsString("Car with vehicleId already exists in the database!"));
    }

    @Test
    public void TestAddCarReturnsErrorWhenPassingIncorrectParameters() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("vehicleId", 7);
        requestBody.put("color", "");
        requestBody.put("model", "Range Rover Outback");

        String jsonString = requestBody.toString();

        ValidatableResponse response = given().contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .body(jsonString).when().post("/cars").then();

        System.out.println("'TestAddCarReturnsErrorWhenPassingAlreadyExistingCarRecordAsParameter()' response:\n" + response.extract().asString());

        response.assertThat().statusCode(HttpStatus.BAD_REQUEST.value()).body("message",
                containsString("Validation failed for object='car'"));
    }
}

