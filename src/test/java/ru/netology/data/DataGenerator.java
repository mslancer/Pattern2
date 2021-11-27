package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@Data
@NoArgsConstructor

public class DataGenerator {

    //    Настраиваем спек запроса для создания пользователя и выводом логина, пароля и статуса в логи
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

//  Создаем Faker для генерации пользователя

    private static final Faker faker = new Faker(new Locale("en"));

//  Создаем необходимого нам пользователя со сгенерированными данными и необходимым нам статусом
//    Примечание: если приходит 404 Not Found вместо statusCode 200, то не включен тестовый режим SUT

    private static void createUser(Registration.RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

//    C помощью Faker генерим логин пользователя

    public static String getRandomLogin() {
        String login = faker.name().firstName();
        return login;
    }

//    С помощью Faker генерим пароль

    public static String getRandomPassword() {
        String password = faker.internet().password(false);
        return password;
    }

//    Блок регистрации пользователя для передачи данных в тест

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            RegistrationDto user = new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto registeredUser = getUser(status);
            createUser(registeredUser);
            return registeredUser;
        }

        @Value
        public static class RegistrationDto {
            String login;
            String password;
            String status;
        }
    }
}
