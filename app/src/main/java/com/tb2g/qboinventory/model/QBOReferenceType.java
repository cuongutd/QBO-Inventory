package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cuong on 12/2/2015.
 */
public class QBOReferenceType implements Parcelable {

    protected String value;
    protected String name;
    protected String type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value);
        dest.writeString(this.name);
        dest.writeString(this.type);
    }

    public QBOReferenceType() {
    }

    protected QBOReferenceType(Parcel in) {
        this.value = in.readString();
        this.name = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<QBOReferenceType> CREATOR = new Parcelable.Creator<QBOReferenceType>() {
        public QBOReferenceType createFromParcel(Parcel source) {
            return new QBOReferenceType(source);
        }

        public QBOReferenceType[] newArray(int size) {
            return new QBOReferenceType[size];
        }
    };
}
