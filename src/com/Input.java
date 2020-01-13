package com;

import java.util.InputMismatchException;
import java.util.Scanner;

// 10:20 start
// 10:40 stop
// 10:45 start
// 11:00 done?
abstract public class Input {
    private static Scanner scan  = new Scanner(System.in);
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
        System.out.print(prompt);
        try {
            input = scan.nextLine();
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("#invalid input#");
                number(prompt);
            }
        } catch (InputMismatchException e) {
            System.out.println("#invalid input#");
            number(prompt);
        }

        return Integer.parseInt(input);
    }
}
