package com.example.du_an_dads.Api;

import com.example.du_an_dads.Model.User_model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @FormUrlEncoded
    @POST("access/signup")
    Call<User_model> addPerson(
            @Field("username") String username,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("password") String password

    );

    @GET("access/login")
    Call<List<User_model>> getListUser(
     @Query("access/login") String key
    );
}
