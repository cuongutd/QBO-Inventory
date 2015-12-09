package com.tb2g.qboinventory.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tb2g.qboinventory.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Cuong on 12/2/2015.
 */
public class QBOItem implements Parcelable {

    protected String Name;
    protected String Sku;
    protected String Description;
    protected Boolean Active;
    protected Boolean SubItem;
    protected QBOReferenceType ParentRef;
    protected BigDecimal UnitPrice;
    protected String Type;
    protected QBOReferenceType IncomeAccountRef;
    protected String PurchaseDesc;
    protected BigDecimal PurchaseCost;
    protected QBOReferenceType ExpenseAccountRef;
    protected QBOReferenceType CogsAccountRef;
    protected QBOReferenceType AssetAccountRef;
    protected Boolean TrackQtyOnHand;
    protected BigDecimal QtyOnHand;
    protected String InvStartDate;
    protected String Id;
    protected QBOModificationMetadata MetaData;
    protected String SyncToken;

    public String getSyncToken() {
        return SyncToken;
    }

    public void setSyncToken(String syncToken) {
        SyncToken = syncToken;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        Sku = sku;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }

    public Boolean getSubItem() {
        return SubItem;
    }

    public void setSubItem(Boolean subItem) {
        SubItem = subItem;
    }

    public QBOReferenceType getParentRef() {
        return ParentRef;
    }

    public void setParentRef(QBOReferenceType parentRef) {
        ParentRef = parentRef;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public QBOReferenceType getIncomeAccountRef() {
        return IncomeAccountRef;
    }

    public void setIncomeAccountRef(QBOReferenceType incomeAccountRef) {
        IncomeAccountRef = incomeAccountRef;
    }

    public String getPurchaseDesc() {
        return PurchaseDesc;
    }

    public void setPurchaseDesc(String purchaseDesc) {
        PurchaseDesc = purchaseDesc;
    }

    public BigDecimal getPurchaseCost() {
        return PurchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        PurchaseCost = purchaseCost;
    }

    public QBOReferenceType getExpenseAccountRef() {
        return ExpenseAccountRef;
    }

    public void setExpenseAccountRef(QBOReferenceType expenseAccountRef) {
        ExpenseAccountRef = expenseAccountRef;
    }

    public QBOReferenceType getCogsAccountRef() {
        return CogsAccountRef;
    }

    public void setCogsAccountRef(QBOReferenceType cogsAccountRef) {
        CogsAccountRef = cogsAccountRef;
    }

    public QBOReferenceType getAssetAccountRef() {
        return AssetAccountRef;
    }

    public void setAssetAccountRef(QBOReferenceType assetAccountRef) {
        AssetAccountRef = assetAccountRef;
    }

    public Boolean getTrackQtyOnHand() {
        return TrackQtyOnHand;
    }

    public void setTrackQtyOnHand(Boolean trackQtyOnHand) {
        TrackQtyOnHand = trackQtyOnHand;
    }

    public BigDecimal getQtyOnHand() {
        return QtyOnHand;
    }

    public void setQtyOnHand(BigDecimal qtyOnHand) {
        QtyOnHand = qtyOnHand;
    }

    public String getInvStartDate() {
        return InvStartDate;
    }

    public void setInvStartDate(String invStartDate) {
        InvStartDate = invStartDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public QBOModificationMetadata getMetaData() {
        return MetaData;
    }

    public void setMetaData(QBOModificationMetadata metaData) {
        MetaData = metaData;
    }

    public static QBOItem fromUPCProduct(UPCProduct product){

        QBOItem item = new QBOItem();

        item.setName(product.getItemname());
        item.setDescription(product.getDescription());
        item.setTrackQtyOnHand(true);
        item.setType(QBOItemTypeEnum.INVENTORY.value());
        item.setActive(true);
        item.setSku(product.getNumber());
        item.setInvStartDate(DateUtil.formatDateToString(new Date()));
        item.setQtyOnHand(new BigDecimal(1));

        if (TextUtils.isEmpty(item.getName()))
            item.setName(product.getDescription());

        return item;

    }

    public static QBOItem fromUPCProduct(UPCSearchProduct product){

        QBOItem item = new QBOItem();

        item.setName(product.getProductname());
        item.setDescription(product.getProducturl());
        item.setTrackQtyOnHand(true);
        item.setType(QBOItemTypeEnum.INVENTORY.value());
        item.setActive(true);
        item.setSku(product.getUpc());
        item.setInvStartDate(DateUtil.formatDateToString(new Date()));
        item.setQtyOnHand(new BigDecimal(1));

        return item;

    }

    public QBOItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.Sku);
        dest.writeString(this.Description);
        dest.writeValue(this.Active);
        dest.writeValue(this.SubItem);
        dest.writeParcelable(this.ParentRef, 0);
        dest.writeSerializable(this.UnitPrice);
        dest.writeString(this.Type);
        dest.writeParcelable(this.IncomeAccountRef, 0);
        dest.writeString(this.PurchaseDesc);
        dest.writeSerializable(this.PurchaseCost);
        dest.writeParcelable(this.ExpenseAccountRef, 0);
        dest.writeParcelable(this.CogsAccountRef, 0);
        dest.writeParcelable(this.AssetAccountRef, 0);
        dest.writeValue(this.TrackQtyOnHand);
        dest.writeSerializable(this.QtyOnHand);
        dest.writeString(this.InvStartDate);
        dest.writeString(this.Id);
        dest.writeParcelable(this.MetaData, 0);
        dest.writeString(this.SyncToken);
    }

    protected QBOItem(Parcel in) {
        this.Name = in.readString();
        this.Sku = in.readString();
        this.Description = in.readString();
        this.Active = (Boolean)in.readValue(Boolean.class.getClassLoader());
        this.SubItem = (Boolean)in.readValue(Boolean.class.getClassLoader());
        this.ParentRef = in.readParcelable(QBOReferenceType.class.getClassLoader());
        this.UnitPrice = (BigDecimal)in.readSerializable();
        this.Type = in.readString();
        this.IncomeAccountRef = in.readParcelable(QBOReferenceType.class.getClassLoader());
        this.PurchaseDesc = in.readString();
        this.PurchaseCost = (BigDecimal)in.readSerializable();
        this.ExpenseAccountRef = in.readParcelable(QBOReferenceType.class.getClassLoader());
        this.CogsAccountRef = in.readParcelable(QBOReferenceType.class.getClassLoader());
        this.AssetAccountRef = in.readParcelable(QBOReferenceType.class.getClassLoader());
        this.TrackQtyOnHand = (Boolean)in.readValue(Boolean.class.getClassLoader());
        this.QtyOnHand = (BigDecimal)in.readSerializable();
        this.InvStartDate = in.readString();
        this.Id = in.readString();
        this.MetaData = in.readParcelable(QBOModificationMetadata.class.getClassLoader());
        this.SyncToken = in.readString();
    }

    public static final Creator<QBOItem> CREATOR = new Creator<QBOItem>() {
        public QBOItem createFromParcel(Parcel source) {
            return new QBOItem(source);
        }

        public QBOItem[] newArray(int size) {
            return new QBOItem[size];
        }
    };
}
