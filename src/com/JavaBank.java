package com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JavaBank {

    FileManager fm = new FileManager();

    JavaBank() {
        buildDirectories();
        printMenu();
        selection();
    }

    private void buildDirectories() {
        try {
            Files.createDirectory(Paths.get("Javabank"));
            Files.createDirectory(Paths.get("Javabank/Customer"));
            Files.createDirectory(Paths.get("Javabank/Account"));
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void printMenu() {
        System.out.println("Välkommen till Javabanken, vad kan vi hjälpa till med?");
        System.out.println("1: Skapa ny kund/konto");
        System.out.println("2: Ändra information om en kund");
        System.out.println("3: Ta bort en kund från Javabanken");
        System.out.println("4: Gör en överföring");
        System.out.println("5: Öppna nytt konto till befintlig kund");
        System.out.println("6: Ändra saldo/skuld");
        System.out.println("0: Avsluta");
        System.out.println();
    }

    private void selection() {
        int selection = Input.number("Mata in val: ");
        switch (selection) {
            case 0:
                break;
            case 1: {
                Customer customer = new Customer(Input.string("Mata in förnamn: "), Input.string("Mata in efternamn: "), Input.string("Mata in E-post adress: "), Input.number("Mata in personnummer: "));
                UniqueRandomNr uniqueRandomNr = new UniqueRandomNr();
                Account account = new Account(uniqueRandomNr.randomNumber(), 0, 0,  customer.getSocialSecurityNumber());
                fm.write("Javabank/Account/"+new Date().getTime()+".txt",account.getList());
                String customerPath = "Javabank/Customer/"+UUID.randomUUID()+"-"+ customer.getFirstName()+ customer.getLastName()+".txt";
                fm.write(customerPath, customer.getList());
                System.out.println("Välkommen till Javabanken!");
                System.out.println("Följande information har lagts till:");
                System.out.println();
                for(String line: fm.read(customerPath)) {
                    System.out.println(line);
                }
                selection();
                break;
            }
            case 2: {
                 List<String> searchword = fm.find(Input.string("Mata in ett sökord: "));
                for(String path:searchword) {
                    for(String line:fm.read(path)) {
                        System.out.println(line);
                    }
                    System.out.println();
                }
                selection();
                break;
            }
            case 3: {
                System.out.println("case 3");
                selection();
                break;
            }
            case 4: {
                System.out.println("case 4");
                selection();
                break;
            }
            case 5: {
                System.out.println("case 5");
                selection();
                break;
            }
            case 6: {
                System.out.println("case 6");
                selection();
                break;
            }
            default:
                System.out.println("#invalid input#");
                selection();
        }
    }
}
