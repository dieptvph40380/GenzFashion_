package fpl.md37.genz_fashion.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvaluationRequest2 {
    @SerializedName("id_client")
    private String idClient;

    @SerializedName("product")
    private List<ProducItem> product;

    @SerializedName("text")
    private String reviewText;

    @SerializedName("stars")
    private int stars;


    public EvaluationRequest2(String idClient, List<ProducItem> product, String reviewText, int stars) {
        this.idClient = idClient;
        this.product = product;
        this.reviewText = reviewText;
        this.stars = stars;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public List<ProducItem> getProduct() {
        return product;
    }

    public void setProduct(List<ProducItem> product) {
        this.product = product;
    }



    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
