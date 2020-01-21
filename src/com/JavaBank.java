package com;

import com.sun.source.tree.ClassTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        System.out.println("7: Sök Kund");
        System.out.println("0: Avsluta");
        System.out.println();
    }

    private void selection() {
        int selection = Input.number("Mata in val: ");
        switch (selection) {
            case 0:
                break;
            case 1: {
                Customer customer = createCustomer();
                UniqueRandomNr uniqueRandomNr = new UniqueRandomNr();
                Account account = new Account(uniqueRandomNr.randomNumber(), 0, 0, customer.getSocialSecurityNumber());
                fm.write("Javabank/Account/" + new Date().getTime() + ".txt", account.getList());
                String customerPath = "Javabank/Customer/" + UUID.randomUUID() + "-" + customer.getFirstName() + customer.getLastName() + ".txt";
                fm.write(customerPath, customer.getList());
                System.out.println("Välkommen till Javabanken!");
                System.out.println("Följande information har lagts till:");
                System.out.println();
                for (String line : fm.read(customerPath)) {
                    System.out.println(line);
                }
                selection();
                break;
            }
            case 2: {
                System.out.println("----------------------------------");
                System.out.println("Select 1 to search for customer");
                System.out.println("Select 2 to search for accounts");
                int choice = Input.number("Mata in val: ");
                switch (choice) {
                    case 1:
                        editCustomerFile();
                        break;
                    case 2:
                        editAccountFile();
                        break;
                }
                selection();
                break;
            }
            case 3: {
                searchSSN();
                selection();
                break;
            }
            case 4: {
                searchSSN();
                selection();
                break;
            }
            case 5: {
                searchSSN();
                selection();
                break;
            }
            case 6: {
                searchSSN();
                selection();
                break;
            }
            case 7: {

                searchNames();
                break;
            }
            default: {
                System.out.println("#invalid input#");
                selection();
            }
        }
    }

    public Customer createCustomer() {
        String firstName;
        do {
            firstName = Input.string("Mata in förnamn: ");
        } while (!validateName(firstName));

        String lastName;
        do {
            lastName = Input.string("Mata in efternamn: ");
        } while (!validateName(lastName));

        String email;
        do {
            email = Input.string("Mata in E-post adress: ");
        } while (!validateEmail(email));

        int ssn;
        do {
            ssn = Input.number("Mata in personnummer: ");
        } while (String.valueOf(ssn).length() != 10 && String.valueOf(ssn).length() != 12);

        return new Customer(firstName, lastName, email, ssn);
    }

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"; // Magi
        return email.matches(regex);
    }

    private boolean validateName(String name) {
        return name.matches("[A-Ö][a-ö]*"); // Förstår ungefär
    }

    public void editCustomerFile() {
        int num = Input.number("Mata in personnummer : ");
        String searchNumber = String.valueOf(num);

        List<String> searchForSSN = fm.findFile(searchNumber, "customer");
        String fileDir = "";
        String ssn = "";
        for (String path : searchForSSN) {
            fileDir = path;
            for (String line : fm.read(path)) {
                System.out.println(line);
                if (line.contains("ssn")) {
                    ssn = line.substring(4);
                }
            }
        }

        if(fm.read(fileDir).isEmpty()) {
            System.out.println("Invalid search word!");
            return;
        }
        System.out.println("-------------------------------------------");
        System.out.println("Välj 1 för att fortsätta redigera fil");
        System.out.println("Välj 2 för att ta bort fil");
        int choice = Input.number("Mata in val: ");
        switch (choice) {
            case 1:
                System.out.println("Mata in ny information");
                Customer customer = new Customer(
                        Input.string("Mata in förnamn: "),
                        Input.string("Mata in efternamn: "),
                        Input.string("Mata in E-post adress: "),
                        Integer.valueOf(ssn)
                );
                fm.write(fileDir, customer.getList());
                System.out.println("Din information har nu uppdaterats");
                break;
            case 2:
                deleteFiles(fileDir);
                break;
        }

    }

    public void editAccountFile() {
        int num = Input.number("Mata in personnummer : ");
        String searchNumber = String.valueOf(num);

        List<String> searchForSSN = fm.findFile(searchNumber, "account");
        String fileDir = "";
        String ssn = "";
        String accNum = "";
        for (String path : searchForSSN) {
            fileDir = path;
            for (String line : fm.read(path)) {
                System.out.println(line);
                if (line.contains("ssn") || line.contains("accNum")) {
                    ssn = line.substring(4);
                    accNum = line.substring(13);
                }
            }
        }

        if(fm.read(fileDir).isEmpty()) {
            System.out.println("Invalid search word!");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("Du har " + searchForSSN.size() + " konton");
        System.out.println("Välj 1 för att fortsätta redigera fil");
        System.out.println("Välj 2 för att ta bort fil");

        int choice = Input.number("Mata in val: ");
        switch (choice) {
            case 1:
                System.out.println("Mata in ny information");
                Account account = new Account(
                        Integer.valueOf(accNum),
                        Input.number("Mata in account balanace: "),
                        Input.number("Mata in debt: "),
                        Integer.valueOf(ssn)
                );
                fm.write(fileDir, account.getList());
                System.out.println("Din information har nu uppdaterats");
                break;
            case 2:
                deleteFiles(fileDir);
                break;
        }
    }

    public void deleteFiles(String fileDir) {
        fm.delete(fileDir);
    }

    //testat refaktorisera search method
    /*public void betterSearchForSSN(int input, String typeFile) {
        String searchNumber = String.valueOf(input);
        List<String> searchForSSN = new List<String>();
        if (typeFile.equals("customer")) {
            searchForSSN = fm.findFile(searchNumber, "customer");
        } else if (typeFile.equals("account")) {
            searchForSSN = fm.findFile(searchNumber, "account");
        }

        String fileDir = "";
        String ssn = "";
        String accNum = "";
        for (String path : searchForSSN) {
            fileDir = path;
            for (String line : fm.read(path)) {
                System.out.println(line);
                if (line.contains("ssn")) {
                    ssn = line.substring(4);
                }

                if (typeFile.equals("account") && (line.contains("ssn") || line.contains("accNum"))) {
                    ssn = line.substring(4);
                    accNum = line.substring(13);
                }
            }
        }
    }*/

        public void searchSSN () {
            int num = Input.number("Mata in ett personnummer : ");
            String searchnumber = String.valueOf(num);

            List<String> searchNumbermethod = fm.find(searchnumber);
            for(String path:searchNumbermethod) {
                for(String line:fm.read(path)) {
                    System.out.println(line);
                }
                System.out.println();
            }
        }

        public void searchNames () {
            List<String> searchword = fm.find(Input.string("Mata in ett sökord: "));
            for (String path : searchword) {
                for (String line : fm.read(path)) {
                    System.out.println(line);
                }
                System.out.println();
            }
        }

    }




