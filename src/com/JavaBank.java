package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JavaBank {

    private FileManager fm;
    private int input;
    private UniqueRandomNr urn = new UniqueRandomNr();

    private int searchIndex = 0;

    private List<String> customerSearchResults = new ArrayList<>();
    private List<Path> accountSearchResults = new ArrayList<>();

    private Customer selectedCustomer;
    private String selectedCustomerPath;
    private Customer selectedCustomerTwo;
    private String selectedCustomerPath;
    private Account selectedAccount;
    private Account selectedAccountTwo;
    private Path selectedAccountPath;
    private Path selectedAccountPathTwo;

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
            if(!Files.exists(Paths.get("Javabank")))Files.createDirectory(Paths.get("Javabank"));
            if(!Files.exists(Paths.get("Javabank/Customer")))Files.createDirectory(Paths.get("Javabank/Customer"));
            if(!Files.exists(Paths.get("Javabank/Account")))Files.createDirectory(Paths.get("Javabank/Account"));
            if(!Files.exists(Paths.get("Javabank/Staffmembers")))Files.createDirectory(Paths.get("Javabank/Staffmembers"));

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

    private void printCustomerOptions() {
        System.out.println("----------------------------------------");
        System.out.println("1. Visa Kundinformation");
        System.out.println("2. Skapa nytt konto");
        System.out.println("3. Redigera personlig information");
        System.out.println("4. Redigera konto information/ Gör en överföring"); // return list of accounts.
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
        System.out.println("1. Visa Kontoinformation");
        System.out.println("2. Redigera saldo");
        System.out.println("3. Redigera skuld");
        System.out.println("4. Gör en överföring");
        System.out.println("0. Tillbaka\n");
    }

    private void mainSelection() {
        switch (input) {
            case 0: // Tillbaka
                break;
            case 1: // Sök kund
                printSearchMenu();
                input = Input.number("Mata in val: ");
                searchSelection();
                break;
            case 2: // Skapa kund
                Customer customer = createCustomer();
                Account account = createAccount(customer, 0 ,0);
                System.out.println("\nSkapade kund > " + customer.getFirstName() + " " + customer.getLastName() + "\nKonto: " + account.getAccountNumber()+"\n");

                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 3: // Skriv ut personal
                showStaffmembers();
                printMainMenu();
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
                printMainMenu();
                input = Input.number("Mata in val: ");
                mainSelection();
                break;
            case 1: // Sök namn
                customerSearchResults = searchCustomers(Input.string("Mata in söktext: "),  SearchBy.NAME);
                System.out.println("----------------------------------------");
                for(String customer:customerSearchResults) {
                    System.out.println(++searchIndex+". "+customer);
                }
                System.out.println("0. Tillbaka\n");
                inputRange(customerSearchResults.size());
                selectCustomer();
                break;
            case 2: // Sök Personnummer
                customerSearchResults = searchCustomers(Input.string("Mata in siffror: "), SearchBy.SSN);
                System.out.println("----------------------------------------");

                for(String customer:customerSearchResults) {
                    System.out.println(++searchIndex+". "+customer);
                }
                System.out.println("0. Tillbaka\n");
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
        searchIndex = 0;
        if(input == 0) {
            printSearchMenu();
            input = Input.number("Mata in val: ");
            searchSelection();
        } else if(input > 0 && input <= customerSearchResults.size()) {
            selectedCustomerPath = customerSearchResults.get(input-1);
            List<String> customerProperties = new ArrayList<>();
            for(String property:fm.readData(customerSearchResults.get(input-1))) {
                customerProperties.add(property.split(":")[1]);
            }
            selectedCustomer = new Customer(customerProperties.get(0), customerProperties.get(1), customerProperties.get(2), Integer.parseInt(customerProperties.get(3)));
            printCustomerOptions();
            input = Input.number("Mata in val: ");
            customerOptionsSelection();
        }
    }

    private void selectCustomerTwo() {
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
            selectedCustomerTwo = new Customer(customerProperties.get(0), customerProperties.get(1), customerProperties.get(2), Integer.parseInt(customerProperties.get(3)));

        }
        /*printAccountOptions();
        input = Input.number("Mata in val: ");
        accountOptionsSelection();*/
    }

    private void transferMoney(double transactionSum) {
        System.out.println();
        selectedAccount.setAccountBalance(selectedAccount.getAccountBalance()-transactionSum);
        fm.write(selectedAccountPath.toString(), selectedAccount.getList());
        selectedAccountTwo.setAccountBalance(selectedAccountTwo.getAccountBalance()+transactionSum);
        fm.write(selectedAccountPathTwo.toString(), selectedAccountTwo.getList());
    }

    private void selectAccount() {
        searchIndex = 0;
        if(input == 0) {
            printSearchMenu();
            input = Input.number("Mata in val: ");
            searchSelection();
        } else if(input > 0 && input <= accountSearchResults.size()) {
            selectedAccountPath = accountSearchResults.get(input-1);
            List<String> accountProperties = new ArrayList<>();
            for(String property:fm.readData(accountSearchResults.get(input-1).toString())) {
                accountProperties.add(property.split(":")[1]);
            }
            selectedAccount = new Account(Integer.parseInt(accountProperties.get(0)), Double.parseDouble(accountProperties.get(1)), Double.parseDouble(accountProperties.get(2)), Integer.parseInt(accountProperties.get(3)));
            printAccountOptions();
            input = Input.number("Mata in val: ");
            accountOptionsSelection();
        }
    }

    private void selectAccountTwo() {

        searchIndex = 0;
        if (input == 0) {
            printAccountOptions();
            input = Input.number("Mata in val: ");
            accountOptionsSelection();
        } else if (input > 0 && input <= accountSearchResults.size()) {
            selectedAccountPathTwo = accountSearchResults.get(input - 1);
            List<String> accountProperties = new ArrayList<>();
            for (String property : fm.readData(accountSearchResults.get(input - 1).toString())) {
                accountProperties.add(property.split(":")[1]);
            }
            selectedAccountTwo = new Account(Integer.parseInt(accountProperties.get(0)), Double.parseDouble(accountProperties.get(1)), Double.parseDouble(accountProperties.get(2)), Integer.parseInt(accountProperties.get(3)));
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
            case 0: // Tillbaka
                printSearchMenu();
                input = Input.number("Mata in val: ");
                searchSelection();
                break;
            case 1: // Visa Kundinformation
                System.out.println("\nNamn: "+selectedCustomer.getFirstName()+" "+selectedCustomer.getLastName());
                System.out.println("Email: "+selectedCustomer.getEmail());
                System.out.println("Personnummer: "+selectedCustomer.getSocialSecurityNumber());
                System.out.println("0. Tillbaka\n");
                inputRange(0);
                printCustomerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 2: // Skapa nytt konto
                createAccount(selectedCustomer, 0 ,0);
                System.out.println("Nytt konto skapat");
                printCustomerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 3: // Redigera personlig information
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 4: // Redigera konto
                searchIndex = 0;
                List<String> accountData;
                for(Path accPath:fm.listFiles("Javabank/Account")) {
                    accountData = new ArrayList<>();
                    for(String line:fm.readData(accPath.toString())) {
                        accountData.add(line.split(":")[1]);
                    }
                    if(accountData.get(3).equals(String.valueOf(selectedCustomer.getSocialSecurityNumber()))) {
                        accountSearchResults.add(accPath);
                        System.out.println(++searchIndex+". "+accPath);
                    }
                }
                System.out.println("0. Tillbaka\n");
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
                printCustomerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 1: // Redigera förnamn
                editFile(FileProperty.FIRSTNAME);
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 2: // Redigera efternamn
                editFile(FileProperty.LASTNAME);
                printCustomerEditOptions();
                input = Input.number("Mata in val: ");
                customerEditOptionsSelection();
                break;
            case 3: // Redigera email
                editFile(FileProperty.EMAIL);
                printCustomerEditOptions();
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
                printCustomerOptions();
                input = Input.number("Mata in val: ");
                customerOptionsSelection();
                break;
            case 1: // Visa kontoinformation
                System.out.println("\nAccount Number: " + selectedAccount.getAccountNumber());
                System.out.println("Account Balance: " + selectedAccount.getAccountBalance());
                System.out.println("Debt: " + selectedAccount.getDebt());
                System.out.println("0. Tillbaka\n");
                inputRange(0);
                printAccountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
            case 2: // Redigera saldo
                editFile(FileProperty.BALANCE);
                printAccountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;

            case 3: // Redigera skuld
                editFile(FileProperty.DEBT);
                printAccountOptions();
                input = Input.number("Mata in val: ");
                accountOptionsSelection();
                break;
            case 4: //Överföring
                customerSearchResults = searchFiles(Input.string("Mata in personnummer: "), fm.listFiles("Javabank/Customer"), SearchBy.SSN);
                System.out.println("----------------------------------------");
                for (Path p : customerSearchResults) {
                    System.out.println(++searchIndex + ". " + p);
                }
                System.out.println("0. Tillbaka\n");
                input = Input.number("Mata in val: ");
                validateInput(customerSearchResults.size());
                selectCustomerTwo();

                accountSearchResults = new ArrayList<>();
                List<String> accountData;
                for (Path accPath : fm.listFiles("Javabank/Account")) {
                    accountData = new ArrayList<>();
                    for (String line : fm.readData(accPath.toString())) {
                        accountData.add(line.split(":")[1]);
                    }
                    if (accountData.get(3).equals(String.valueOf(selectedCustomerTwo.getSocialSecurityNumber()))) {
                        accountSearchResults.add(accPath);
                        System.out.println(++searchIndex + ". " + accPath);
                    }
                }
                System.out.println("0. Tillbaka\n");
                input = Input.number("Mata in val: ");
                validateInput(accountSearchResults.size());
                selectAccountTwo();
                double transactionSum = Input.floatingNumber("Ange hur mycket du vill överföra: ");
                System.out.println("Vill du genomföra följande överföring? : ");
                System.out.println("-----------------");
                System.out.println("Namn" + "\t" + "\t" + "\t" + "\t"   + "Kontonummer" + "\t" + "\t" + "Belopp" + "\t" +  "\t" + "Överföringssumma");
                System.out.println(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName() +"\t" + selectedAccount.getAccountNumber() + "\t" + "\t"+ selectedAccount.getAccountBalance() + "\t" +  "\t"  + "\t" + "-" + transactionSum);
                System.out.println(selectedCustomerTwo.getFirstName() + " " + selectedCustomerTwo.getLastName() +"\t" + selectedAccountTwo.getAccountNumber() +"\t" + "\t"+ selectedAccountTwo.getAccountBalance() + "\t" +  "\t"  + "\t" + "+" + transactionSum);

                System.out.println("1. Ja");
                System.out.println("0. Nej\n");
                input = Input.number("Mata in val: ");
                validateInput(1);
                    if (input == 0){
                        printCustomerOptions();
                        input = Input.number("Mata in val: ");
                        customerOptionsSelection();
                    }
                    else if (input == 1) {
                        transferMoney(transactionSum);
                        System.out.println("Överföring genomförd :-)");
                        printMainMenu();
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



    private Account createAccount(Customer customer, int balance, int debt) {
        Account account = new Account(urn.randomNumber(), balance, debt, customer.getSocialSecurityNumber());
        fm.write("Javabank/Account/" + new Date().getTime() + ".txt", account.getList());
        return account;
    }

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean validateName(String name) {
        return name.matches("[A-Ö][a-ö]*");
    }

    private void editFile(FileProperty fileProperty) {
        if(fileProperty==FileProperty.FIRSTNAME) {
            fm.delete(selectedCustomerPath.toString());
            List<String> splitPath = Arrays.asList(selectedCustomerPath.split(selectedCustomer.getFirstName()));
            selectedCustomer.setFirstName(Input.string("Mata in nytt förnamn: "));
            fm.write(splitPath.get(0)+selectedCustomer.getFirstName()+splitPath.get(1), selectedCustomer.getList());
        }
        if(fileProperty==FileProperty.LASTNAME) {
            fm.delete(selectedCustomerPath.toString());
            List<String> splitPath = Arrays.asList(selectedCustomerPath.toString().split(selectedCustomer.getLastName()));
            selectedCustomer.setLastName(Input.string("Mata in nytt efternamn: "));
            fm.write(splitPath.get(0)+selectedCustomer.getLastName()+splitPath.get(1), selectedCustomer.getList());
        }
        if(fileProperty==FileProperty.EMAIL) {
            String newEmail = Input.string("Mata in ny email: ");
            while(!validateEmail(newEmail)) {
                System.out.println("#invalid email#");
                newEmail = Input.string("Mata in ny email: ");
            }
            selectedCustomer.setEmail(newEmail);
            fm.write(selectedCustomerPath, selectedCustomer.getList());
        }
        if(fileProperty == FileProperty.BALANCE) {
            selectedAccount.setAccountBalance(Input.floatingNumber("Mata in nytt saldo: "));
            fm.write(selectedAccountPath.toString(), selectedAccount.getList());
        }
        if(fileProperty == FileProperty.DEBT) {
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

        int ssn;
        do {
            ssn = Input.number("Mata in personnummer: ");
        } while (String.valueOf(ssn).length() != 10 && String.valueOf(ssn).length() != 12);

        Customer customer = new Customer(firstName, lastName, email, ssn);
        fm.write("Javabank/Customer/" + UUID.randomUUID() + "-" + customer.getFirstName() + "_" + customer.getLastName() + ".txt", customer.getList());
        return customer;
    }

    private List<String> searchCustomers(String searchQuery, SearchBy searchBy) {
        List<String> searchResults = new ArrayList<>();
        Customer customerFile;
        List<String> customerProperties;

        System.out.println("----------------------------------------");
        System.out.println("Javabanken > Sök kund");
        System.out.println("----------------------------------------");
        System.out.println("1. Sök namn");
        System.out.println("2. Sök personnummer");
        System.out.println("0. Avbryt\n");

        inputRange(2);

        for(Path filePath:fm.listFiles("Javabank/Customer")) {
            customerProperties = new ArrayList<>();
            for(String line:fm.readData(filePath.toString())) {
                customerProperties.add(line.split(":")[1]);
            }
            customerFile = new Customer(customerProperties.get(0), customerProperties.get(1), customerProperties.get(2), Integer.parseInt(customerProperties.get(3)));
            if(searchBy == SearchBy.NAME) {
                if(customerFile.getFirstName().toLowerCase().contains(searchQuery.toLowerCase()) || customerFile.getLastName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    searchResults.add(filePath.toString());
                }
            } else if(searchBy == SearchBy.SSN) {
                if(String.valueOf(customerFile.getSocialSecurityNumber()).toLowerCase().contains(searchQuery.toLowerCase())) {
                    searchResults.add(filePath.toString());
                }
            }
        }
        return searchResults;
    }

    private List<String> listAccounts(String ssn) {
        return new ArrayList<>();
    }

    private void inputRange(int range) {
        input = Input.number("Mata in val: ");
        while (input < 0 || input > range) {
            System.out.println("#invalid input#");
            input = Input.number("Mata in val: ");
        }
    }
}
