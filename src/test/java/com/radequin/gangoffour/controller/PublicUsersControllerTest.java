package com.radequin.gangoffour.controller;

import com.google.gson.JsonObject;
import com.radequin.gangoffour.GangOfFourApplication;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {GangOfFourApplication.class}, webEnvironment = RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
@Transactional
public class PublicUsersControllerTest {

    @Value("${local.server.port}")
    private int port;

    private static final String BASE_URL = "/public/users";

    @Test
    public void register_success() {
        JsonObject loginCredentials = new JsonObject();
        loginCredentials.addProperty("firstName", "register_test1");
        loginCredentials.addProperty("lastName", "register_test1");
        loginCredentials.addProperty("userName", "register_test1");
        loginCredentials.addProperty("email", "register_test1@test.com");
        loginCredentials.addProperty("password", "test");


        given()
                .accept(ContentType.JSON).
                contentType(ContentType.JSON)
                .body(loginCredentials.toString()).log().ifValidationFails()

                .when()

                .post("http://localhost:" + port + BASE_URL + "/register")

                .then()

                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .body("username", Matchers.equalTo("register_test1"))
                .and()
                .body("password", Matchers.isEmptyOrNullString())
                .and()
                .body("token", not(Matchers.isEmptyOrNullString()));


    }

    @Test
    public void login_success() {
        JsonObject loginCredentials = new JsonObject();
        loginCredentials.addProperty("username", "test1");
        loginCredentials.addProperty("password", "test");


        given()
                .accept(ContentType.JSON).
                contentType(ContentType.JSON)
                .body(loginCredentials.toString()).log().all()

                .when()

                .post("http://localhost:" + port + BASE_URL + "/login")

                .then()

                .log().all()
                .assertThat()
                .statusCode(200);


    }

    @Test
    public void login_fail_invalid() {
        JsonObject loginCredentials = new JsonObject();
        loginCredentials.addProperty("username", "test1");
        loginCredentials.addProperty("password", "invalide");


        given()
                .accept(ContentType.JSON).
                contentType(ContentType.JSON)
                .body(loginCredentials.toString()).log().all()

                .when()

                .post("http://localhost:" + port + BASE_URL + "/login")

                .then()

                .log().all()
                .assertThat()
                .statusCode(401);


    }

    @Test
    public void register_fail_already_exist() {
        JsonObject loginCredentials = new JsonObject();
        loginCredentials.addProperty("firstName", "firstNameTest2");
        loginCredentials.addProperty("lastName", "lastNameTest2");
        loginCredentials.addProperty("userName", "test2");
        loginCredentials.addProperty("email", "test9@test.com2");
        loginCredentials.addProperty("password", "test");
        given()
                .accept(ContentType.JSON).
                contentType(ContentType.JSON)
                .body(loginCredentials.toString()).log().all()

                .when()

                .post("http://localhost:" + port + BASE_URL + "/register")

                .then()

                .log().all()
                .assertThat()
                .statusCode(400)
        ;


    }


}