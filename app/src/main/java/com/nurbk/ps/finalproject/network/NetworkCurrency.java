package com.nurbk.ps.finalproject.network;

import android.content.Context;

import com.nurbk.ps.finalproject.R;
import com.nurbk.ps.finalproject.model.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NetworkCurrency {

    private final String BASE_URL = "https://api.exchangeratesapi.io/";
    private Retrofit retrofit;
    private CurrencyApi api;

    Context context;

    private NetworkCurrency(Context context) {
        this.context = context;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .build();

        api = retrofit.create(CurrencyApi.class);

    }


    private static NetworkCurrency instance;

    public static NetworkCurrency getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkCurrency(context);
        }
        return instance;
    }


    public CurrencyApi getApi() {
        return api;
    }


    public ApiError parseHttpError(Response<?> response) {

        ApiError error;
        try {
            assert response.errorBody() != null;
            error = new ApiError(response.code(), context.getString(R.string.error_code));
            if (error != null) {
                error.setCode(response.code());
            }
        } catch (NullPointerException e) {
            return new ApiError(context.getString(R.string.error_connection_failed));

        }

        return error;
    }

    public ApiError parseNetworkError(Throwable throwable) {
        if (throwable instanceof IOException) {
            return new ApiError(context.getString(R.string.error_connection_failed));
        }
        return new ApiError(context.getString(R.string.error_unexpected));
    }

}
