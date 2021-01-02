package com.nurbk.ps.finalproject.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.nurbk.ps.finalproject.repository.MainRepository;

public class MainViewModel extends AndroidViewModel {
    private MainRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository =  MainRepository.getInstance(application.getApplicationContext());

    }

    public MainRepository getLoginRepository() {
        return repository;
    }
}
