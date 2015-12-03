package com.tb2g.qboinventory.model;

/**
 * Created by Cuong on 12/2/2015.
 */
public enum QBOItemTypeEnum {

    ASSEMBLY("Assembly"),
    CATEGORY("Category"),
    FIXED_ASSET("Fixed Asset"),
    GROUP("Group"),
    INVENTORY("Inventory"),
    NON_INVENTORY("NonInventory"),
    OTHER_CHARGE("Other Charge"),
    PAYMENT("Payment"),
    SERVICE("Service"),
    SUBTOTAL("Subtotal"),
    DISCOUNT("Discount"),
    TAX("Tax"),
    TAX_GROUP("Tax Group");

    private final String value;

    private QBOItemTypeEnum(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static QBOItemTypeEnum fromValue(String v) {
        QBOItemTypeEnum[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            QBOItemTypeEnum c = arr$[i$];
            if (c.value.equals(v)) {
                return c;
            }
        }

        throw new IllegalArgumentException(v);
    }

}