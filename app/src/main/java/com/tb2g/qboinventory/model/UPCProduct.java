package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cuong on 12/1/2015.
 */
public class UPCProduct implements Parcelable {

    boolean valid;
    String number;
    String itemname;
    String alias;
    String description;
    String avg_price;
    int rate_up;
    int rate_down;
    String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getNumber() {
        return number.replaceFirst("^0+(?!$)", ""); //remove leading 0
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public int getRate_up() {
        return rate_up;
    }

    public void setRate_up(int rate_up) {
        this.rate_up = rate_up;
    }

    public int getRate_down() {
        return rate_down;
    }

    public void setRate_down(int rate_down) {
        this.rate_down = rate_down;
    }

    public UPCProduct() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(valid ? (byte)1 : (byte)0);
        dest.writeString(this.number);
        dest.writeString(this.itemname);
        dest.writeString(this.alias);
        dest.writeString(this.description);
        dest.writeString(this.avg_price);
        dest.writeInt(this.rate_up);
        dest.writeInt(this.rate_down);
        dest.writeString(this.reason);
    }

    protected UPCProduct(Parcel in) {
        this.valid = in.readByte() != 0;
        this.number = in.readString();
        this.itemname = in.readString();
        this.alias = in.readString();
        this.description = in.readString();
        this.avg_price = in.readString();
        this.rate_up = in.readInt();
        this.rate_down = in.readInt();
        this.reason = in.readString();
    }

    public static final Creator<UPCProduct> CREATOR = new Creator<UPCProduct>() {
        public UPCProduct createFromParcel(Parcel source) {
            return new UPCProduct(source);
        }

        public UPCProduct[] newArray(int size) {
            return new UPCProduct[size];
        }
    };
}
