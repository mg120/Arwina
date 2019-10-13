package com.tamiuz.arwina.Models;

import com.google.gson.annotations.SerializedName;

public class ForgetPasswordModel {

    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private String data;

    //getters
    public Boolean getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
