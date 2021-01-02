package com.nurbk.ps.finalproject.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.nurbk.ps.finalproject.Utlis.NotificationUtils;

import com.nurbk.ps.finalproject.Utlis.SharedPreferenceHelper;
import com.nurbk.ps.finalproject.network.NetworkCurrency;
import com.nurbk.ps.finalproject.repository.MainRepository;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobServiceBackground extends JobService {

    private NetworkCurrency networkCurrency = NetworkCurrency.getInstance(this);

    private SharedPreferences sharedPreferences;

    @Override
    public boolean onStartJob(JobParameters params) {


        sharedPreferences = getSharedPreferences(SharedPreferenceHelper.PREF_FILE_NAME, Context.MODE_PRIVATE);
        boolean visable = sharedPreferences
                .getBoolean(SharedPreferenceHelper.PREF_IMAGE_VISABLE, false);
        networkCurrency.getApi().getCurrency(sharedPreferences.
                        getString(SharedPreferenceHelper.PREF_BASE, "USD"),
                sharedPreferences.getString(SharedPreferenceHelper.
                        PREF_SYMBOLS, "EUD"))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            dataConverter(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

        MainRepository.getInstance(this).baseCall(sharedPreferences.
                        getString(SharedPreferenceHelper.PREF_BASE, "USD"),
                sharedPreferences.getString(SharedPreferenceHelper.
                        PREF_SYMBOLS, "EUD"));
        if (visable) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            ComponentName componentName = new ComponentName(this, JobServiceBackground.class);
            JobInfo jobInfo = new JobInfo.Builder(2, componentName)
                    .setRequiresBatteryNotLow(true)
                    .setMinimumLatency(1000 * 50*60)
                    .setOverrideDeadline(1000 * 60*60)
                    .build();
            jobScheduler.schedule(jobInfo);


        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    void dataConverter(String data) throws JSONException {

        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonObjectRates = jsonObject.getJSONObject("rates");
        Float converterValue = (float) jsonObjectRates.getDouble(sharedPreferences.getString(SharedPreferenceHelper.PREF_SYMBOLS, "EUR"));



        float value = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences().getFloat(SharedPreferenceHelper.PREF_PRICE, 0);
        float textValue = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences()
                .getFloat(SharedPreferenceHelper.PREF_TEXT, 0);
        float priceConverter = textValue * converterValue;

        SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                .putFloat(SharedPreferenceHelper.PREF_PRICE, priceConverter).apply();

        SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                .putFloat(SharedPreferenceHelper.PREF_TEXT, textValue).apply();

        float price = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences()
                .getFloat(SharedPreferenceHelper.PREF_TEXT, 0);

        String base = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences()
                .getString(SharedPreferenceHelper.PREF_BASE, "USD");

        String symbols = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences()
                .getString(SharedPreferenceHelper.PREF_SYMBOLS, "EUR");

        if (value != priceConverter) {
            NotificationUtils.createMainNotificationChannel(this);
            NotificationUtils.showBasicNotification(this, "Exchange Rates", "Price of " + price + " " + base + " for the " + symbols + " " + value);
        }
    }
}
