package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Cuong on 12/1/2015.
 */
public class QBOQueryItemResponse implements Parcelable {
    protected List<QBOItem> Item;
    protected Integer startPosition;
    protected Integer maxResults;
    protected Integer totalCount;

    public QBOQueryItemResponse() {
    }

    public List<QBOItem> getItem() {
        return Item;
    }

    public void setItem(List<QBOItem> item) {
        Item = item;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Item);
        dest.writeValue(this.startPosition);
        dest.writeValue(this.maxResults);
        dest.writeValue(this.totalCount);
    }

    protected QBOQueryItemResponse(Parcel in) {
        this.Item = in.createTypedArrayList(QBOItem.CREATOR);
        this.startPosition = (Integer)in.readValue(Integer.class.getClassLoader());
        this.maxResults = (Integer)in.readValue(Integer.class.getClassLoader());
        this.totalCount = (Integer)in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<QBOQueryItemResponse> CREATOR = new Parcelable.Creator<QBOQueryItemResponse>() {
        public QBOQueryItemResponse createFromParcel(Parcel source) {
            return new QBOQueryItemResponse(source);
        }

        public QBOQueryItemResponse[] newArray(int size) {
            return new QBOQueryItemResponse[size];
        }
    };
}
