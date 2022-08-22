package ru.netology.generator;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void registrationUsers(RegistrationData userData) {
        given()
                .spec(requestSpec)
                .body(new RegistrationData(
                        userData.getLogin(),
                        userData.getPassword(),
                        userData.getStatus()))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String generateLogin() {
        return faker.name().firstName();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationData generateUser(String status) {
            return new RegistrationData(generateLogin(), generatePassword(), status);
        }

        public static RegistrationData registerUser(String status) {
            RegistrationData registerUser = generateUser(status);
            registrationUsers(registerUser);
            return registerUser;
        }
    }

    @Value
    public static class RegistrationData {
        String login;
        String password;
        String status;
    }
}