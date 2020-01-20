package com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JavaBank {

    private FileManager fm;
    private int input;
    private UniqueRandomNr urn = new UniqueRandomNr();

    JavaBank() {
        fm = new FileManager();
        buildDirectories();
        printMainMenu();

        input = Input.number("Mata in val: ");

        mainSelection();
    }

    private void buildDirectories() {
        try {
            Files.createDirectory(Paths.get("Javabank"));
            Files.createDirectory(Paths.get("Javabank/Customer"));
            Files.createDirectory(Paths.get("Javabank/Account"));
            Files.createDirectory(Paths.get("Javabank/Personell"));
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void printMainMenu() {
        System.out.println("Välkommen till Javabanken!");
        System.out.println("----------------------------------------");
        System.out.println("1. Sök kund");
        System.out.println("2. Skapa kund");
        System.out.println("3. Personal");
        System.out.println("666. Redigera kund");
        System.out.println("0. Avsluta\n");
    }
    private void printSearchMenu() {
        System.out.println("----------------------------------------");
        System.out.println("1. Sök namn");
        System.out.println("2. Sök personnummer");
        System.out.println("0. Tillbaka\n");
    }

    private void mainSelection() {
        switch (input) {
            case 0:
                break;
            case 1:
                printSearchMenu();
                input = Input.number("Mata in val: ");
                searchSelection();
                break;
            case 2:
                Customer customer = createCustomer();
                fm.write("Javabank/Customer/"+ UUID.randomUUID()+"-"+customer.getFirstName()+"_"+customer.getLastName()+".txt", customer.getList());

                Account account = new Account(urn.randomNumber(), 0, 0, customer.getSocialSecurityNumber());
                fm.write("Javabank/Account/"+new Date().getTime()+".txt", account.getList());

                System.out.println("Skapade kund > "+customer.getFirstName()+" "+customer.getLastName()+"\nmed konto: "+account.getAccountNumber());

                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 3:
                // Print personell register.
                break;
            case 666:
                editCustomerFile();
                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            default:
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                break;
        }
        /*
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
            default: {
                System.out.println("#invalid input#");
                selection();
            }
        }*/
    }

    private void searchSelection() {
        switch (input) {
            case 0:
                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 1:
                // Search customer by name.
                break;
            case 2:
                // Search customer by ssn.
                break;
            default:

                break;
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

        if(fm.read(fileDir).isEmpty()) {
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




