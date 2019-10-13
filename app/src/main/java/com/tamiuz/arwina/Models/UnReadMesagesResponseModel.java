package com.tamiuz.arwina.Models;

import com.google.gson.annotations.SerializedName;

public class UnReadMesagesResponseModel {

    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private Integer data;

    // getters

    public Boolean getMessage() {
        return message;
    }

    public Integer getData() {
        return data;
    }
}
