package com.tamiuz.arwina.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersModel {
    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private List<OrderData> data;

    // getters
    public Boolean getMessage() {
        return message;
    }

    public List<OrderData> getData() {
        return data;
    }

    // class OrderData
    public static class OrderData implements Parcelable{
        @SerializedName("id")
        private Integer id;
        @SerializedName("order_number")
        private String order_number;
        @SerializedName("user_id")
        private Integer user_id;
        @SerializedName("company_id")
        private Integer company_id;
        @SerializedName("item_name")
        private String item_name;
        @SerializedName("item_image")
        private String item_image;
        @SerializedName("item_id")
        private Integer item_id;
        @SerializedName("placelat")
        private String placelat;
        @SerializedName("placelng")
        private String placelng;
        @SerializedName("placeaddress")
        private String placeaddress;
        @SerializedName("qty")
        private Integer qty;
        @SerializedName("price")
        private String price;
        @SerializedName("delivercompany")
        private String delivercompany;
        @SerializedName("deliverdate")
        private String deliverdate;
        @SerializedName("timetype")
        private String timetype;
        @SerializedName("desc")
        private String desc;
        @SerializedName("status")
        private Integer status;
        @SerializedName("created_at")
        private String created_at;


        protected OrderData(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            order_number = in.readString();
            if (in.readByte() == 0) {
                user_id = null;
            } else {
                user_id = in.readInt();
            }
            if (in.readByte() == 0) {
                company_id = null;
            } else {
                company_id = in.readInt();
            }
            item_name = in.readString();
            item_image = in.readString();
            if (in.readByte() == 0) {
                item_id = null;
            } else {
                item_id = in.readInt();
            }
            placelat = in.readString();
            placelng = in.readString();
            placeaddress = in.readString();
            if (in.readByte() == 0) {
                qty = null;
            } else {
                qty = in.readInt();
            }
            price = in.readString();
            delivercompany = in.readString();
            deliverdate = in.readString();
            timetype = in.readString();
            desc = in.readString();
            if (in.readByte() == 0) {
                status = null;
            } else {
                status = in.readInt();
            }
            created_at = in.readString();
        }

        public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
            @Override
            public OrderData createFromParcel(Parcel in) {
                return new OrderData(in);
            }

            @Override
            public OrderData[] newArray(int size) {
                return new OrderData[size];
            }
        };

        // getters
        public Integer getId() {
            return id;
        }

        public String getOrder_number() {
            return order_number;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public Integer getCompany_id() {
            return company_id;
        }

        public Integer getItem_id() {
            return item_id;
        }

        public String getPlacelat() {
            return placelat;
        }

        public String getPlacelng() {
            return placelng;
        }

        public String getPlaceaddress() {
            return placeaddress;
        }

        public Integer getQty() {
            return qty;
        }

        public String getPrice() {
            return price;
        }

        public String getDelivercompany() {
            return delivercompany;
        }

        public String getDeliverdate() {
            return deliverdate;
        }

        public String getTimetype() {
            return timetype;
        }

        public String getDesc() {
            return desc;
        }

        public String getItem_image() {
            return item_image;
        }

        public Integer getStatus() {
            return status;
        }

        public String getCreated_at() {
            return created_at;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(id);
            }
            dest.writeString(order_number);
            if (user_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(user_id);
            }
            if (company_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(company_id);
            }
            dest.writeString(item_name);
            dest.writeString(item_image);
            if (item_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(item_id);
            }
            dest.writeString(placelat);
            dest.writeString(placelng);
            dest.writeString(placeaddress);
            if (qty == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(qty);
            }
            dest.writeString(price);
            dest.writeString(delivercompany);
            dest.writeString(deliverdate);
            dest.writeString(timetype);
            dest.writeString(desc);
            if (status == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(status);
            }
            dest.writeString(created_at);
        }
    }
}
