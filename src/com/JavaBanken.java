package com;

import java.util.Scanner;

public class JavaBanken {
    JavaBanken() {
        System.out.println("Välkommen till Javabanken, vad kan vi hjälpa till med?");
        System.out.println("1: Skapa ny kund/konto");
        System.out.println("2: Ändra information om en kund");
        System.out.println("3: Ta bort en kund från Javabanken");
        System.out.println("4: Gör en överföring");
        System.out.println("5: Öppna nytt konto till befintlig kund");
        System.out.println("6: Ändra saldo/skuld");

        Scanner scan = new Scanner(System.in);
        int number = scan.nextInt();
        switch (number) {
            case 1: {
                break;
            }
            case 2: {
                break;
            }
            case 3: {

                break;
            }
            case 4: {
                break;

            }
            case 5: {
                break;

            }
            case 6: {
                break;

            }
            default:

        }
    }

}
