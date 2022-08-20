package ru.netology.generator;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {        // public class UserRegistrtionDataGenerator {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final static String activeStatus = "active";
    private final static String blockedStatus = "blocked";
    private static Gson gson = new Gson();
    private static Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void registrationUsers(RegistrationData userData) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(userData))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static RegistrationData generateValidActive() {
        RegistrationData regData = new RegistrationData(faker.name().username(),
                faker.internet().password(true), activeStatus);
        registrationUsers(regData);
        return regData;
    }

    public static RegistrationData generateValidBlocked() {
        RegistrationData regData = new RegistrationData(faker.name().username(),
                faker.internet().password(true), blockedStatus);
        registrationUsers(regData);
        return regData;
    }

    public static RegistrationData generateInvalidLogin() {
        String password = faker.internet().password(true);
        RegistrationData regData = new RegistrationData(faker.name().username(),
                password, activeStatus);
        registrationUsers(regData);
        return new RegistrationData(faker.name().username(),
                password, activeStatus);
    }

    public static RegistrationData generateInvalidPassword() {
        String login = faker.name().username();
        RegistrationData regData = new RegistrationData(login,
                faker.internet().password(true), activeStatus);
        registrationUsers(regData);
        return new RegistrationData(login,
                faker.internet().password(true), activeStatus);
    }

    @Value
    public static class RegistrationData {
        String login;
        String password;
        String status;
    }
}