package com;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

abstract public class AccountNumber {

    public static List<Integer> getAllAccountsNr() throws IOException {
        List<Integer> currentAccountsNumbers = new ArrayList<>();
        for (String path : FileManager.getFilesPaths("Javabank/Account")) {
            Path accountFilePath = Path.of(path);
            String account_number =  Files.readAllLines(accountFilePath).toString();
            int at = account_number.indexOf(",");
            String nr =((String) account_number.subSequence(15,at));
            currentAccountsNumbers.add(Integer.parseInt(nr));
        }
        return currentAccountsNumbers;
    }

    public static int randomNumber(){
        return 1000000000 + (int) (Math.random() * 999999999);
    }

    public static int generateUniqueAccountNr() throws IOException {
        boolean unique = false;
        int randomNr = randomNumber();

        while (unique == false){
            if (getAllAccountsNr().contains(randomNr) ){
                randomNr = randomNumber();
            }else {
                unique = true;
                return randomNr;
            }
        }
        return 0;
    }
}
