package ru.netology.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static String getFirstCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getSecondCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getStatusFirstCard() {
        return "APPROVED";
    }

    public static String getStatusSecondCard() {
        return "DECLINED";
    }

    public static String getValidMonth() {
        return "10";
    }

    public static String getValidYear() {
        return "22";
    }

    public static String getValidOwner() {

        return faker.name().fullName();
    }

    public static String getValidCvc() {
        return "123";
    }
    public static String getDoubleSurnameOfTheOwner() {
        return "Ivan Ivanov-Petrov";
    } // двойное имя
    public static String getLessThan16DigitsInTheCard() {
        return "4444 4444 4444 444";
    }
    public static String getNumberOutsideDatabaseInTheCard() {
        return "4444 4444 4444 4443";
    }
    public static String get16ZerosInTheCard() {
        return "0000 0000 0000 0000";
    }
    public static String getLettersAndSpecialSymbolsInTheCard() {
        return "*,/. =-0? апвп hffj";
    }
    public static String getEmptyFieldInTheCard() {
        return "";
    }
    public static String getLettersAndSpecialSymbolsInTheMonth() {
        return "*j";
    }
    public static String getMonthNumberMore12() {
        return "13";
    }
    public static String getMonthNumber00() {
        return "00";
    }
    public static String getMonthFieldEmpty() {
        return "";
    }
    public static String getYearFieldPrevious() {
        return "21";
    }

    public static String getYearFieldNext() {
        return "23";
    }
    public static String getYearMoreThan6YearsOfTheCurrentYear() {
        return "28";
    }
    public static String getYearZero() {
        return "00";
    }
    public static String getLettersAndSpecialSymbolsInTheYear() {
        return "*j";
    }
    public static String getYearFieldEmpty() {
        return "";
    }
    public static String getOnlySurnameInTheOwner() {
        return "Ivanov";
    }
    public static String getSurnameWithSmallLetterInTheOwner() {
        return "ivanov";
    }
    public static String getMoreThan30LatinCharactersInTheOwner() {
        return "Ivanovvanovvanovvanovvanovvanov";
    }
    public static String get1LatinCharacterInTheOwner() {
        return "A";
    }
    public static String getRussianLettersAndNumbersAndSpecialSymbolsInTheOwner() {
        return "апр№,%?8767";
    }
    public static String getOwnerFieldEmpty() {
        return "";
    }

    public static String getCvcZero() {
        return "000";
    }

    public static String getCvcFieldWithTwoDigits() {
        return "12";
    }

    public static String getLettersAndSpecialSymbolsInTheCvc() {
        return "@&П";
    }

    public static String getCvcFieldEmpty() {
        return "";
    }
}