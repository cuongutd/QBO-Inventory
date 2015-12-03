package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cuong on 12/3/2015.
 */
public class QBOCompanyInfo implements Parcelable {

    private String CompanyName;
    private String Id;

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CompanyName);
        dest.writeString(this.Id);
    }

    public QBOCompanyInfo() {
    }

    protected QBOCompanyInfo(Parcel in) {
        this.CompanyName = in.readString();
        this.Id = in.readString();
    }

    public static final Parcelable.Creator<QBOCompanyInfo> CREATOR = new Parcelable.Creator<QBOCompanyInfo>() {
        public QBOCompanyInfo createFromParcel(Parcel source) {
            return new QBOCompanyInfo(source);
        }

        public QBOCompanyInfo[] newArray(int size) {
            return new QBOCompanyInfo[size];
        }
    };
}
