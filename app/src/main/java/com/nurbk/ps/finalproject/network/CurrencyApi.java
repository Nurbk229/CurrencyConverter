package com.nurbk.ps.finalproject.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyApi {


    @GET("latest")
    public Call<ResponseBody> getCurrency(
            @Query("base") String base,
                @Query("symbols") String symbols
    );

}
