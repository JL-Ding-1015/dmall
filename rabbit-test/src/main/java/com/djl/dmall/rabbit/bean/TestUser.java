package com.djl.dmall.rabbit.bean;

import java.io.Serializable;

public class TestUser implements Serializable {
    private String username;
    private String email;

    public TestUser() {
    }

    public TestUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
