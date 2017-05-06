package com.wings.intelligentclass.domain;

import java.util.List;

/**
 * Created by wing on 2017/4/27.
 */

public class User {

    private String account;
    private String name;
    private String password;
    private String email;
    private String number;
    private String description;
    private List<String> classes;

    public User(String account, String name, String password, String email, String number, String description) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.email = email;
        this.number = number;
        this.description = description;
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
