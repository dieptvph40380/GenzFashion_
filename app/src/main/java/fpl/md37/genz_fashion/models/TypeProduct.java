package fpl.md37.genz_fashion.models;

import com.google.gson.annotations.SerializedName;

public class TypeProduct {
    @SerializedName("_id")
    private String id;
    private String name;
    private String image;
    private String id_size;

    public TypeProduct() {
    }

    public TypeProduct(String id, String name, String image, String id_size) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.id_size = id_size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId_size() {
        return id_size;
    }

    public void setId_size(String id_size) {
        this.id_size = id_size;
    }
}
