package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Customer {
    private LinkedHashMap<String, String> details = new LinkedHashMap<>();


    public Customer(String firstName, String lastName, String email, int ssn) {
        details.put("firstname", firstName);
        details.put("lastname", lastName);
        details.put("email", email);
        details.put("ssn", String.valueOf(ssn));
        //details.put("accounts", )
    }

    public String getFirstName() {
        return details.get("firstname");
    }

    public void setFirstName(String firstName) {
        this.details.put("firstname", firstName);
    }

    public String getLastName() {
        return details.get("lastname");
    }

    public void setLastName(String lastName) {
        this.details.put("lastname", lastName);
    }

    public String getEmail() {
        return details.get("email");
    }

    public void setEmail (String email) {
        this.details.put("email", email);
    }

    public int getSocialSecurityNumber (){
        return Integer.parseInt(details.get("ssn"));
    }

    public void setSocialSecurityNumber (int socialSecurityNumber){
        this.details.put("ssn", String.valueOf(socialSecurityNumber));
    }

    public List<String> getList() {
        List<String> detailsList = new ArrayList<>();
        for(String key : details.keySet()) {
            detailsList.add(key+":"+details.get(key));
        }
        return detailsList;
    }


}
