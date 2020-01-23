package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class JavaBank {

    private FileManager fm;
    private int input;
    private UniqueRandomNr urn = new UniqueRandomNr();
    private int searchIndex = 0;
    private List<Path> customerSearchResults = new ArrayList<>();

    private List<Path> accountSearchResults = new ArrayList<>();
    private Customer selectedCustomer;
    private Path selectedCustomerPath;
    private Account selectedAccount;
    private Path selectedAccountPath;

    private enum FileProperty {
        FIRSTNAME,
        LASTNAME,
        EMAIL,
        BALANCE,
        DEBT
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
        System.out.println("3. Personal");
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
        System.out.println("1. Visa Information");
        System.out.println("2. Skapa nytt konto");
        System.out.println("3. Redigera personlig information");
        System.out.println("4. Redigera konto information"); // return list of accounts.
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
        System.out.println("1. Visa Information");
        System.out.println("2. Redigera saldo");
        System.out.println("3. Redigera skuld");
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
                Account account = createAccount(customer, 0, 0);
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
                ArrayList<Transaction> transactions = new ArrayList<>();
                transactions.add(trans);

                System.out.println("kontonummer" + "\tdatum" + "\t" + "\ttransaktionsumma" + "\tsaldo");
                for (Transaction t : transactions) {
                    System.out.println(t.accountNumber + "\t" + t.date.getYear() + "-" + t.date.getMonth() + "-" + t.date.getDay() + "\t" + t.transactionsSum + "\t" + "\t" + "\t" + "\t" + t.balance);
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
                validateInput(customerSearchResults.size());
                selectCustomer();
                break;
            case 2:
                customerSearchResults = searchFiles(Input.string("Mata in siffror: "), fm.listFiles("Javabank/Customer"), SearchBy.SSN);
                System.out.println("----------------------------------------");

                for (Path p : customerSearchResults) {
                    System.out.println(++searchIndex + ". " + p);
                }
                System.out.println("0. Tillbaka\n");
                input = Input.number("Mata in val: ");
                validateInput(customerSearchResults.size());
                selectCustomer();

                break;
            default:
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                searchSelection();
                break;
        }
    }

    private void selectCustomer() {
        searchIndex = 0;
        if (input == 0) {
            printSearchMenu();
            input = Input.number("Mata in val: ");
            searchSelection();
        } else if (input > 0 && input <= customerSearchResults.size()) {
            selectedCustomerPath = customerSearchResults.get(input - 1);
            List<String> customerProperties = new ArrayList<>();
            for (String property : fm.readData(customerSearchResults.get(input - 1).toString())) {
                customerProperties.add(property.split(":")[1]);
            }
            selectedCustomer = new Customer(customerProperties.get(0), customerProperties.get(1), customerProperties.get(2), Integer.parseInt(customerProperties.get(3)));
            printCustomerOptions();
            input = Input.number("Mata in val: ");
            customerOptionsSelection();
        }
    }

    private void selectAccount() {
        searchIndex = 0;
        if (input == 0) {
            printSearchMenu();
            input = Input.number("Mata in val: ");
            searchSelection();
        } else if (input > 0 && input <= accountSearchResults.size()) {
            selectedAccountPath = accountSearchResults.get(input - 1);
            List<String> accountProperties = new ArrayList<>();
            for (String property : fm.readData(accountSearchResults.get(input - 1).toString())) {
                accountProperties.add(property.split(":")[1]);
            }
            selectedAccount = new Account(Integer.parseInt(accountProperties.get(0)), Double.parseDouble(accountProperties.get(1)), Double.parseDouble(accountProperties.get(2)), Integer.parseInt(accountProperties.get(3)));
            printAccountOptions();
            input = Input.number("Mata in val: ");
            accountOptionsSelection();
        }
    }

    private void validateInput(int range) {
        while (input < 0 || input > range) {
            System.out.println("#invalid input#");
            input = Input.number("Mata in val: ");
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
                System.out.println("\nNamn: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
                System.out.println("Email: " + selectedCustomer.getEmail());
                System.out.println("Personnummer: " + selectedCustomer.getSocialSecurityNumber());
                System.out.println("0. Tillbaka\n");
                input = Input.number("Mata in val: ");
                validateInput(0);
                printCustomerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 2:
                createAccount(selectedCustomer, 0, 0);
                System.out.println("Nytt konto skapat");
                printCustomerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 3:
                // new menu -> edit what? (customer file)
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 4:
                searchIndex = 0;
                List<String> accountData;
                for (Path accPath : fm.listFiles("Javabank/Account")) {
                    accountData = new ArrayList<>();
                    for (String line : fm.readData(accPath.toString())) {
                        accountData.add(line.split(":")[1]);
                    }
                    if (accountData.get(3).equals(String.valueOf(selectedCustomer.getSocialSecurityNumber()))) {
                        accountSearchResults.add(accPath);
                        System.out.println(++searchIndex + ". " + accPath);
                    }
                }
                System.out.println("0. Tillbaka\n");
                input = Input.number("Mata in val: ");
                validateInput(accountSearchResults.size());
                selectAccount();
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
                editFile(FileProperty.FIRSTNAME);
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 2:
                editFile(FileProperty.LASTNAME);
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 3:
                editFile(FileProperty.EMAIL);
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
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
                printCustomerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 1:
                System.out.println("\nAccount Number: " + selectedAccount.getAccountNumber());
                System.out.println("Account Balance: " + selectedAccount.getAccountBalance());
                System.out.println("Debt: " + selectedAccount.getDebt());
                System.out.println("0. Tillbaka\n");
                input = Input.number("Mata in val: ");
                validateInput(0);
                printAccountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
            case 2:
                editFile(FileProperty.BALANCE);
                printAccountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;

            case 3:
                editFile(FileProperty.DEBT);
                printAccountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
            default:
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
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

        Customer customer = new Customer(firstName, lastName, email, ssn);
        fm.write("Javabank/Customer/" + UUID.randomUUID() + "-" + customer.getFirstName() + "_" + customer.getLastName() + ".txt", customer.getList());
        return customer;
    }

    private Account createAccount(Customer customer, int balance, int debt) {
        Account account = new Account(urn.randomNumber(), balance, debt, customer.getSocialSecurityNumber());
        fm.write("Javabank/Account/" + new Date().getTime() + ".txt", account.getList());
        return account;
    }

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"; // Magi
        return email.matches(regex);
    }

    private boolean validateName(String name) {
        return name.matches("[A-Ö][a-ö]*"); // Förstår ungefär
    }

    private void editFile(FileProperty fileProperty) {
        if (fileProperty == FileProperty.FIRSTNAME) {
            fm.delete(selectedCustomerPath.toString());
            List<String> splitPath = Arrays.asList(selectedCustomerPath.toString().split(selectedCustomer.getFirstName()));
            selectedCustomer.setFirstName(Input.string("Mata in nytt förnamn: "));
            fm.write(splitPath.get(0) + selectedCustomer.getFirstName() + splitPath.get(1), selectedCustomer.getList());
        }
        if (fileProperty == FileProperty.LASTNAME) {
            fm.delete(selectedCustomerPath.toString());
            List<String> splitPath = Arrays.asList(selectedCustomerPath.toString().split(selectedCustomer.getLastName()));
            selectedCustomer.setLastName(Input.string("Mata in nytt efternamn: "));
            fm.write(splitPath.get(0) + selectedCustomer.getLastName() + splitPath.get(1), selectedCustomer.getList());
        }
        if (fileProperty == FileProperty.EMAIL) {
            String newEmail = Input.string("Mata in ny email: ");
            while (!validateEmail(newEmail)) {
                System.out.println("#invalid email#");
                newEmail = Input.string("Mata in ny email: ");
            }
            selectedCustomer.setEmail(newEmail);
            fm.write(selectedCustomerPath.toString(), selectedCustomer.getList());
        }
        if (fileProperty == FileProperty.BALANCE) {
            selectedAccount.setAccountBalance(Input.floatingNumber("Mata in nytt saldo: "));
            fm.write(selectedAccountPath.toString(), selectedAccount.getList());
        }
        if (fileProperty == FileProperty.DEBT) {
            selectedAccount.setDebt(Input.floatingNumber("Mata in ny skuld: "));
            fm.write(selectedAccountPath.toString(), selectedAccount.getList());
        }
    }


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

    private List<Path> searchFiles(String searchTerm, List<Path> filesList, SearchBy searchBy) {
        List<Path> returnPaths = new ArrayList<>();

        Customer customerFile;
        List<String> customerProperties;

        for (Path filePath : filesList) {
            customerProperties = new ArrayList<>();
            for (String line : fm.readData(filePath.toString())) {
                customerProperties.add(line.split(":")[1]);
            }
            customerFile = new Customer(customerProperties.get(0), customerProperties.get(1), customerProperties.get(2), Integer.parseInt(customerProperties.get(3)));
            if (searchBy == SearchBy.NAME) {
                if (customerFile.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) || customerFile.getLastName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    returnPaths.add(filePath);
                }
            } else if (searchBy == SearchBy.SSN) {
                if (String.valueOf(customerFile.getSocialSecurityNumber()).toLowerCase().contains(searchTerm.toLowerCase())) {

                    returnPaths.add(filePath);
                }
            }
        }
        return returnPaths;
    }
}




