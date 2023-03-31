package com.example.sheetbuilder.model;

import timber.log.Timber;

public class User {
    private String id;
    private String email;
    private String sheetID;

    private final String TAG = getClass().getSimpleName();
    public User(){
        //public no-arg constr
    }

    public User(String email, String id){
        this.email = email;
        this.id = id;
    }

    public String getEmail(){
        return email;
    }

    public String getId(){return id;}
}
