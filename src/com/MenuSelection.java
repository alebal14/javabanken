package com;

import java.util.ArrayList;
import java.util.List;

abstract public class MenuSelection {
    private static List<String> customerSearchResults = new ArrayList<>();
    private static List<String> accountSearchResults = new ArrayList<>();

    public static void main(int input) {
        switch (input) {
            case 0: // Avsluta
                break;
            case 1: // Sök kund
                PrintMenu.search();
                search(Validate.inputRange(2));
                break;
            case 2: // Skapa kund
                System.out.println();
                Customer customer = BankManager.createCustomer();
                Account account = BankManager.createAccount(customer, 0, 0);
                System.out.println("\n----------------------------------------");
                System.out.println("\nSkapade kund > " + customer.getFirstName() + " " + customer.getLastName() + "\nKonto: " + account.getAccountNumber() + "\n");
                System.out.println("----------------------------------------");
                PrintMenu.main();
                main(Input.number("Mata in val: "));
                break;
            case 3: // Skriv ut personal
                PrintMenu.personnel();
                Validate.inputRange(0);
                PrintMenu.main();
                main(Input.number("Mata in val: "));
                break;
            default: // invalid input
                System.out.println("#invalid input#");
                main(Input.number("Mata in val: "));
                break;
        }
    }

    public static void search(int input) {
        switch (input) {
            case 0: // Tillbaka
                PrintMenu.main();
                main(Input.number("Mata in val: "));
                break;
            case 1: // Sök namn
                String searchQuery = Input.string("Mata in söktext: ");
                customerSearchResults = FileManager.searchFiles("Javabank/Customer", searchQuery, "firstname");
                for(String filePath:FileManager.searchFiles("Javabank/Customer", searchQuery, "lastname")) {
                    if(!customerSearchResults.contains(filePath)) {
                        customerSearchResults.add(filePath);
                    }
                }
                PrintMenu.searchResult(customerSearchResults);
                BankManager.selectCustomer(customerSearchResults, Validate.inputRange(customerSearchResults.size()));
                break;
            case 2: // Sök Personnummer
                customerSearchResults = FileManager.searchFiles("Javabank/Customer", Input.string("Mata in söktext: "), "ssn");
                PrintMenu.searchResult(customerSearchResults);
                BankManager.selectCustomer(customerSearchResults, Validate.inputRange(customerSearchResults.size()));
                break;
            default: // invalid input
                System.out.println("#invalid input#");
                search(Input.number("Mata in val: "));
                break;
        }
    }

    public static void customerOptions(int input) {
        switch (input) {
            case 0: // Tillbaka
                PrintMenu.main();
                main(Input.number("Mata in val: "));
                break;
            case 1: // Visa Kundinformation
                System.out.println("\nNamn: " + BankManager.getSelectedCustomer().getFirstName() + " " + BankManager.getSelectedCustomer().getLastName());
                System.out.println("Email: " + BankManager.getSelectedCustomer().getEmail());
                System.out.println("Personnummer: " + BankManager.getSelectedCustomer().getSocialSecurityNumber());
                System.out.println("0. Tillbaka\n");
                Validate.inputRange(0);
                PrintMenu.customerOptions();
                customerOptions(Input.number("Mata in val: "));
                break;
            case 2: // Skapa nytt konto
                BankManager.createAccount(BankManager.getSelectedCustomer(), 0, 0);
                System.out.println("Nytt konto skapat");
                PrintMenu.customerOptions();
                customerOptions(Input.number("Mata in val: "));
                break;
            case 3: // Redigera personlig information
                PrintMenu.editCustomer();
                editCustomer(Input.number("Mata in val: "));
                break;
            case 4: // Redigera konto
                accountSearchResults = BankManager.listAccounts(BankManager.getSelectedCustomer().getSocialSecurityNumber());
                PrintMenu.searchResult(accountSearchResults);
                BankManager.selectAccount(accountSearchResults, Validate.inputRange(accountSearchResults.size()));
                break;
            case 5: // Radera kund
                System.out.println("Kund raderad");
                BankManager.deleteCustomer();
                break;
            default:
                System.out.println("#invalid input#");
                customerOptions(Input.number("Mata in val: "));
                break;
        }
    }

    public static void editCustomer(int input) {
        switch (input) {
            case 0: // Tillbaka
                PrintMenu.customerOptions();
                customerOptions(Input.number("Mata in val: "));
                break;
            case 1: // Redigera förnamn
                BankManager.editFile(BankManager.FileProperty.FIRSTNAME);
                PrintMenu.editCustomer();
                editCustomer(Input.number("Mata in val: "));
                break;
            case 2: // Redigera efternamn
                BankManager.editFile(BankManager.FileProperty.LASTNAME);
                PrintMenu.editCustomer();
                editCustomer(Input.number("Mata in val: "));
                break;
            case 3: // Redigera email
                BankManager.editFile(BankManager.FileProperty.EMAIL);
                PrintMenu.editCustomer();
                editCustomer(Input.number("Mata in val: "));
                break;
            default: // invalid input
                System.out.println("#invalid input#");
                editCustomer(Input.number("Mata in val: "));
                break;
        }
    }

