package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cuong on 12/2/2015.
 */
public class QBOResponse implements Parcelable {
    private QBOCompanyInfo CompanyInfo;
    private QBOQueryItemResponse QueryResponse;
    private QBOItem Item;
    private String time;
    private String errorCode;
    private String errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public QBOCompanyInfo getCompanyInfo() {
        return CompanyInfo;
    }

    public void setCompanyInfo(QBOCompanyInfo companyInfo) {
        CompanyInfo = companyInfo;
    }

    public QBOItem getItem() {
        return Item;
    }

    public void setItem(QBOItem item) {
        Item = item;
    }

    public QBOQueryItemResponse getQueryResponse() {
        return QueryResponse;
    }

    public void setQueryResponse(QBOQueryItemResponse queryResponse) {
        QueryResponse = queryResponse;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public QBOResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.CompanyInfo, 0);
        dest.writeParcelable(this.QueryResponse, 0);
        dest.writeParcelable(this.Item, 0);
        dest.writeString(this.time);
        dest.writeString(this.errorCode);
        dest.writeString(this.errorMsg);
    }

    protected QBOResponse(Parcel in) {
        this.CompanyInfo = in.readParcelable(QBOCompanyInfo.class.getClassLoader());
        this.QueryResponse = in.readParcelable(QBOQueryItemResponse.class.getClassLoader());
        this.Item = in.readParcelable(QBOItem.class.getClassLoader());
        this.time = in.readString();
        this.errorCode = in.readString();
        this.errorMsg = in.readString();
    }

    public static final Creator<QBOResponse> CREATOR = new Creator<QBOResponse>() {
        public QBOResponse createFromParcel(Parcel source) {
            return new QBOResponse(source);
        }

        public QBOResponse[] newArray(int size) {
            return new QBOResponse[size];
        }
    };
}
