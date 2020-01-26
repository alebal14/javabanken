package com;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JavaBank {


    JavaBank() {
        BankManager.buildDirectories();
        PrintMenu.main();
        MenuSelection.main(Validate.inputRange(3));
    }
}