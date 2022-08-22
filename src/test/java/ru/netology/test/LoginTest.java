package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.generator.DataGenerator.Registration.generateUser;
import static ru.netology.generator.DataGenerator.Registration.registerUser;
import static ru.netology.generator.DataGenerator.generateLogin;
import static ru.netology.generator.DataGenerator.generatePassword;

public class LoginTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    void shouldShowLoginIn() {
        var registeredUser = registerUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $(".heading").shouldHave(Condition.text("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void shouldShowWrongLogin() {
        var registeredUser = registerUser("active");
        var wrongLogin = generateLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowWrongPassword() {
        var registeredUser = registerUser("active");
        var wrongPassword = generatePassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowNotLogin() {
        var registeredUser = registerUser("active");
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=login]").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowNotPassword() {
        var registeredUser = registerUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=password]").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowBlockedUser() {
        var blockedUser = registerUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = generateUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(text("Неверно указан логин или пароль"));
    }
}




