package com.example.sheetbuilder.model;

import timber.log.Timber;

public class Sheet {
    private String sheetName;

    private final String TAG = getClass().getSimpleName();
    public Sheet(){
        //public no-arg constr
    }

    public Sheet(String name){
        this.sheetName = name;
        Timber.tag(TAG).d("in sheet constructor " + this.sheetName +" "+name);
    }

    public String getName(){
        return sheetName;
    }

    public void setName(String newName){
        this.sheetName=newName;
    }
}
