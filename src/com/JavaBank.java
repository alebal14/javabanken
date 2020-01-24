package com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JavaBank {

    private FileManager fm;
    private AccountNumber urn = new AccountNumber();

    private List<String> customerSearchResults = new ArrayList<>();
    private List<String> accountSearchResults = new ArrayList<>();

    private int input;

    private Customer selectedCustomer, selectedCustomerTwo;
    private Account selectedAccount, selectedAccountTwo;
    private String selectedCustomerPath, selectedAccountPath, selectedAccountPathTwo;

    private enum FileProperty {
        FIRSTNAME,
        LASTNAME,
        EMAIL,
        BALANCE,
        DEBT
    }


    JavaBank() {
        fm = new FileManager();
        buildDirectories();
        PrintMenu.main();
        inputRange(3);
        mainSelection();
    }

    private void buildDirectories() {
        try {

            if (!Files.exists(Paths.get("Javabank"))) Files.createDirectory(Paths.get("Javabank"));
            if (!Files.exists(Paths.get("Javabank/Customer"))) Files.createDirectory(Paths.get("Javabank/Customer"));
            if (!Files.exists(Paths.get("Javabank/Account"))) Files.createDirectory(Paths.get("Javabank/Account"));
            if (!Files.exists(Paths.get("Javabank/Employees"))) Files.createDirectory(Paths.get("Javabank/Employees"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mainSelection() {
        switch (input) {
            case 0: // Avsluta
                break;
            case 1: // Sök kund
                PrintMenu.search();
                inputRange(2);
                searchSelection();
                break;
            case 2: // Skapa kund
                System.out.println();
                Customer customer = createCustomer();
                Account account = createAccount(customer, 0, 0);
                System.out.println("\n----------------------------------------");
                System.out.println("\nSkapade kund > " + customer.getFirstName() + " " + customer.getLastName() + "\nKonto: " + account.getAccountNumber() + "\n");

                System.out.println("----------------------------------------");
                PrintMenu.main();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 3: // Skriv ut personal
                System.out.println("\n----------------------------------------");
                System.out.println("Personal");
                System.out.println("----------------------------------------");
                printPersonnel();
                System.out.println("\n0. Tillbaka\n");
                inputRange(0);
                PrintMenu.main();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            default: // invalid input
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
        }
    }

    private void searchSelection() {
        switch (input) {
            case 0: // Tillbaka
                PrintMenu.main();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 1: // Sök namn
                String searchQuery = Input.string("Mata in söktext: ");
                customerSearchResults = fm.searchFiles("Javabank/Customer", searchQuery, "firstname");
                for(String filePath:fm.searchFiles("Javabank/Customer", searchQuery, "lastname")) {
                    if(!customerSearchResults.contains(filePath)) {
                        customerSearchResults.add(filePath);
                    }
                }
                PrintMenu.searchResult(customerSearchResults);
                inputRange(customerSearchResults.size());
                selectCustomer();
                break;
            case 2: // Sök Personnummer

                customerSearchResults = fm.searchFiles("Javabank/Customer", Input.string("Mata in söktext: "), "ssn");
                PrintMenu.searchResult(customerSearchResults);
                inputRange(customerSearchResults.size());
                selectCustomer();
                break;
            default: // invalid input
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                searchSelection();
                break;
        }
    }

    private void selectCustomer() {
        if (input == 0) {
            PrintMenu.main();
            input = Input.number("Mata in val: ");
            mainSelection();
        } else if (input > 0 && input <= customerSearchResults.size()) {
            selectedCustomerPath = customerSearchResults.get(input - 1);
            List<String> customerProperties = new ArrayList<>();
            for (String property : fm.readData(customerSearchResults.get(input - 1))) {

                customerProperties.add(property.split(":")[1]);
            }
            selectedCustomer = new Customer(customerProperties.get(0), customerProperties.get(1), customerProperties.get(2), Long.parseLong(customerProperties.get(3)));
            PrintMenu.customerOptions();
            input = Input.number("Mata in val: ");
            customerOptionsSelection();
        }
    }

    private void selectCustomerTwo() {
        if (input == 0) {
            PrintMenu.main();
            input = Input.number("Mata in val: ");
            mainSelection();
        } else if (input > 0 && input <= customerSearchResults.size()) {
            selectedCustomerPath = customerSearchResults.get(input - 1);
            List<String> customerProperties = new ArrayList<>();
            for (String property : fm.readData(customerSearchResults.get(input - 1))) {
                customerProperties.add(property.split(":")[1]);
            }
            selectedCustomerTwo = new Customer(customerProperties.get(0), customerProperties.get(1), customerProperties.get(2), Long.parseLong(customerProperties.get(3)));

        }
    }

    private void transferMoney(double transactionSum) {
        System.out.println();
        selectedAccount.setAccountBalance(selectedAccount.getAccountBalance() - transactionSum);
        fm.write(selectedAccountPath, selectedAccount.getList());
        selectedAccountTwo.setAccountBalance(selectedAccountTwo.getAccountBalance() + transactionSum);
        fm.write(selectedAccountPathTwo, selectedAccountTwo.getList());
    }

    private void selectAccount() {
        if (input == 0) {
            PrintMenu.customerOptions();
            input = Input.number("Mata in val: ");
            customerOptionsSelection();
        } else if (input > 0 && input <= accountSearchResults.size()) {
            selectedAccountPath = accountSearchResults.get(input - 1);
            List<String> accountProperties = new ArrayList<>();
            for (String property : fm.readData(accountSearchResults.get(input - 1))) {

                accountProperties.add(property.split(":")[1]);
            }
            selectedAccount = new Account(Integer.parseInt(accountProperties.get(0)), Double.parseDouble(accountProperties.get(1)), Double.parseDouble(accountProperties.get(2)), Long.parseLong(accountProperties.get(3)));
            PrintMenu.accountOptions();
            input = Input.number("Mata in val: ");
            accountOptionsSelection();
        }
    }

    private void selectAccountTwo() {
        if (input == 0) {
            PrintMenu.accountOptions();
            input = Input.number("Mata in val: ");
            accountOptionsSelection();
        } else if (input > 0 && input <= accountSearchResults.size()) {
            selectedAccountPathTwo = accountSearchResults.get(input - 1);
            List<String> accountProperties = new ArrayList<>();
            for (String property : fm.readData(accountSearchResults.get(input - 1))) {
                accountProperties.add(property.split(":")[1]);
            }
            selectedAccountTwo = new Account(Integer.parseInt(accountProperties.get(0)), Double.parseDouble(accountProperties.get(1)), Double.parseDouble(accountProperties.get(2)), Long.parseLong(accountProperties.get(3)));
        }
    }

    private void customerOptionsSelection() {
        switch (input) {
            case 0: // Tillbaka
                PrintMenu.main();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 1: // Visa Kundinformation
                System.out.println("\nNamn: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
                System.out.println("Email: " + selectedCustomer.getEmail());
                System.out.println("Personnummer: " + selectedCustomer.getSocialSecurityNumber());
                System.out.println("0. Tillbaka\n");
                inputRange(0);
                PrintMenu.customerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 2: // Skapa nytt konto
                createAccount(selectedCustomer, 0, 0);
                System.out.println("Nytt konto skapat");
                PrintMenu.customerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 3: // Redigera personlig information
                PrintMenu.editCustomer();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 4: // Redigera konto
                accountSearchResults = listAccounts(selectedCustomer.getSocialSecurityNumber());
                PrintMenu.searchResult(accountSearchResults);
                inputRange(accountSearchResults.size());
                selectAccount();
                break;
            case 5: // Radera kund
                System.out.println("Kund raderad");
                deleteCustomer();
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
            case 0: // Tillbaka
                PrintMenu.customerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 1: // Redigera förnamn
                editFile(FileProperty.FIRSTNAME);
                PrintMenu.editCustomer();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 2: // Redigera efternamn
                editFile(FileProperty.LASTNAME);
                PrintMenu.editCustomer();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 3: // Redigera email
                editFile(FileProperty.EMAIL);
                PrintMenu.editCustomer();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            default: // invalid input
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
        }
    }

    private void accountOptionsSelection() {
        switch (input) {
            case 0: // Tillbaka
                PrintMenu.customerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 1: // Visa kontoinformation
                System.out.println("\nAccount Number: " + selectedAccount.getAccountNumber());
                System.out.println("Account Balance: " + selectedAccount.getAccountBalance());
                System.out.println("Debt: " + selectedAccount.getDebt());
                System.out.println("0. Tillbaka\n");
                inputRange(0);
                PrintMenu.accountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
            case 2: // Redigera saldo
                editFile(FileProperty.BALANCE);
                PrintMenu.accountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;

            case 3: // Redigera skuld
                editFile(FileProperty.DEBT);
                PrintMenu.accountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
            case 4: //Överföring
                System.out.println("----------------------------------------");
                System.out.println("Sök konto att överföra till");
                System.out.println("----------------------------------------");
                System.out.println("1. Sök namn");
                System.out.println("2. Sök personnummer");
                System.out.println("0. Avbryt\n");
                inputRange(2);

                if (input == 0) {

                    PrintMenu.main();
                    input = Input.number("Mata in val: ");
                    mainSelection();
                    break;

                } else if (input == 1) {
                    String searchQuery = Input.string("Mata in söktext: ");
                    customerSearchResults = fm.searchFiles("Javabank/Customer", searchQuery, "firstname");
                    for(String filePath:fm.searchFiles("Javabank/Customer", searchQuery, "lastname")) {
                        if(!customerSearchResults.contains(filePath)) {
                            customerSearchResults.add(filePath);
                        }
                    }
                } else if (input == 2) {
                    customerSearchResults = fm.searchFiles("Javabank/Customer", Input.string("Mata in söktext: "), "ssn");

                }
                PrintMenu.searchResult(customerSearchResults);
                inputRange(customerSearchResults.size());
                selectCustomerTwo();
                accountSearchResults = listAccounts(selectedCustomerTwo.getSocialSecurityNumber());
                PrintMenu.searchResult(accountSearchResults);
                inputRange(accountSearchResults.size());
                selectAccountTwo();
                double transactionSum = Input.floatingNumber("Ange hur mycket du vill överföra: ");
                System.out.println("Vill du genomföra följande överföring?");
                System.out.println("----------------------------------------");
                System.out.println("Namn" + "\t" + "\t" + "\t" + "Kontonummer" + "\t" + "\t" + "Belopp" + "\t" + "\t" + "Överföringssumma");
                System.out.println(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() + "\t" + selectedAccount.getAccountNumber() + "\t" + "\t" + selectedAccount.getAccountBalance() + "\t" + "\t" + "\t" + "-" + transactionSum);
                System.out.println(selectedCustomerTwo.getFirstName() + " " + selectedCustomerTwo.getLastName() + "\t" + selectedAccountTwo.getAccountNumber() + "\t" + "\t" + selectedAccountTwo.getAccountBalance() + "\t" + "\t" + "\t" + "+" + transactionSum);
                System.out.println("1. Ja");
                System.out.println("0. Nej\n");
                inputRange(1);
                if (input == 0) {
                    PrintMenu.customerOptions();
                    input = Input.number("Mata in val: ");
                    customerOptionsSelection();
                } else if (input == 1) {
                    transferMoney(transactionSum);
                    System.out.println("Överföring genomförd :-)");
                    PrintMenu.main();
                    input = Input.number("Mata in val: ");
                    mainSelection();
                }
                break;
            case 5:
                System.out.println("Konto raderad");
                deleteAccount();
                break;
            default: // invalid input
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
        }
    }

    private void editFile(FileProperty fileProperty) {
        if (fileProperty == FileProperty.FIRSTNAME) {

            fm.delete(selectedCustomerPath);
            List<String> splitPath = Arrays.asList(selectedCustomerPath.split(selectedCustomer.getFirstName()));
            selectedCustomer.setFirstName(Input.string("Mata in nytt förnamn: "));
            fm.write(splitPath.get(0) + selectedCustomer.getFirstName() + splitPath.get(1), selectedCustomer.getList());
        }

        if (fileProperty == FileProperty.LASTNAME) {

            fm.delete(selectedCustomerPath);
            List<String> splitPath = Arrays.asList(selectedCustomerPath.split(selectedCustomer.getLastName()));
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
            fm.write(selectedCustomerPath, selectedCustomer.getList());
        }

        if (fileProperty == FileProperty.BALANCE) {
            selectedAccount.setAccountBalance(Input.floatingNumber("Mata in nytt saldo: "));
            fm.write(selectedAccountPath, selectedAccount.getList());
        }
        if (fileProperty == FileProperty.DEBT) {

            selectedAccount.setDebt(Input.floatingNumber("Mata in ny skuld: "));
            fm.write(selectedAccountPath, selectedAccount.getList());
        }
    }

    private void printPersonnel() {


        for (String line : fm.readData("Javabank/Employees/Staffmembers.txt")) {

            System.out.println(line);
        }
    }

    private Customer createCustomer() {
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

        long ssn;
        do {
            ssn = Input.longNumber("Mata in personnummer: ");
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

    private List<String> listAccounts(long ssn) {
        List<String> searchResults = new ArrayList<>();
        List<String> accountData;
        Account accountFile;

        for (Path filePath : fm.listFiles("Javabank/Account")) {
            accountData = new ArrayList<>();
            for (String line : fm.readData(filePath.toString())) {
                accountData.add(line.split(":")[1]);
            }
            accountFile = new Account(Integer.parseInt(accountData.get(0)), Double.parseDouble(accountData.get(1)), Double.parseDouble(accountData.get(2)), Long.parseLong(accountData.get(3)));
            if (accountFile.getSSN() == ssn) {
                searchResults.add(filePath.toString());
            }
        }
        return searchResults;
    }

    private void inputRange(int range) {
        input = Input.number("Mata in val: ");
        while (input < 0 || input > range) {
            System.out.println("#Input out of range#");
            input = Input.number("Mata in val: ");
        }
    }

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean validateName(String name) {
        return name.matches("[A-Z][a-z]*");
    }


    private void deleteCustomer() {
        PrintMenu.main();
        input = Input.number("Mata in val: ");
        mainSelection();
        fm.delete(selectedCustomerPath);
        long ssn = selectedCustomer.getSocialSecurityNumber();
        for (String path : listAccounts(ssn)) {
            fm.delete(path);
        }
    }

    private void deleteAccount() {
        PrintMenu.customerOptions();
        input = Input.number("Mata in val: ");
        customerOptionsSelection();
        fm.delete(selectedAccountPath);



    }

}
