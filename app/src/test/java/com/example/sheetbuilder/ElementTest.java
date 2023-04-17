package com.example.sheetbuilder;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

import com.example.sheetbuilder.model.Element;

import org.junit.Test;

public class ElementTest {

    private Element ElementToTest = new Element("Strength","1", "1");

    @Test
    public void getText() {
        assertEquals(ElementToTest.getText(), "Strength");
    }

    @Test
    public void getId() {
        assertEquals(ElementToTest.getId(), "1");
    }

    @Test
    public void setText() {
        String newText = "Agility";
        assertEquals(ElementToTest.getText(), "Strength"); //before
        ElementToTest.setText(newText);
        assertEquals(ElementToTest.getText(), "Agility"); //after
    }
}