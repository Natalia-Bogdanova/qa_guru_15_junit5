package com.bogdanova;

import com.bogdanova.data.Locale;
import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    @ValueSource(strings =  {"Selenide", "JUnit"}) //test_data
    @ParameterizedTest(name= "Проверка числа результатов поиска в Яндексе для запроса {0}")
    void yandexSearchCommonTest (String testData) {
        open("https://ya.ru");
        $("#text").setValue(testData);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .shouldHave(CollectionCondition.size(10))
                .first()
                .shouldHave(text(testData));
    }

    @CsvSource({
            "Selenide, Мы с гордостью заявляем",
            "JUnit, В этом туториале по JUnit 5"
    }) //test_data
    @ParameterizedTest(name= "Проверка числа результатов поиска в Яндексе для запроса {0}")
    void yandexSearchCommonTestDifferentExpectedText (String searchQuery, String expectedText) {
        open("https://ya.ru");
        $("#text").setValue(searchQuery);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .shouldHave(CollectionCondition.size(10))
                .first()
                .shouldHave(text(expectedText));
    }

    static Stream<Arguments> selenideSiteButtonText() {
        return Stream.of(
                Arguments.of(Locale.EN, List.of("Quick start", "Docs", "FAQ", "Blog", "Javadoc", "Users", "Quotes")),
                Arguments.of(Locale.RU, List.of("С чего начать?", "Док", "ЧАВО", "Блог", "Javadoc", "Пользователи", "Отзывы"))
        );
    }

    @MethodSource()
    @ParameterizedTest(name = "Проверка отображения кнопок для локали: {0}")
    void selenideSiteButtonText(Locale locale, List<String> buttonsTexts) {
        open("https://ru.selenide.org");
        $$("#languages a").find(text(locale.name())).click();
        $$(".main-menu-pages a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }
}
