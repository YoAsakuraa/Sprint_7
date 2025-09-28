package testClass;

import java.util.UUID;

public class CourierTestData {
    private static String lastLogin;
    private static String lastPassword;
    private static String lastFirstName;

    private CourierTestData() {
        // Защита от случайного создания экземпляра
    }

    public static String generateUniqueBody() {
        lastLogin = "LoginTest_" + UUID.randomUUID().toString().substring(0, 8);
        lastPassword = "PasswordTest_" + UUID.randomUUID().toString().substring(0, 8);
        lastFirstName = "FirstNameTest_" + UUID.randomUUID().toString().substring(0, 8);

        return "{\"login\":\"" + lastLogin + "\",\"password\":\"" + lastPassword + "\",\"firstName\":\"" + lastFirstName + "\"}";
    }

    public static String generateBodyWithoutPassword() {
        lastLogin = "LoginTest_" + UUID.randomUUID().toString().substring(0, 8);
        lastPassword = null; // пароль отсутствует
        lastFirstName = "FirstNameTest_" + UUID.randomUUID().toString().substring(0, 8);

        return "{\"login\":\"" + lastLogin + "\",\"firstName\":\"" + lastFirstName + "\"}";
    }

    public static String generateBodyWithoutLogin() {
        lastLogin = null; // логин отсутствует
        lastPassword = "PasswordTest_" + UUID.randomUUID().toString().substring(0, 8);
        lastFirstName = "FirstNameTest_" + UUID.randomUUID().toString().substring(0, 8);

        return "{\"password\":\"" + lastPassword + "\",\"firstName\":\"" + lastFirstName + "\"}";
    }

    public static String generateBodyWithoutFirstName() {
        lastLogin = "LoginTest_" + UUID.randomUUID().toString().substring(0, 8);
        lastPassword = "PasswordTest_" + UUID.randomUUID().toString().substring(0, 8);
        lastFirstName = null; // имя отсутствует

        return "{\"login\":\"" + lastLogin + "\",\"password\":\"" + lastPassword + "\"}";
    }

    public static String generateUniqueLogin() {
        lastLogin = "LoginTest_" + UUID.randomUUID().toString().substring(0, 8);
        return lastLogin;
    }

    public static String generateUniquePassword() {
        lastPassword = "PasswordTest_" + UUID.randomUUID().toString().substring(0, 8);
        return lastPassword;
    }

    public static String generateUniqueFirstName() {
        lastFirstName = "FirstNameTest_" + UUID.randomUUID().toString().substring(0, 8);
        return lastFirstName;
    }

    public static String getLastLogin() {
        return lastLogin;
    }

    public static String getLastPassword() {
        return lastPassword;
    }

    public static String getLastFirstName() {
        return lastFirstName;
    }
}