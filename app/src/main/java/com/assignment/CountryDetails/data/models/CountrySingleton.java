package com.assignment.CountryDetails.data.models;

import java.security.PublicKey;

public class CountrySingleton {

    private String Heading="";

    public static final CountrySingleton INSTANCE=new CountrySingleton();

    private CountrySingleton() {
    }

    public static CountrySingleton getInstance(){
        return INSTANCE;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }
}
