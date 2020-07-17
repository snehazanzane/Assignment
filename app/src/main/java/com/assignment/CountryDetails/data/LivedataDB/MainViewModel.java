package com.assignment.CountryDetails.data.LivedataDB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.assignment.CountryDetails.data.DB.CountryDatabase;
import com.assignment.CountryDetails.data.models.CountryDetailsRow;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private CountryRepository countryRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        countryRepository = new CountryRepository(application);
    }

    /**
     * getAll
     * @return Live data array
     */
    public LiveData<List<CountryDetailsRow>> getAll() {
        return countryRepository.getMutableLiveData();
    }
}
