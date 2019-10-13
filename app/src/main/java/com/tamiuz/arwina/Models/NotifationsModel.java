package com.tamiuz.arwina.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotifationsModel {

    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private List<messageData> data;
//    @SerializedName("message")
//    private List<messageData> message;

    public Boolean getMessage() {
        return message;
    }

    public List<messageData> getData() {
        return data;
    }

    // messageData ....
    public static class messageData {
        @SerializedName("id")
        private int id;
        @SerializedName("user_id")
        private int user_id;
        @SerializedName("notification")
        private String notification;
        @SerializedName("created_at")
        private String created_at;


        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getNotification() {
            return notification;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
