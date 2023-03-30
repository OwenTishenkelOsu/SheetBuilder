package com.example.sheetbuilder.model;

import timber.log.Timber;

public class Element {
    private String elementText;
    private String id;
    private String sheetID;

    private final String TAG = getClass().getSimpleName();
    public Element(){
        //public no-arg constr
    }

    public Element(String text, String id, String sheetID){
        this.elementText = text;
        this.id = id;
        this.sheetID = sheetID;
    }

    public String getText(){
        return elementText;
    }

    public String getId(){return id;}

    public void setText(String newText){
        this.elementText=newText;
    }
}
