package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

class AuthTest {


    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }


    @Test
    @DisplayName("Success authorisation")
    void shouldSuccessAuth() {
        val registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] .input__box .input__control[name=login]").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__box .input__control[name=password]").setValue(registeredUser.getPassword());
        $(byText("Продолжить")).click();
        $(withText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Blocked user")
    void shouldBlockedUser() {
        val registeredUser = getRegisteredUser("blocked");
        $("[data-test-id=login] .input__box .input__control[name=login]").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__box .input__control[name=password]").setValue(registeredUser.getPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Wrong password")
    void shouldIncorrectPassword() {
        val registeredUser = getUser("blocked");
        val wrongPassword = getRandomPassword();
        $("[data-test-id=login] .input__box .input__control[name=login]").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__box .input__control[name=password]").setValue(wrongPassword);
        $(byText("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Wrong login")
    void shouldIncorrectLogin() {
        val registeredUser = getUser("blocked");
        val wrongLogin = getRandomLogin();
        $("[data-test-id=login] .input__box .input__control[name=login]").setValue(wrongLogin);
        $("[data-test-id=password] .input__box .input__control[name=password]").setValue(registeredUser.getPassword());
        $(byText("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}

