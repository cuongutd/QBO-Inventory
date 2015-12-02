package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cuong on 12/1/2015.
 */
public class UPCItem implements Parcelable {
    private String sku;
    private String productName;
    private String description;
    private String price;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sku);
        dest.writeString(this.productName);
        dest.writeString(this.description);
        dest.writeString(this.price);
    }

    public UPCItem() {
    }

    protected UPCItem(Parcel in) {
        this.sku = in.readString();
        this.productName = in.readString();
        this.description = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<UPCItem> CREATOR = new Parcelable.Creator<UPCItem>() {
        public UPCItem createFromParcel(Parcel source) {
            return new UPCItem(source);
        }

        public UPCItem[] newArray(int size) {
            return new UPCItem[size];
        }
    };
}
