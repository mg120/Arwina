package com.tamiuz.arwina.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsModel {

    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private List<ProductData> data;

    //getters
    public Boolean getMessage() {
        return message;
    }

    public List<ProductData> getData() {
        return data;
    }


    // class ProductData
    public static class ProductData implements Parcelable {
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


        protected ProductData(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            if (in.readByte() == 0) {
                trader_id = null;
            } else {
                trader_id = in.readInt();
            }
            title = in.readString();
            if (in.readByte() == 0) {
                price = null;
            } else {
                price = in.readInt();
            }
            if (in.readByte() == 0) {
                qty = null;
            } else {
                qty = in.readInt();
            }
            if (in.readByte() == 0) {
                maxqty = null;
            } else {
                maxqty = in.readInt();
            }
            address = in.readString();
            desc = in.readString();
            image = in.readString();
            created_at = in.readString();
            if (in.readByte() == 0) {
                suspensed = null;
            } else {
                suspensed = in.readInt();
            }
        }

        public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
            @Override
            public ProductData createFromParcel(Parcel in) {
                return new ProductData(in);
            }

            @Override
            public ProductData[] newArray(int size) {
                return new ProductData[size];
            }
        };

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
            if (trader_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(trader_id);
            }
            dest.writeString(title);
            if (price == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(price);
            }
            if (qty == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(qty);
            }
            if (maxqty == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(maxqty);
            }
            dest.writeString(address);
            dest.writeString(desc);
            dest.writeString(image);
            dest.writeString(created_at);
            if (suspensed == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(suspensed);
            }
        }
    }
}
