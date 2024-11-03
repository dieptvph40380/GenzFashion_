package fpl.md37.genz_fashion.api;

import java.util.ArrayList;
import java.util.List;

import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.Response;
import fpl.md37.genz_fashion.models.Size;
import fpl.md37.genz_fashion.models.Suppliers;
import fpl.md37.genz_fashion.models.TypeProduct;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    public static String BASE_URL="http://10.0.2.2:3000/api/";
    //listTypeProduct
    @GET("typeproduct")
    Call<Response<ArrayList<TypeProduct>>> getAlltypeproduct();
    //listSize
    @GET("get-list-size")
    Call<Response<ArrayList<Size>>> getAllSizes();
    //add type
    @Multipart
    @POST("add-type")
    Call<ResponseBody> addTypeProduct(
            @Part("name") RequestBody name,
            @Part("id_size") RequestBody sizes,
            @Part MultipartBody.Part image
    );
    //deleteTypeProduct
    @DELETE("delete-typeproduct-by-id/{id}")
    Call<Response<TypeProduct>>deleteTypeProduct(@Path("id") String id);
    //updateTypeProduct
    @Multipart
    @PUT("update-typeproduct/{id}")
    Call<ResponseBody> updateTypeProduct(
            @Path("id") String id,
            @Part("name") RequestBody name,
            @Part("id_size") RequestBody sizes,
            @Part MultipartBody.Part image
    );
    //list suppliers
    @GET("suppliers")
    Call<Response<ArrayList<Suppliers>>> getAllsuppliers();
    //add supplier
    @Multipart
    @POST("add-supplier")
    Call<ResponseBody> addSuppliers(
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("email") RequestBody email,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image
    );

    //delete supplier
    @DELETE("delete-supplier-by-id/{id}")
    Call<Response<Suppliers>> deleteSuppliers(@Path("id") String id);
    @Multipart
    @PUT("update-supplier/{id}")
    Call<ResponseBody> updateSupplier(
            @Path("id") String id,
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone,
            @Part("email") RequestBody email,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image
    );
    //listProduct
    @GET("prodct")
    Call<Response<ArrayList<Product>>> getAllProducts();
    //add product
    @Multipart
    @POST("add-product")
    Call<Response<Product>> addProduct(
            @Part("product_name") RequestBody product_name,
            @Part("price") RequestBody price,
            @Part("quantity") RequestBody quantity,
            @Part("state") RequestBody state,
            @Part("description") RequestBody description,
            @Part("id_suppliers") RequestBody suppliers,
            @Part("id_producttype") RequestBody typeproducts,
            @Part ArrayList<MultipartBody.Part> image
    );


}
