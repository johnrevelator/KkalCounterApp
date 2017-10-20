package inc.ak.kkalcounter.rest;


import java.util.List;

import inc.ak.kkalcounter.model.Eating;
import inc.ak.kkalcounter.model.Product;
import inc.ak.kkalcounter.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkInterface {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @POST("tables/Products")
    Call<Product> addProduct(@Body Product product);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @POST("tables/Eating")
    Call<Eating> addEating(@Body Eating eating);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @DELETE("tables/Eating/{id}")
    Call<Eating> deleteEating(@Path("id") String bookId);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @GET("tables/Products")
    Call<List<Product>> getProductList(@Query("$skip") Integer skip,@Query("$top") Integer top);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @GET("tables/Eating")
    Call<List<Eating>> getEatingList(@Query("$filter") String filter);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @GET("tables/Eating")
    Call<List<Eating>> getEating(@Query("$filter") String filter,@Query("$orderby") String orderby);
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @GET("tables/Products")
    Call<List<Product>> getProductListFilter(@Query("$filter") String filter,@Query("$skip") Integer skip,@Query("$top") Integer top);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @GET("tables/Users")
    Call<List<User>> getUser(@Query("$filter") String filter);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @POST("tables/Users")
    Call<User> setUser(@Body User user);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "zumo-api-version: 2.0.0"
    })
    @PATCH("tables/Users/{id}")
    Call<User> updateUser(@Path("id") String id,@Body User user);


}
