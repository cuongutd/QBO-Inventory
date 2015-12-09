package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cuong on 12/9/2015.
 */
public class UPCSearchProduct implements Parcelable {
    private String productname;
    private String imageurl;
    private String producturl;
    private String price;
    private String currency;
    private String saleprice;
    private String storename;
    private String upc;

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getProducturl() {
        return producturl;
    }

    public void setProducturl(String producturl) {
        this.producturl = producturl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public UPCSearchProduct() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productname);
        dest.writeString(this.imageurl);
        dest.writeString(this.producturl);
        dest.writeString(this.price);
        dest.writeString(this.currency);
        dest.writeString(this.saleprice);
        dest.writeString(this.storename);
        dest.writeString(this.upc);
    }

    protected UPCSearchProduct(Parcel in) {
        this.productname = in.readString();
        this.imageurl = in.readString();
        this.producturl = in.readString();
        this.price = in.readString();
        this.currency = in.readString();
        this.saleprice = in.readString();
        this.storename = in.readString();
        this.upc = in.readString();
    }

    public static final Creator<UPCSearchProduct> CREATOR = new Creator<UPCSearchProduct>() {
        public UPCSearchProduct createFromParcel(Parcel source) {
            return new UPCSearchProduct(source);
        }

        public UPCSearchProduct[] newArray(int size) {
            return new UPCSearchProduct[size];
        }
    };
}
