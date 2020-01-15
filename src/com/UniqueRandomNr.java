package com;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UniqueRandomNr {



    public static void main(String[] args) throws IOException {

        UniqueRandomNr uniqueRandomNr = new UniqueRandomNr();
        System.out.println(uniqueRandomNr.generateUniqueAccountNr());

    }





    public List<String> getAccountFilesPaths() throws IOException {
        List<String> paths = new ArrayList<>();
        Files.walk(Paths.get("Javabank/Account/")).forEach(path -> paths.add(path.toString()));
        paths.remove(0);
        return paths;
    }

    public List<Integer> getAllAccountsNr() throws IOException {
        List<Integer> currentAccountsNumbers = new ArrayList<>();

        for (String path : getAccountFilesPaths()) {
            Path accountFilePath = Path.of(path);
            String account_number =  Files.readAllLines(accountFilePath).toString();
            int at = account_number.indexOf(",");
            String nr =((String) account_number.subSequence(15,at));
            currentAccountsNumbers.add(Integer.parseInt(nr));
        }
        return currentAccountsNumbers;
    }

    public int randomNumber(){
        return 1000000000 + (int) (Math.random() * 999999999);
    }

    public int generateUniqueAccountNr() throws IOException {
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
