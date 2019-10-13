package com.tamiuz.arwina.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliversResponseModel {

    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private List<DeliverData> data;

    //getters
    public Boolean getMessage() {
        return message;
    }

    public List<DeliverData> getData() {
        return data;
    }


    // class DeliverData
    public class DeliverData {
        @SerializedName("id")
        private Integer id;
        @SerializedName("role")
        private Integer role;
        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        private String phone;
        @SerializedName("image")
        private String image;
        @SerializedName("carimage")
        private String carimage;
        @SerializedName("licenseimage")
        private String licenseimage;
        @SerializedName("identityimage")
        private String identityimage;
        @SerializedName("lat")
        private String lat;
        @SerializedName("lng")
        private String lng;
        @SerializedName("address")
        private String address;
        @SerializedName("promotioncode")
        private String promotioncode;
        @SerializedName("firebase_token")
        private String firebase_token;
        @SerializedName("suspensed")
        private Integer suspensed;

        // getters
        public Integer getId() {
            return id;
        }

        public Integer getRole() {
            return role;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getImage() {
            return image;
        }

        public String getCarimage() {
            return carimage;
        }

        public String getLicenseimage() {
            return licenseimage;
        }

        public String getIdentityimage() {
            return identityimage;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }

        public String getAddress() {
            return address;
        }

        public String getPromotioncode() {
            return promotioncode;
        }

        public String getFirebase_token() {
            return firebase_token;
        }

        public Integer getSuspensed() {
            return suspensed;
        }
    }
}
