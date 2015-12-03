package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Cuong on 12/2/2015.
 */
public class QBOModificationMetadata implements Parcelable {

    protected String CreateTime;
    protected String LastUpdatedTime;

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getLastUpdatedTime() {
        return LastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        LastUpdatedTime = lastUpdatedTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CreateTime);
        dest.writeString(this.LastUpdatedTime);
    }

    public QBOModificationMetadata() {
    }

    protected QBOModificationMetadata(Parcel in) {
        this.CreateTime = in.readString();
        this.LastUpdatedTime = in.readString();
    }

    public static final Creator<QBOModificationMetadata> CREATOR = new Creator<QBOModificationMetadata>() {
        public QBOModificationMetadata createFromParcel(Parcel source) {
            return new QBOModificationMetadata(source);
        }

        public QBOModificationMetadata[] newArray(int size) {
            return new QBOModificationMetadata[size];
        }
    };
}
