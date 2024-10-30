package fpl.md37.genz_fashion.api;

import java.util.ArrayList;

import fpl.md37.genz_fashion.models.Response;
import fpl.md37.genz_fashion.models.Size;
import fpl.md37.genz_fashion.models.TypeProduct;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    public static String BASE_URL="http://10.0.2.2:3000/api/";
    @GET("typeproduct")
    Call<Response<ArrayList<TypeProduct>>> getAlltypeproduct();
    @GET("get-list-size")
    Call<Response<ArrayList<Size>>> getAllSizes();
}
