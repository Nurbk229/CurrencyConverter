package com.nurbk.ps.finalproject.ui.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.ExtendedCurrency;
import com.nurbk.ps.finalproject.R;
import com.nurbk.ps.finalproject.Utlis.SharedPreferenceHelper;
import com.nurbk.ps.finalproject.databinding.ActivityMainBinding;
import com.nurbk.ps.finalproject.model.ApiError;
import com.nurbk.ps.finalproject.service.JobServiceBackground;
import com.nurbk.ps.finalproject.ui.viewmodel.MainViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding mainBinding;
    private MainViewModel viewModel;
    private TextView textView;
    private ImageView imageView;

    private String base = "", symbols = "";
    private int i = 1, imageBase = 0, imageSymbols = 0;
    boolean imageVisa;
    ExtendedCurrency[] currenciess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(MainViewModel.class);


        CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
        currenciess = ExtendedCurrency.CURRENCIES; //Array of all currencies

        getData();
        picker.setListener((name, code, symbol, flagDrawableResID) -> {
            // Implement your code here
            imageView.setImageResource(flagDrawableResID);
            picker.dismiss();
            textView.setText(code);
            if (i == 1) {
                base = code;
                imageBase = flagDrawableResID;
            } else if (i == 2) {
                symbols = code;
                imageSymbols = flagDrawableResID;
            }
            SharedPreferenceHelper.getInstance(this).setData(base, symbols, imageBase, imageSymbols);

        });


        mainBinding.imageView.setOnClickListener(v -> {
            imageView = (ImageView) v;
            textView = mainBinding.txtConverter;
            picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
            i = 1;
        });
        mainBinding.imageView2.setOnClickListener(v -> {
            imageView = (ImageView) v;
            textView = mainBinding.txtConverter2;
            picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
            i = 2;
        });


        mainBinding.button.setOnClickListener(v -> {

            viewModel.getLoginRepository().baseCall(base, symbols);
        });

        viewModel.getLoginRepository().getLoginLiveData().observe(this, result -> {
            switch (result.status) {
                case ERROR:
                    mainBinding.progressBar.setVisibility(View.GONE);
                    ApiError error = (ApiError) result.data;
                    Snackbar.make(mainBinding.contaner, error.getMessage(), Snackbar.LENGTH_LONG).show();
                    break;
                case SUCCESS:
                    mainBinding.progressBar.setVisibility(View.GONE);
                    try {
                        dataConverter(result.data.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    mainBinding.progressBar.setVisibility(View.VISIBLE);

                    break;
            }
        });


        imageVisa = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences()
                .getBoolean(SharedPreferenceHelper.PREF_IMAGE_VISABLE, false);
        if (imageVisa) {
            mainBinding.ImageVisable.setImageResource(R.drawable.ic_baseline_visibility_24);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startJobService();
            }
        } else {
            mainBinding.ImageVisable.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }

        mainBinding.ImageVisable.setOnClickListener(
                v -> {
                    SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                            .putBoolean(SharedPreferenceHelper.PREF_IMAGE_VISABLE, !imageVisa).apply();
                    if (imageVisa) {
                        mainBinding.ImageVisable.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    } else {
                        mainBinding.ImageVisable.setImageResource(R.drawable.ic_baseline_visibility_24);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startJobService();
                        }
                    }
                    imageVisa = !imageVisa;
                }
        );

        mainBinding.floatingActionButton2.setOnClickListener(v -> covertBaseToSymbols());

    }

    void dataConverter(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonObjectRates = jsonObject.getJSONObject("rates");
        Float converterValue = (float) jsonObjectRates.getDouble(symbols);
        float valueText = Float.valueOf(mainBinding.txtPrice.getText().toString());
        float priceConverter = valueText * converterValue;
        mainBinding.txtValue.setText(priceConverter + "");

        SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                .putFloat(SharedPreferenceHelper.PREF_PRICE, priceConverter).apply();
        SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                .putFloat(SharedPreferenceHelper.PREF_TEXT, valueText).apply();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startJobService() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, JobServiceBackground.class);
        JobInfo jobInfo = new JobInfo.Builder(2, componentName)
                .setRequiresBatteryNotLow(true)
                .setMinimumLatency(1000 * 50 * 60)
                .setOverrideDeadline(1000 * 60 * 60)
                .build();
        jobScheduler.schedule(jobInfo);
    }


    private void getData() {
        float value = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences().getFloat(SharedPreferenceHelper.PREF_PRICE, 0);

        float price = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences()
                .getFloat(SharedPreferenceHelper.PREF_TEXT, 0);

        imageBase = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences().getInt(SharedPreferenceHelper.PREF_IMAGE_BASE, currenciess[1].getFlag());


        imageSymbols = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences().getInt(SharedPreferenceHelper.PREF_IMAGE_SYMBOLS, currenciess[2].getFlag());

        base = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences().getString(SharedPreferenceHelper.PREF_BASE, currenciess[1].getCode());


        symbols = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences().getString(SharedPreferenceHelper.PREF_SYMBOLS, currenciess[2].getCode());

        mainBinding.imageView.setImageResource(imageBase);
        mainBinding.imageView2.setImageResource(imageSymbols);

        mainBinding.txtConverter.setText(base);
        mainBinding.txtConverter2.setText(symbols);
        mainBinding.txtPrice.setText(price + "");
        mainBinding.txtValue.setText(value + "");

        viewModel.getLoginRepository().baseCall(base, symbols);
    }

    private void covertBaseToSymbols() {


        covertData(symbols,
                base,
                imageBase,
                imageSymbols,
                false);


        String base = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences().getString(SharedPreferenceHelper.PREF_BASE, "USD");


        String symbols = SharedPreferenceHelper.getInstance(this)
                .getSharedPreferences().getString(SharedPreferenceHelper.PREF_SYMBOLS, "EUR");

        mainBinding.txtConverter.setText(base);
        mainBinding.txtConverter2.setText(symbols);
    }

    private void covertData(String base,
                            String symbols,
                            int imageSymbols,
                            int imageBase,
                            boolean isConvert) {
        SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                .putString(SharedPreferenceHelper.PREF_BASE, base).apply();
        SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                .putString(SharedPreferenceHelper.PREF_SYMBOLS, symbols).apply();
        SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                .putInt(SharedPreferenceHelper.PREF_IMAGE_BASE, imageBase).apply();
        SharedPreferenceHelper.getInstance(this).getSharedPreferences().edit()
                .putInt(SharedPreferenceHelper.PREF_IMAGE_SYMBOLS, imageSymbols).apply();


        getData();
    }
}