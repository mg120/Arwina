package com.tamiuz.arwina.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderProductDataModel implements Parcelable {

    private int product_id;
    private int trader_id;
    private String deliver_address;
    private double deliver_lat;
    private double deliver_lng;
    private String product_count;
    private int order_price;
    private int deliver_company;
    private String order_date;
    private String order_time;
    private String other_notes;

    public OrderProductDataModel() {
    }

    protected OrderProductDataModel(Parcel in) {
        product_id = in.readInt();
        trader_id = in.readInt();
        deliver_address = in.readString();
        deliver_lat = in.readDouble();
        deliver_lng = in.readDouble();
        product_count = in.readString();
        order_price = in.readInt();
        deliver_company = in.readInt();
        order_date = in.readString();
        order_time = in.readString();
        other_notes = in.readString();
    }

    public static final Creator<OrderProductDataModel> CREATOR = new Creator<OrderProductDataModel>() {
        @Override
        public OrderProductDataModel createFromParcel(Parcel in) {
            return new OrderProductDataModel(in);
        }

        @Override
        public OrderProductDataModel[] newArray(int size) {
            return new OrderProductDataModel[size];
        }
    };

    // setter and getters
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getTrader_id() {
        return trader_id;
    }

    public void setTrader_id(int trader_id) {
        this.trader_id = trader_id;
    }

    public String getDeliver_address() {
        return deliver_address;
    }

    public void setDeliver_address(String deliver_address) {
        this.deliver_address = deliver_address;
    }

    public double getDeliver_lat() {
        return deliver_lat;
    }

    public void setDeliver_lat(double deliver_lat) {
        this.deliver_lat = deliver_lat;
    }

    public double getDeliver_lng() {
        return deliver_lng;
    }

    public void setDeliver_lng(double deliver_lng) {
        this.deliver_lng = deliver_lng;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public int getOrder_price() {
        return order_price;
    }

    public void setOrder_price(int order_price) {
        this.order_price = order_price;
    }

    public int getDeliver_company() {
        return deliver_company;
    }

    public void setDeliver_company(int deliver_company) {
        this.deliver_company = deliver_company;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOther_notes() {
        return other_notes;
    }

    public void setOther_notes(String other_notes) {
        this.other_notes = other_notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(product_id);
        dest.writeInt(trader_id);
        dest.writeString(deliver_address);
        dest.writeDouble(deliver_lat);
        dest.writeDouble(deliver_lng);
        dest.writeString(product_count);
        dest.writeInt(order_price);
        dest.writeInt(deliver_company);
        dest.writeString(order_date);
        dest.writeString(order_time);
        dest.writeString(other_notes);
    }
}
