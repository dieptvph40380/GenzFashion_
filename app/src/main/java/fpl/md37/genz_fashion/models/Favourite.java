package fpl.md37.genz_fashion.models;

import com.google.gson.annotations.SerializedName;

public class Favourite {


    private String name;
    private String id_client;
    private String id_product;

    public Favourite() {
    }

    public Favourite(String name, String id_product, String id_client) {
        this.name = name;
        this.id_product = id_product;
        this.id_client = id_client;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
