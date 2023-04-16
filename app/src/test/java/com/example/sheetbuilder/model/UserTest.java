package com.example.sheetbuilder.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

    User testUser = new User("testUser@gmail.com", "1");

    @Test
    public void getEmail() {
        assertEquals(testUser.getEmail(), "testUser@gmail.com");
    }

    @Test
    public void getId() {
        assertEquals(testUser.getId(), "1");
    }
}