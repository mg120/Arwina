package com.tamiuz.arwina.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllCompaniesModel {

    @SerializedName("message")
    private Boolean message;
    @SerializedName("data")
    private DataObj data;

    //getters
    public Boolean getMessage() {
        return message;
    }

    public DataObj getData() {
        return data;
    }

    // class DataObj
    public class DataObj{
        @SerializedName("companies")
        private List<CompanyData> companies;
        @SerializedName("sliders")
        private List<CompanyData> sliders;

        // gettters
        public List<CompanyData> getCompanies() {
            return companies;
        }

        public List<CompanyData> getSliders() {
            return sliders;
        }
    }
    // class CompanyData
    public static class CompanyData implements Parcelable {
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
        @SerializedName("rate")
        private Integer rate;
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
        @SerializedName("suspensed")
        private Integer suspensed;


        protected CompanyData(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            if (in.readByte() == 0) {
                role = null;
            } else {
                role = in.readInt();
            }
            name = in.readString();
            phone = in.readString();
            image = in.readString();
            if (in.readByte() == 0) {
                rate = null;
            } else {
                rate = in.readInt();
            }
            commercialreg = in.readString();
            commercialregno = in.readString();
            address = in.readString();
            if (in.readByte() == 0) {
                havedelivery = null;
            } else {
                havedelivery = in.readInt();
            }
            if (in.readByte() == 0) {
                balance = null;
            } else {
                balance = in.readInt();
            }
            if (in.readByte() == 0) {
                suspensed = null;
            } else {
                suspensed = in.readInt();
            }
        }

        public static final Creator<CompanyData> CREATOR = new Creator<CompanyData>() {
            @Override
            public CompanyData createFromParcel(Parcel in) {
                return new CompanyData(in);
            }

            @Override
            public CompanyData[] newArray(int size) {
                return new CompanyData[size];
            }
        };

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

        public Integer getRate() {
            return rate;
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
            if (role == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(role);
            }
            dest.writeString(name);
            dest.writeString(phone);
            dest.writeString(image);
            if (rate == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(rate);
            }
            dest.writeString(commercialreg);
            dest.writeString(commercialregno);
            dest.writeString(address);
            if (havedelivery == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(havedelivery);
            }
            if (balance == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(balance);
            }
            if (suspensed == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(suspensed);
            }
        }
    }
}
