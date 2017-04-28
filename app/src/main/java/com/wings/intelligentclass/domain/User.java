package com.wings.intelligentclass.domain;

import java.util.List;

/**
 * Created by wing on 2017/4/27.
 */

public class User {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String description;
    private List<String> classes;

    public User(String username, String password, String email, String phone, String description) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.description = description;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }
}
