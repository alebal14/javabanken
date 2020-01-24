package com;

import java.util.InputMismatchException;
import java.util.Scanner;

abstract public class Input {
    private static Scanner scan = new Scanner(System.in);
    private static String input;

    public static String string(String prompt) {
        System.out.print(prompt);
        try {
            input = scan.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("#invalid input#");
            string(prompt);
        }
        return input;
    }

    public static int number(String prompt) {
        int inputInt = -1;
        boolean everythingIsFine;
        do {
            System.out.print(prompt);
            try {
                input = scan.nextLine();
                inputInt = Integer.parseInt(input);
                everythingIsFine = true;
            } catch (NumberFormatException e) {
                System.out.println("#invalid input#");
                everythingIsFine = false;
            }
        } while (!everythingIsFine);
        return inputInt;
    }

    public static long longNumber(String prompt) {
        long inputLong = -1;
        boolean everythingIsFine;
        do {
            System.out.print(prompt);
            try {
                input = scan.nextLine();
                inputLong = Long.parseLong(input);
                everythingIsFine = true;
            } catch (NumberFormatException e) {
                System.out.println("#invalid input#");
                everythingIsFine = false;
            }
        } while (!everythingIsFine);
        return inputLong;
    }

    public static double floatingNumber(String prompt) {
        double inputDouble = -1;
        boolean everythingIsFine;
        do {
            System.out.print(prompt);
            try {
                input = scan.nextLine();
                inputDouble = Double.parseDouble(input);
                everythingIsFine = true;
            } catch (NumberFormatException e) {
                System.out.println("#invalid input#");
                everythingIsFine = false;
            }
        } while (!everythingIsFine);
        return inputDouble;
    }
}