    public static void accountOptions(int input) {
        switch (input) {
            case 0: // Tillbaka
                PrintMenu.customerOptions();
                customerOptions(Input.number("Mata in val: "));
                break;
            case 1: // Visa kontoinformation
                System.out.println("\nAccount Number: " + BankManager.getSelectedAccount().getAccountNumber());
                System.out.println("Account Balance: " + BankManager.getSelectedAccount().getAccountBalance());
                System.out.println("Debt: " + BankManager.getSelectedAccount().getDebt());
                System.out.println("0. Tillbaka\n");
                Validate.inputRange(0);
                PrintMenu.accountOptions();
                accountOptions(Input.number("Mata in val: "));
                break;
            case 2: // Redigera saldo
                BankManager.editFile(BankManager.FileProperty.BALANCE);
                PrintMenu.accountOptions();
                accountOptions(Input.number("Mata in val: "));
                break;
            case 3: // Redigera skuld
                BankManager.editFile(BankManager.FileProperty.DEBT);
                PrintMenu.accountOptions();
                accountOptions(Input.number("Mata in val: "));
                break;
            case 4: //Överföring
                System.out.println("----------------------------------------");
                System.out.println("Sök konto att överföra till");
                System.out.println("----------------------------------------");
                System.out.println("1. Sök namn");
                System.out.println("2. Sök personnummer");
                System.out.println("0. Avbryt\n");
                switch (Validate.inputRange(2)) {
                    case 0:
                        PrintMenu.main();
                        main(Input.number("Mata in val: "));
                        break;
                    case 1:
                        String searchQuery = Input.string("Mata in söktext: ");
                        customerSearchResults = FileManager.searchFiles("Javabank/Customer", searchQuery, "firstname");
                        for(String filePath:FileManager.searchFiles("Javabank/Customer", searchQuery, "lastname")) {
                            if(!customerSearchResults.contains(filePath)) {
                                customerSearchResults.add(filePath);
                            }
                        }
                        break;
                    case 2:
                        customerSearchResults = FileManager.searchFiles("Javabank/Customer", Input.string("Mata in söktext: "), "ssn");
                        break;
                }
                PrintMenu.searchResult(customerSearchResults);
                BankManager.selectCustomerTwo(customerSearchResults, Validate.inputRange(customerSearchResults.size()));
                accountSearchResults = BankManager.listAccounts(BankManager.getSelectedCustomerTwo().getSocialSecurityNumber());
                PrintMenu.searchResult(accountSearchResults);
                BankManager.selectAccountTwo(accountSearchResults, Validate.inputRange(accountSearchResults.size()));
                double transactionSum = Input.floatingNumber("Ange hur mycket du vill överföra: ");
                System.out.println("Vill du genomföra följande överföring?");
                System.out.println("----------------------------------------");
                System.out.println("Namn" + "\t" + "\t" + "\t" + "Kontonummer" + "\t" + "\t" + "Belopp" + "\t" + "\t" + "Överföringssumma");
                System.out.println(BankManager.getSelectedCustomer().getFirstName() + " " + BankManager.getSelectedCustomer().getLastName() + "\t" + BankManager.getSelectedAccount().getAccountNumber() + "\t" + "\t" + BankManager.getSelectedAccount().getAccountBalance() + "\t" + "\t" + "\t" + "-" + transactionSum);
                System.out.println(BankManager.getSelectedCustomerTwo().getFirstName() + " " + BankManager.getSelectedCustomerTwo().getLastName() + "\t" + BankManager.getSelectedAccountTwo().getAccountNumber() + "\t" + "\t" + BankManager.getSelectedAccountTwo().getAccountBalance() + "\t" + "\t" + "\t" + "+" + transactionSum);
                System.out.println("1. Ja");
                System.out.println("0. Nej\n");
                switch(Validate.inputRange(1)) {
                    case 0:
                        PrintMenu.customerOptions();
                        customerOptions(Input.number("Mata in val: "));
                        break;
                    case 1:
                        BankManager.transferMoney(transactionSum);
                        System.out.println("Överföring genomförd :-)");
                        PrintMenu.main();
                        main(Input.number("Mata in val: "));
                        break;
                }
                break;
            case 5:
                System.out.println("Konto raderad");
                BankManager.deleteAccount();
                break;
            default: // invalid input
                System.out.println("#invalid input#");
                accountOptions(Input.number("Mata in val: "));
                break;
        }
    }
}
