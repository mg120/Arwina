package com.tamiuz.arwina.Models;

import com.google.gson.annotations.SerializedName;

public class RegisterResponseModel {

    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private LoginDataObj data;

    //Getters
    public Boolean getMessage() {
        return message;
    }

    public LoginDataObj getData() {
        return data;
    }

    // class LoginDataObj
    public static class LoginDataObj {
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
        @SerializedName("commercialreg")
        private String commercialreg;
        @SerializedName("commercialregno")
        private String commercialregno;
        @SerializedName("address")
        private String address;
        @SerializedName("havedelivery")
        private Integer havedelivery;
        @SerializedName("balance")
        private Integer balance;
        @SerializedName("forgetcode")
        private String forgetcode;
        @SerializedName("promotioncode")
        private String promotioncode;
        @SerializedName("myorderscount")
        private Integer myorderscount;

        // Getters
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

        public String getCommercialreg() {
            return commercialreg;
        }

        public String getCommercialregno() {
            return commercialregno;
        }

        public String getAddress() {
            return address;
        }

        public Integer getHavedelivery() {
            return havedelivery;
        }

        public Integer getBalance() {
            return balance;
        }

        public String getForgetcode() {
            return forgetcode;
        }

        public String getPromotioncode() {
            return promotioncode;
        }

        public Integer getMyorderscount() {
            return myorderscount;
        }
    }
}
