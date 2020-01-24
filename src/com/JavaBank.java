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

    private enum Filename {
        FIRSTNAME,
        LASTNAME
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
            if (!Files.exists(Paths.get("Javabank/Personnel"))) Files.createDirectory(Paths.get("Javabank/Personnel"));

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
                customerSearchResults = fm.searchFiles("Javabank/Customer",Input.string("Mata in söktext: "), "firstname");
                PrintMenu.searchResult(customerSearchResults);
                inputRange(customerSearchResults.size());
                selectCustomer();
                break;
            case 2: // Sök Personnummer
                customerSearchResults = fm.searchFiles("Javabank/Customer",Input.string("Mata in söktext: "), "ssn");
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
                renameFile(Filename.FIRSTNAME);
                PrintMenu.editCustomer();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 2: // Redigera efternamn
                renameFile(Filename.LASTNAME);
                PrintMenu.editCustomer();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 3: // Redigera email
                String newEmail = Input.string("Mata in ny email: ");
                while (validateEmail(newEmail)) {
                    newEmail = Input.string("Mata in ny email: ");
                }
                fm.write(selectedAccountPath, fm.edit(selectedCustomerPath, "email", newEmail));
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

    private void renameFile(Filename filename) {
        if(filename == Filename.FIRSTNAME) {
            String[] splitPath = selectedCustomerPath.split(selectedCustomer.getFirstName());
            List<String> customerData = fm.edit(selectedCustomerPath, "firstname", Input.string("Mata in nytt förnamn: "));
            fm.delete(selectedCustomerPath);
            selectedCustomer = new Customer(customerData.get(0).split(":")[1], customerData.get(1).split(":")[1], customerData.get(2).split(":")[1], Long.parseLong(customerData.get(3).split(":")[1]));
            fm.write(splitPath[0]+selectedCustomer.getFirstName()+splitPath[1], customerData);
        }
        if(filename == Filename.LASTNAME) {
            String[] splitPath = selectedCustomerPath.split(selectedCustomer.getLastName());
            List<String> customerData = fm.edit(selectedCustomerPath, "lastname", Input.string("Mata in nytt efternamn: "));
            fm.delete(selectedCustomerPath);
            selectedCustomer = new Customer(customerData.get(0).split(":")[1], customerData.get(1).split(":")[1], customerData.get(2).split(":")[1], Long.parseLong(customerData.get(3).split(":")[1]));
            fm.write(splitPath[0]+selectedCustomer.getLastName()+splitPath[1], customerData);
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
                //editFile(FileProperty.BALANCE);
                PrintMenu.accountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;

            case 3: // Redigera skuld
                //editFile(FileProperty.DEBT);
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
                    customerSearchResults = fm.searchFiles("Javabank/Customer",Input.string("Mata in söktext: "), "firstname");
                } else if (input == 2) {
                    customerSearchResults = fm.searchFiles("Javabank/Customer",Input.string("Mata in söktext: "), "ssn");
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
                System.out.println("Namn" + "\t" + "\t" + "\t" + "\t" + "Kontonummer" + "\t" + "\t" + "Belopp" + "\t" + "\t" + "Överföringssumma");
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
            default: // invalid input
                System.out.println("#invalid input#");
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
        }
    }

    private void printPersonnel() {
        for (String line : fm.readData("Javabank/Personnel/Personnel.txt")) {
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
}
