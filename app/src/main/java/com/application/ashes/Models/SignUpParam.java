package com.application.ashes.Models;

public class SignUpParam {
    String name;
    String email;
    String password;

    public SignUpParam(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public SignUpParam(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
