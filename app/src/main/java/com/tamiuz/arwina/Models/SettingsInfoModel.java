package com.tamiuz.arwina.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingsInfoModel {

    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private InfoData data;

    //getters
    public Boolean getMessage() {
        return message;
    }

    public InfoData getData() {
        return data;
    }


    public class InfoData{
        @SerializedName("info")
        private List<InfoDataModel> info;

        //getter
        public List<InfoDataModel> getInfo() {
            return info;
        }
    }

    // class InfoDataModel
    public static class InfoDataModel {
        @SerializedName("id")
        private Integer id;
        @SerializedName("arprivacy")
        private String arprivacy;
        @SerializedName("arabout")
        private String arabout;
        @SerializedName("arconditions")
        private String arconditions;
        @SerializedName("commission")
        private Integer commission;
        @SerializedName("logo")
        private String logo;
        @SerializedName("facebook")
        private String facebook;
        @SerializedName("twitter")
        private String twitter;
        @SerializedName("instagram")
        private String instagram;
        @SerializedName("youtube")
        private String youtube;

        // Getters
        public Integer getId() {
            return id;
        }

        public String getArprivacy() {
            return arprivacy;
        }

        public String getArabout() {
            return arabout;
        }

        public String getArconditions() {
            return arconditions;
        }

        public Integer getCommission() {
            return commission;
        }

        public String getLogo() {
            return logo;
        }

        public String getFacebook() {
            return facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getInstagram() {
            return instagram;
        }

        public String getYoutube() {
            return youtube;
        }
    }
}
