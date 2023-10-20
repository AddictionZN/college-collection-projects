package com.example.mongonav.Models;

public class User {

    private String username;
    private String email;
    private String password;
    private String transport;
    private String system;
    private String mode;

    public User() {

    }

    public User(String username, String email, String password, String transport, String system, String mode) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.transport = transport;
        this.system = system;
        this.mode = mode;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
