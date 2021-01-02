package com.nurbk.ps.finalproject.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nurbk.ps.finalproject.Utlis.Result;
import com.nurbk.ps.finalproject.model.ApiError;
import com.nurbk.ps.finalproject.network.NetworkCurrency;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainRepository {

    private NetworkCurrency networkApi;


    private MutableLiveData<Result> loginLiveData = new MutableLiveData<>();
    private Call<ResponseBody> baseCall;
    private Context context;

    private MainRepository(Context context) {
        networkApi = NetworkCurrency.getInstance(context);
        this.context = context;
    }

    private static MainRepository instance;

    public static MainRepository getInstance(Context context) {
        if (instance == null)
            instance = new MainRepository(context);
        return instance;
    }

    public void baseCall(String baseCurrency, String symbols) {
        loginLiveData.postValue(Result.loading(true));

        baseCall = networkApi.getApi().getCurrency(baseCurrency, symbols);

        baseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        loginLiveData.postValue(Result.success(response.body().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ApiError error = networkApi.parseHttpError(response);
                    loginLiveData.postValue(Result.error(error.getMessage(), error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ApiError error = networkApi.parseNetworkError(t);
                loginLiveData.postValue(Result.error(error.getMessage(), error));
            }
        });
    }


    public LiveData<Result> getLoginLiveData() {
        return loginLiveData;
    }

}
