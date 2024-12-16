package fpl.md37.genz_fashion.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderRequest {

    @SerializedName("id_client")
    private String idClient;

    @SerializedName("name_user")
    private String nameUser;

    @SerializedName("phone_user")
    private String phoneUser;

    @SerializedName("address_user")
    private String addressUser;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("products")
    private List<ProducItem> products;

    @SerializedName("total_amount")
    private double totalAmount;

    public OrderRequest(String idClient, String nameUser, String phoneUser, String addressUser, String paymentMethod, List<ProducItem> products, double totalAmount) {
        this.idClient = idClient;
        this.nameUser = nameUser;
        this.phoneUser = phoneUser;
        this.addressUser = addressUser;
        this.paymentMethod = paymentMethod;
        this.products = products;
        this.totalAmount = totalAmount;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getAddressUser() {
        return addressUser;
    }

    public void setAddressUser(String addressUser) {
        this.addressUser = addressUser;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<ProducItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProducItem> products) {
        this.products = products;
    }

    public double getTotalAmount() { // Getter cho totalAmount
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) { // Setter cho totalAmount
        this.totalAmount = totalAmount;
    }
}
