package com.example.sheetbuilder;

import static org.junit.Assert.*;

import com.example.sheetbuilder.model.User;

import org.junit.Test;

public class UserTest {

    private User testUser = new User("testUser@gmail.com", "1");

    @Test
    public void getEmail() {
        assertEquals(testUser.getEmail(), "testUser@gmail.com");
    }

    @Test
    public void getId() {
        assertEquals(testUser.getId(), "1");
    }
}