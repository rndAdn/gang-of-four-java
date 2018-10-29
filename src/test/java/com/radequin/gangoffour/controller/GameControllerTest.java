package com.radequin.gangoffour.controller;

import com.google.gson.JsonObject;
import com.radequin.gangoffour.GangOfFourApplication;
import com.radequin.gangoffour.controller.dto.UserLoginDTO;
import com.radequin.gangoffour.domain.GameStatus;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {GangOfFourApplication.class}, webEnvironment = RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
@Transactional
public class GameControllerTest {

    @Value("${local.server.port}")
    private int port;

    private static final String BASE_URL = "/game";

    UserLoginDTO getTokenKey(String usrername, String password) {
        JsonObject loginCredentials = new JsonObject();
        loginCredentials.addProperty("username", usrername);
        loginCredentials.addProperty("password", password);

        Response response =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .body(loginCredentials.toString())
                        .when()
                        .post("http://localhost:" + port + "/public/users/login")
                        .then()
                        .extract().response();

        UserLoginDTO user = new UserLoginDTO();
        user.setId(new Long("" + response.path("id")));
        user.setPassword(response.path("password"));
        user.setToken(response.path("token"));
        user.setUsername(response.path("username"));

        return user;
    }

    @Test
    public void create_newGame_fails_auth() {
        given()
                .header("Authorization", "Bearer fails")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + port + BASE_URL + "/create/user/42")
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(401);
    }

    @Test
    public void create_newGame_succces() {
        UserLoginDTO user = getTokenKey("test1", "test");

        given()
                .header("Authorization", "Bearer " + user.getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + port + BASE_URL + "/create/user/" + user.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(201);
    }


    @Test
    public void get_game_fails_auth() {
        given()
                .header("Authorization", "Bearer fails")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + port + BASE_URL + "/42/player/42")
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(401);
    }

    @Test
    public void get_game_succces() {
        UserLoginDTO user = getTokenKey("test1", "test");
        given()
                .header("Authorization", "Bearer " + user.getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:" + port + BASE_URL + "/1/player/" + user.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void get_game_player_fails_auth() {
        given()
                .header("Authorization", "Bearer fails")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + port + BASE_URL + "/games/player/42")
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(401);
    }

    @Test
    public void get_game_player_succces() {
        UserLoginDTO user = getTokenKey("test1", "test");
        given()
                .header("Authorization", "Bearer " + user.getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:" + port + BASE_URL + "/games/player/" + user.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }


    @Test
    public void join_2_success() {
        UserLoginDTO user = getTokenKey("test4", "test");

        given()
                .header("Authorization", "Bearer " + user.getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + port + BASE_URL + "/join/1/user/" + user.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .body("id", Matchers.equalTo(1))
                .body("gameStatus", Matchers.equalToIgnoringCase(GameStatus.WAITS_FOR_PLAYER.getValue()))
                .body("nextPlayer", Matchers.notNullValue())
                .body("players[0].cardLeft", Matchers.equalTo(0))
                .body("players[1].cardLeft", Matchers.equalTo(0))
        ;
    }


    @Test
    public void join_full_success() {
        UserLoginDTO user = getTokenKey("test4", "test");

        given()
                .header("Authorization", "Bearer " + user.getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + port + BASE_URL + "/join/2/user/" + user.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .body("id", Matchers.equalTo(2))
                .body("gameStatus", Matchers.equalToIgnoringCase(GameStatus.IN_PROGRESS.getValue()))
                .body("nextPlayer", Matchers.notNullValue())
                .body("players[0].cardLeft", Matchers.equalTo(16))
                .body("players[1].cardLeft", Matchers.equalTo(16))
                .body("players[2].cardLeft", Matchers.equalTo(16))
                .body("players[3].cardLeft", Matchers.equalTo(16))
        ;
    }

    @Test
    public void join_already_full_fail() {
        UserLoginDTO user = getTokenKey("test4", "test");

        given()
                .header("Authorization", "Bearer " + user.getToken())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + port + BASE_URL + "/join/3/user/" + user.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
        ;
    }

    @Test
    public void play_move_auth_fail() {
        given()
                .header("Authorization", "Bearer fails")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:" + port + BASE_URL + "/3/player/42/move")
                .then()
                .log().ifValidationFails()
                .assertThat()
                .statusCode(401);
    }
}