package com.example.edrobe;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private final String itemName;
    private final String itemPrice;
    private final String imageUrl;
    private int quantity;

    public CartItem(String itemName, String itemPrice, String itemImageUrl) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.imageUrl = itemImageUrl;
        this.quantity = 1;
    }

    protected CartItem(Parcel in) {
        itemName = in.readString();
        itemPrice = in.readString();
        imageUrl = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeString(itemPrice);
        dest.writeString(imageUrl);
        dest.writeInt(quantity);
    }
}
