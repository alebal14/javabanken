package com;


public class Kund {
    private String firstName;
    private String lastName;
    private String email;
    private int socialSecurityNumber;


    public Kund(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public int getSocialSecurityNumber (){
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber (int socialSecurityNumber){
        this.socialSecurityNumber = socialSecurityNumber;
    }




}
