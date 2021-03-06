package com.application.ashes.Retrofit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignUp implements Serializable {
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("email")
    @Expose
    public String email;



    @SerializedName("id")
    @Expose
    public String id;



    public SignUp() {
    }

    public SignUp(String name, String email, String password) {
        this.name = name;
        this.email = email;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }






    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
