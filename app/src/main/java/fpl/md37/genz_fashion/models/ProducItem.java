package fpl.md37.genz_fashion.models;

import com.google.gson.annotations.SerializedName;

public class ProducItem {
    @SerializedName("productId")
    private Product productId;

    @SerializedName("sizeId")
    private Size  sizeId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("isSelected")
    private boolean isSelected;

    public ProducItem(Product productId, Size  sizeId, int quantity, boolean isSelected) {
        this.productId = productId;
        this.sizeId = sizeId;
        this.quantity = quantity;
        this.isSelected = isSelected;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Size  getSizeId() {
        return sizeId;
    }

    public void setSizeId(Size  sizeId) {
        this.sizeId = sizeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
