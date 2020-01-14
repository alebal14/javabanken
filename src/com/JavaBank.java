package com;

import java.util.Date;
import java.util.UUID;

public class JavaBank {

    FileManager fm = new FileManager();

    JavaBank() {
        System.out.println("Välkommen till Javabanken, vad kan vi hjälpa till med?");
        System.out.println("1: Skapa ny kund/konto");
        System.out.println("2: Ändra information om en kund");
        System.out.println("3: Ta bort en kund från Javabanken");
        System.out.println("4: Gör en överföring");
        System.out.println("5: Öppna nytt konto till befintlig kund");
        System.out.println("6: Ändra saldo/skuld");

        int number = Input.number("Mata in val: ");
        switch (number) {
            case 1: {
                Account account = new Account(12345, 0, 0 );
                fm.write(new Date().getTime()+".txt",account.getList());
                Customer customer = new Customer(Input.string("Mata in förnamn: "), Input.string("Mata in efternamn: "), Input.string("Mata in E-post adress: "), Input.number("Mata in personnummer: "));
                fm.write(UUID.randomUUID()+"-"+ customer.getFirstName()+ customer.getLastName()+".txt", customer.getList());
                break;
            }
            case 2: {
                System.out.println("case 2");
                break;
            }
            case 3: {
                System.out.println("case 3");
                break;
            }
            case 4: {
                System.out.println("case 4");
                break;
            }
            case 5: {
                System.out.println("case 5");
                break;
            }
            case 6: {
                System.out.println("case 6");
                break;
            }
            default:

        }
    }

}
