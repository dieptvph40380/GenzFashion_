package fpl.md37.genz_fashion.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
      private String id, product_name, quantity, price, description;
      private boolean state;
      private ArrayList<String> image;

      @SerializedName("id_suppliers")
      private String suppliersId;

      @SerializedName("id_producttype")
      private String typeProductId;

      public Product() {
      }

      public Product(String id, String product_name, String quantity, String price, String description, boolean state, ArrayList<String> image, String suppliersId, String typeProductId) {
            this.id = id;
            this.product_name = product_name;
            this.quantity = quantity;
            this.price = price;
            this.description = description;
            this.state = state;
            this.image = image;
            this.suppliersId = suppliersId;
            this.typeProductId = typeProductId;
      }

      public String getId() {
            return id;
      }

      public void setId(String id) {
            this.id = id;
      }

      public String getProduct_name() {
            return product_name;
      }

      public void setProduct_name(String product_name) {
            this.product_name = product_name;
      }

      public String getQuantity() {
            return quantity;
      }

      public void setQuantity(String quantity) {
            this.quantity = quantity;
      }

      public String getPrice() {
            return price;
      }

      public void setPrice(String price) {
            this.price = price;
      }

      public String getDescription() {
            return description;
      }

      public void setDescription(String description) {
            this.description = description;
      }

      public boolean isState() {
            return state;
      }

      public void setState(boolean state) {
            this.state = state;
      }

      public ArrayList<String> getImage() {
            return image;
      }

      public void setImage(ArrayList<String> image) {
            this.image = image;
      }

      public String getSuppliersId() {
            return suppliersId;
      }

      public void setSuppliersId(String suppliersId) {
            this.suppliersId = suppliersId;
      }

      public String getTypeProductId() {
            return typeProductId;
      }

      public void setTypeProductId(String typeProductId) {
            this.typeProductId = typeProductId;
      }
}