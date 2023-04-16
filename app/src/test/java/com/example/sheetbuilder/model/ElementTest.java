package com.example.sheetbuilder.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ElementTest {

    private Element ElementToTest = new Element("Strength","1", "1");

    @Test
    public void getText() {
        assertEquals(ElementToTest.getText(), "Strength");
        assertNotEquals(ElementToTest.getText(), "");
    }

    @Test
    public void getId() {
        assertEquals(ElementToTest.getId(), "1");
        assertNotEquals(ElementToTest.getId(), "2");
    }

    @Test
    public void setText() {
        String newText = "Agility";
        ElementToTest.setText(newText);

        assertEquals(ElementToTest.getText(), "Agility");
        assertNotEquals(ElementToTest.getText(), "Strength");
    }
}