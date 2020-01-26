package com;

public class Validate {
    public static int inputRange(int range) {
        int input;
        input = Input.number("Mata in val: ");
        while (input < 0 || input > range) {
            System.out.println("#Input out of range#");
            input = Input.number("Mata in val: ");
        }
        return input;
    }

    public static boolean email(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static boolean name(String name) {
        return name.matches("[A-Z][a-z]*");
    }
}
