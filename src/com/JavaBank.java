package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class JavaBank {

    private FileManager fm;
    private int input;
    private UniqueRandomNr urn = new UniqueRandomNr();
    private int searchIndex = 0;
    private List<Path> customerSearchResults = new ArrayList<>();

    private enum FileProperty {
        FIRSTNAME,
        LASTNAME,
        EMAIL,
        SSN
    }

    private enum SearchBy {
        NAME,
        SSN
    }


    JavaBank() {
        fm = new FileManager();
        buildDirectories();
        printMainMenu();

        input = Input.number("Mata in val: ");

        mainSelection();
    }

    private void buildDirectories() {
        try {
            if (!Files.exists(Paths.get("Javabank"))) Files.createDirectory(Paths.get("Javabank"));
            if (!Files.exists(Paths.get("Javabank/Customer"))) Files.createDirectory(Paths.get("Javabank/Customer"));
            if (!Files.exists(Paths.get("Javabank/Account"))) Files.createDirectory(Paths.get("Javabank/Account"));
            if (!Files.exists(Paths.get("Javabank/Staffmembers")))
                Files.createDirectory(Paths.get("Javabank/Staffmembers"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printMainMenu() {
        System.out.println("----------------------------------------");
        System.out.println("Välkommen till Javabanken!");
        System.out.println("----------------------------------------");
        System.out.println("1. Sök kund");
        System.out.println("2. Skapa kund");
        System.out.println("3. Redigera");
        System.out.println("4. Skriv ut Personallista");
        System.out.println("5. Skapa account för customer");
        System.out.println("6. Överföringar");
        System.out.println("0. Avsluta\n");
    }

    private void printSearchMenu() {
        System.out.println("----------------------------------------");
        System.out.println("1. Sök namn"); // return list of customers.
        System.out.println("2. Sök personnummer"); // return list of customers.
        System.out.println("0. Tillbaka\n");
    }

    private void printCustomerOptions() {
        System.out.println("----------------------------------------");
        System.out.println("1. Redigera personlig information");
        System.out.println("2. Redigera konto information"); // return list of accounts.
        System.out.println("0. Tillbaka\n");
    }

    private void printCustomerEditOptions() {
        System.out.println("----------------------------------------");
        System.out.println("1. Redigera förnamn");
        System.out.println("2. Redigera efternamn");
        System.out.println("3. Redigera email");
        System.out.println("0. Tillbaka\n");
    }

    private void printAccountOptions() {
        System.out.println("----------------------------------------");
        System.out.println("1. Redigera saldo");
        System.out.println("2. Redigera skuld");
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
                fm.write("Javabank/Customer/" + UUID.randomUUID() + "-" + customer.getFirstName() + "_" + customer.getLastName() + ".txt", customer.getList());
                Account account = new Account(urn.randomNumber(), 0, 0, customer.getSocialSecurityNumber());
                fm.write("Javabank/Account/" + new Date().getTime() + ".txt", account.getList());
                System.out.println("\nSkapade kund > " + customer.getFirstName() + " " + customer.getLastName() + "\nKonto: " + account.getAccountNumber() + "\n");
                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 3:
                System.out.println("----------------------------------");
                System.out.println("Select 1 to search for customer");
                System.out.println("Select 2 to search for accounts");
                int choice = Input.number("Mata in val: ");
                switch (choice) {
                    case 1:
                        //editCustomerFile();
                        break;
                    case 2:
                        //editAccountFile();
                        break;
                }
                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 4:
                // Print personal register.
                showStaffmembers();
                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 5:
                int number = Input.number("Mata in personnummer: ");
                Account accountForCust = new Account(urn.randomNumber(), 0, 0, number);
                fm.write("Javabank/Account/" + new Date().getTime() + ".txt", accountForCust.getList());
                break;
            case 6:
                Transaction trans = new Transaction();
                ArrayList <Transaction> transactions = new ArrayList<>();
                transactions.add(trans);

                System.out.println("kontonummer" + "\tdatum" + "\t" + "\ttransaktionsumma"   + "\tsaldo");
                for( Transaction t : transactions){
                    System.out.println(t.accountNumber + "\t" + t.date.getYear() +"-"+ t.date.getMonth() +"-" +t.date.getDay() + "\t"+  t.transactionsSum +"\t" +"\t" +"\t"+ "\t"+ t.balance);
                }


                break;
            default:
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
        }
    }


    private void searchSelection() {
        switch (input) {
            case 0:
                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 1:
                customerSearchResults = searchFiles(Input.string("Mata in söktext: "), fm.listFiles("Javabank/Customer"), SearchBy.NAME);
                System.out.println("----------------------------------------");
                for (Path p : customerSearchResults) {
                    System.out.println(++searchIndex + ". " + p);
                }
                System.out.println("0. Tillbaka\n");
                input = Input.number("Mata in val: ");
                break;
            case 2:
                customerSearchResults = searchFiles(Input.string("Mata in söktext: "), fm.listFiles("Javabank/Customer"), SearchBy.SSN);
                System.out.println("----------------------------------------");
                for (Path p : customerSearchResults) {
                    System.out.println(++searchIndex + ". " + p);
                }
                System.out.println("0. Tillbaka\n");
                input = Input.number("Mata in val: ");
                break;
            default:
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                searchSelection();
                break;
        }
    }

    private void customerOptionsSelection() {
        switch (input) {
            case 0:
                printSearchMenu();
                input = Input.number("Mata in val: ");
                searchSelection();
                break;
            case 1:
                // new menu -> edit what? (customer file)
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 2:
                // list accounts
                printAccountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
            default:
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
        }
    }


    private void customerEditOptionsSelection() {
        switch (input) {
            case 0:
                printCustomerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 1:
                // edit first name
                break;
            case 2:
                //  edit last name
                break;
            case 3:
                // edit email
                break;
            default:
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
        }
    }

    private void accountOptionsSelection() {
        switch (input) {
            case 0:
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
            case 1:
                // Redigera saldo
                break;
            case 2:
                // Redigera skuld
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
/*
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
                List<String> deleteAccount = fm.findFile(searchNumber, "account");
                System.out.println(deleteAccount);
                deleteFiles(searchNumber);
                System.out.println("Du har tagit bort fil");
                System.out.println("-------------------------------------------");
                break;
        }

    }

    public void editAccountFile() {
        int num = Input.number("Mata in personnummer : ");
        String searchNumber = String.valueOf(num);

        List<String> searchForSSN = fm.findFile(searchNumber, "account");
        String fileDir = "";
        String ssn = "";
        for (String path : searchForSSN) {
            fileDir = path;
            for (String line : fm.read(path)) {
                System.out.println(line);
                if (line.contains("ssn") || line.contains("accNum")) {
                    ssn = line.substring(4);
                }
            }
        }

        if(fm.read(fileDir).isEmpty()) {
            System.out.println("Invalid search word!");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("Du har " + searchForSSN.size() + " konton");
        int amount = 1;
        for(String path : searchForSSN){
            for(String printAllAccounts : fm.read(path)){
                if (printAllAccounts.contains("accountnumber")) {
                    System.out.println("Konto " + amount + ": " + printAllAccounts);
                    amount++;
                }
            }
        }
        System.out.println("-------------------------------------------");
        System.out.println("Välj 1 för att fortsätta redigera ett konto");
        System.out.println("Välj 2 för att ta bort ett konto");

        int choice = Input.number("Mata in val: ");
        switch (choice) {
            case 1:
                int accNumber = Input.number("Mata in account number: ");
                String newFileDir = getAccFileDir(Integer.toString(accNumber));
                System.out.println("Mata in ny information");
                Account account = new Account(
                        accNumber,
                        Input.number("Mata in account balance: "),
                        Input.number("Mata in debt: "),
                        Integer.valueOf(ssn)
                );
                fm.write(newFileDir, account.getList());
                System.out.println("Din information har nu uppdaterats");
                break;
            case 2:
                int accNumb = Input.number("Mata in account number: ");
                String newFileDirr = getAccFileDir(Integer.toString(accNumb));
                fm.delete(newFileDirr);
                System.out.println("Du har tagit bort konto");
                break;
        }
    }

    public void deleteFiles(String ssn) {
        List<String> files = fm.find(ssn);
        for(String path : files) {
            fm.delete(path);
        }
    }

    public String getAccFileDir(String searchAccNum){
        List<String> getAccNum = fm.findFile(searchAccNum, "account");
        String fileDir = "";
        for (String path : getAccNum) {
            fileDir = path;
        }

        return fileDir;
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

 */


    private void showStaffmembers() {

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

    private List<Path> searchFiles(String searchTerm, List<Path> filesList, Object searchingFor) {
        List<Path> returnPaths = new ArrayList<>();

        Customer customerFile;
        List<String> customerProperties;
        for (Path filePath : filesList) {
            customerProperties = new ArrayList<>();
            for (String line : fm.readData(filePath.toString())) {
                customerProperties.add(line.split(":")[1]);
            }
            customerFile = new Customer(customerProperties.get(0), customerProperties.get(1), customerProperties.get(2), Integer.parseInt(customerProperties.get(3)));
            if (searchingFor == SearchBy.NAME) {
                if (customerFile.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) || customerFile.getLastName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    returnPaths.add(filePath);
                }
            } else if (searchingFor == SearchBy.SSN) {
                if (String.valueOf(customerFile.getSocialSecurityNumber()).toLowerCase().contains(searchTerm.toLowerCase())) {
                    returnPaths.add(filePath);
                }
            }
        }
        return returnPaths;
    }
}




