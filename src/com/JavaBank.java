package com;

import com.sun.source.tree.ClassTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Stream;

public class JavaBank {

    FileManager fm = new FileManager();

    JavaBank() throws Exception {
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
        System.out.println("8: Visa information om personal");
        System.out.println("0: Avsluta");
        System.out.println();
    }

    private void selection() throws Exception {
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
                editCustomerFile();
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
            case 8: {
                showStaffmembers();
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
        List<String> searchword = fm.find(Input.string("Mata in ett sökord: "));
        String fileDir = "";
        String ssn = "";
        for (String path : searchword) {
            fileDir = path;
            for (String line : fm.read(path)) {
                System.out.println(line);
                if (line.contains("ssn")) {
                    ssn = line.substring(4);
                }
            }
        }

        if (fm.read(fileDir).isEmpty()) {
            System.out.println("Invalid search word!");
            return;
        }

        System.out.println();
        System.out.println("Mata in ny information");

        Customer customer = new Customer(
                Input.string("Mata in förnamn: "),
                Input.string("Mata in efternamn: "),
                Input.string("Mata in E-post adress: "),
                Integer.valueOf(ssn)
        );
        fm.write(fileDir, customer.getList());
        System.out.println("Din information har nu uppdaterats");
    }

    public void searchSSN() {
        int num = Input.number("Mata in ett personnummer : ");
        String searchnumber = String.valueOf(num);

        List<String> searchNumbermethod = fm.find(searchnumber);
        for (String path : searchNumbermethod) {
            for (String line : fm.read(path)) {
                System.out.println(line);
            }
            System.out.println();
        }
    }

    public void searchNames() {
        List<String> searchword = fm.find(Input.string("Mata in ett sökord: "));
        for (String path : searchword) {
            for (String line : fm.read(path)) {
                System.out.println(line);
            }
            System.out.println();
        }
    }

    public void showStaffmembers() throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "Javabank/Staffmembers/Staffmembers.txt"));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}














