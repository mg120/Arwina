package com.tamiuz.arwina.Models;

import com.google.gson.annotations.SerializedName;

public class EditProductResponseModel {
    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private productDataObj data;

    // getters
    public Boolean getMessage() {
        return message;
    }

    public productDataObj getData() {
        return data;
    }

    // class productDataObj
    public class productDataObj{
        @SerializedName("id")
        private Integer id;
        @SerializedName("trader_id")
        private Integer trader_id;
        @SerializedName("title")
        private String title;
        @SerializedName("price")
        private Integer price;
        @SerializedName("qty")
        private Integer qty;
        @SerializedName("maxqty")
        private Integer maxqty;
        @SerializedName("address")
        private String address;
        @SerializedName("desc")
        private String desc;
        @SerializedName("image")
        private String image;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("suspensed")
        private Integer suspensed;

        // getters
        public Integer getId() {
            return id;
        }

        public Integer getTrader_id() {
            return trader_id;
        }

        public String getTitle() {
            return title;
        }

        public Integer getPrice() {
            return price;
        }

        public Integer getQty() {
            return qty;
        }

        public Integer getMaxqty() {
            return maxqty;
        }

        public String getAddress() {
            return address;
        }

        public String getDesc() {
            return desc;
        }

        public String getImage() {
            return image;
        }

        public String getCreated_at() {
            return created_at;
        }

        public Integer getSuspensed() {
            return suspensed;
        }
    }
}
