package com.example.sheetbuilder.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class SheetTest {

    Sheet testSheet = new Sheet("Bob", "12", "2");

    @Test
    public void getName() {
        assertEquals(testSheet.getName(), "Bob");
    }

    @Test
    public void getId() {
        assertEquals(testSheet.getId(), "12");
    }

    @Test
    public void getUserID() {

        assertEquals(testSheet.getUserID(), "2");
    }

    @Test
    public void setName() {
        String newName = "Antonio";
        assertEquals(testSheet.getName(), "Bob"); //before
        testSheet.setName(newName);
        assertEquals(testSheet.getName(), "Antonio");

    }
}