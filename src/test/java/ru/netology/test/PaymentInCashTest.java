package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Description;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.DataHelperSQL;
import ru.netology.page.PaymentPage;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelperSQL.cleanDataBase;

public class PaymentInCashTest {
    PaymentPage paymentPage = new PaymentPage();
    StartPage startPage = new StartPage();

    @BeforeEach
    void CleanDataBaseAndOpenWeb() { //очистить базу данных и открыть веб страницу
        cleanDataBase();
        startPage = open("http://localhost:8080", StartPage.class);
        startPage.buyPaymentByCard();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Description
    //1. Позитивные сценарии
    //1.1_Позитивный тест с первой картой c двойной фамилией владельца
    @Test
    void shouldApproveFirstCardWithDoubleSurnameOfTheOwner() {
        val cardNumber = DataHelper.getFirstCardNumber(); //номер используемой карты
        val month = DataHelper.getValidMonth();
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getDoubleSurnameOfTheOwner(); //тестируемое имя
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment(); //сообщение которое должно быть
        val expected = DataHelper.getStatusFirstCard(); //ОР-какое сообщение банка вывести
        val actual = DataHelperSQL.getPurchaseByDebitCard(); //ФР-покупка дебетовой картой
        assertEquals(expected, actual);
    }

    @Description
    //1.2_Позитивный тест со второй картой c двойной фамилией владельца
    @Test
    void shouldApproveSecondCardWithDoubleSurnameOfTheOwner() {
        val cardNumber = DataHelper.getSecondCardNumber();
        val month = DataHelper.getValidMonth();
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getDoubleSurnameOfTheOwner(); //тестируемое имя
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutUnsuccessfulPaymentRefused();
        val expected = DataHelper.getStatusSecondCard(); //ОР-какое сообщение банка вывести
        val actual = DataHelperSQL.getPurchaseByDebitCard(); //ФР-покупка дебетовой картой
        assertEquals(expected, actual);
    }

    @Description
    //2. Негативные сценарии
    //2.1_Негативный тест с вводом номера карты, содержащую меньше 16 цифр
    @Test
    void shouldLessThan16DigitsInTheCard() { //следует ввести меньше 16 цифр в карте
        val cardNumber = DataHelper.getLessThan16DigitsInTheCard();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.2_Негативный тест с вводом номера карты, содержащее 16 нулей
    @Test
    void should16ZerosInTheCard() { //следует ввести номер карты, содержащий 16 нулей
        val cardNumber = DataHelper.get16ZerosInTheCard();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutUnsuccessfulPaymentRefused(); //сообщение которое должно быть
    }

    @Description
    //2.3_Негативный тест с вводом номера карты, отсутствующим в базе данных
    @Test
    void shouldNumberOutsideDatabaseInTheCard() { //следует ввести номер карты "4444 4444 4444 4443"
        val cardNumber = DataHelper.getNumberOutsideDatabaseInTheCard();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutUnsuccessfulPaymentRefused(); //сообщение которое должно быть
    }

    @Description
  //2.4_Негативный тест с вводом номера карты, содержащий латинские буквы, русские буквы и спецсинволы
    @Test
    void shouldLettersAndSpecialSymbolsInTheCard() { //Следует ввести номер карты, содержащий латинские буквы, русские буквы и спецсинволы
        val cardNumber = DataHelper.getLettersAndSpecialSymbolsInTheCard();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.5_Негативный тест с пустым полем карты
    @Test
    void shouldEmptyFieldInTheCard() { //следует поле карты оставить пустым
        val cardNumber = DataHelper.getEmptyFieldInTheCard();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.6_Негативный тест с вводом месяца, содержащего латинские буквы, русские буквы и спецсинволы
    @Test
    void shouldLettersAndSpecialSymbolsInTheMonth() { //Ввод месяца, содержащий латинские буквы, русские буквы и спецсинволы
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getLettersAndSpecialSymbolsInTheMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.7_Негативный тест с вводом месяца больше 12
    @Test
    void shouldMonthNumberMore12() { //ввод месяца 13
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getMonthNumberMore12(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectCardExpirationDate(); //сообщение которое должно быть
    }

    @Description
    //2.8_Негативный тест с вводом месяца 00 c учетом текущего года
    @Test
    void shouldMonthNumber00WithCurrentYear() { //ввод месяца 00 c учетом текущего года
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getMonthNumber00(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectCardExpirationDate(); //сообщение которое должно быть
    }

    @Description
    //2.9_Негативный тест с вводом месяца 00 c учетом валидного года больше текущего
    @Test
    void shouldMonthNumber00() { //ввод месяца 00 и годом 23
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getMonthNumber00(); //
        val year = DataHelper.getYearFieldNext();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment(); //сообщение которое должно быть
    }

    @Description
    //2.10_Негативный тест с пустым полем месяца
    @Test
    void shouldMonthFieldEmpty() { //пустой месяц
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getMonthFieldEmpty(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.11_Негативный тест с предыдущим годом
    @Test
    void shouldYearFieldPrevious() { //предыдущий год
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getYearFieldPrevious();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutCardExpiration(); //сообщение которое должно быть
    }

    @Description
    //2.12_Негативный тест в поле "Год" ввести год на 6 лет больше текущего года
    @Test
    void shouldYearMoreThan6YearsOfTheCurrentYear() { //в поле "Год" ввести год на 6 лет больше текущего года
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getYearMoreThan6YearsOfTheCurrentYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectCardExpirationDate(); //сообщение которое должно быть
    }

    @Description
    //2.13_Негативный тест в поле "Год" ввести нулевой год
    @Test
    void shouldYearZero() { //в поле "Год" ввести нулевой год
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getYearZero();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutCardExpiration(); //сообщение которое должно быть
    }

    @Description
    //2.14_Негативный тест в поле "Год" ввести латинские буквы, русские буквы и спецсинволы
    @Test
    void shouldLettersAndSpecialSymbolsInTheYear() { //В поле "Год" ввести латинские буквы, русские буквы и спецсинволы
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getLettersAndSpecialSymbolsInTheYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.15_Негативный тест поле "Год" оставить пустым
    @Test
    void shouldYearFieldEmpty() { //поле "Год" оставить пустым
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getYearFieldEmpty();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.16_Негативный тест поле "Владелец" ввести только имя или только фамилию латинскими буквами
    @Test
    void shouldOnlySurnameInTheOwner() { //в поле "Владелец" ввести только фамилию латинскими буквами
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getOnlySurnameInTheOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment(); //сообщение которое должно быть
    }

    @Description
    //2.17_Негативный тест в поле "Владелец" ввести только имя или только фамилию латинскими буквами с маленькой буквы
    @Test
    void shouldSurnameWithSmallLetterInTheOwner() { //в поле "Владелец" ввести только фамилию латинскими буквами с маленькой буквы
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getSurnameWithSmallLetterInTheOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment(); //сообщение которое должно быть
    }

    @Description
    //2.18_Негативный тест в поле "Владелец" ввести более 30 латинских символов
    @Test
    void shouldMoreThan30LatinCharactersInTheOwner() { //в поле "Владелец" ввести 31 латинских символов
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getMoreThan30LatinCharactersInTheOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment(); //сообщение которое должно быть
    }

    @Description
    //2.19_Негативный тест в поле "Владелец" ввести 1 заглавный латинский символ
    @Test
    void should1LatinCharacterInTheOwner() { //в поле "Владелец" ввести латинский символ "A"
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.get1LatinCharacterInTheOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment(); //сообщение которое должно быть
    }

    @Description
    //2.20_Негативный тест в поле "Владелец" ввести русские буквы, числа и спецсинволы
    @Test
    void shouldRussianLettersAndNumbersAndSpecialSymbolsInTheOwner() { //В поле "Владелец" ввести русские буквы, числа и спецсинволы
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getRussianLettersAndNumbersAndSpecialSymbolsInTheOwner();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment(); //сообщение которое должно быть
    }

    @Description
    //2.21_Негативный тест с пустым полем владельца
    @Test
    void shouldOwnerFieldEmpty() { //поле "Год" оставить пустым
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getOwnerFieldEmpty();
        val cvc = DataHelper.getValidCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutTheMandatoryFillingInOfTheField(); //сообщение которое должно быть
    }

    @Description
    //2.22_Негативный тест с нулевым полем CVC
    @Test
    void shouldCvcZero() { //поле "Cvc" ввести нули
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getCvcZero();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutSuccessfulPayment(); //сообщение которое должно быть
    }

    @Description
    //2.23_Негативный тест в поле "Cvc" ввести буквы и спецсимволы
    @Test
    void shouldLettersAndSpecialSymbolsInTheCvc() { //В поле "Cvc" ввести ввести буквы и спецсимволы
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getLettersAndSpecialSymbolsInTheCvc();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.24_Негативный тест с пустым полем Cvc
    @Test
    void shouldCvcFieldEmpty() { //следует поле Cvc оставить пустым
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getCvcFieldEmpty();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }

    @Description
    //2.25_Негативный тест с полем Cvc содержащим менее трех цифр
    @Test
    void shouldCvcFieldWithTwoDigits() { //ввести в поле Cvc 12
        val cardNumber = DataHelper.getFirstCardNumber();
        val month = DataHelper.getValidMonth(); //
        val year = DataHelper.getValidYear();
        val owner = DataHelper.getValidOwner();
        val cvc = DataHelper.getCvcFieldWithTwoDigits();
        paymentPage.fillOutLine(cardNumber, month, year, owner, cvc);
        paymentPage.messageAboutIncorrectDataFormat(); //сообщение которое должно быть
    }
}
