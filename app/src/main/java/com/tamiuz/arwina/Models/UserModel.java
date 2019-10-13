package com.tamiuz.arwina.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel  implements Parcelable{

    private int id;
    private int role;
    private String name;
    private String phone;
    private String image;
    private String commercialreg;
    private String commercialregno;
    private String promotioncode;
    private String address;
    private String forgetcode;
    private int havedelivery;
    private int balance;
    private int suspensed;
    private int myorderscount;

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        id = in.readInt();
        role = in.readInt();
        name = in.readString();
        phone = in.readString();
        image = in.readString();
        commercialreg = in.readString();
        commercialregno = in.readString();
        promotioncode = in.readString();
        address = in.readString();
        forgetcode = in.readString();
        havedelivery = in.readInt();
        balance = in.readInt();
        suspensed = in.readInt();
        myorderscount = in.readInt();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCommercialreg() {
        return commercialreg;
    }

    public void setCommercialreg(String commercialreg) {
        this.commercialreg = commercialreg;
    }

    public String getCommercialregno() {
        return commercialregno;
    }

    public void setCommercialregno(String commercialregno) {
        this.commercialregno = commercialregno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getForgetcode() {
        return forgetcode;
    }

    public void setForgetcode(String forgetcode) {
        this.forgetcode = forgetcode;
    }

    public String getPromotioncode() {
        return promotioncode;
    }

    public void setPromotioncode(String promotioncode) {
        this.promotioncode = promotioncode;
    }

    public int getHavedelivery() {
        return havedelivery;
    }

    public void setHavedelivery(int havedelivery) {
        this.havedelivery = havedelivery;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getSuspensed() {
        return suspensed;
    }

    public void setSuspensed(int suspensed) {
        this.suspensed = suspensed;
    }

    public int getMyorderscount() {
        return myorderscount;
    }

    public void setMyorderscount(int myorderscount) {
        this.myorderscount = myorderscount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(role);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(image);
        dest.writeString(commercialreg);
        dest.writeString(commercialregno);
        dest.writeString(promotioncode);
        dest.writeString(address);
        dest.writeString(forgetcode);
        dest.writeInt(havedelivery);
        dest.writeInt(balance);
        dest.writeInt(suspensed);
        dest.writeInt(myorderscount);
    }
}
