package com.assignment.CountryDetails.data.LivedataDB;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.assignment.CountryDetails.data.DB.CountryDatabase;
import com.assignment.CountryDetails.data.models.CountryDetailsResponseMainModel;
import com.assignment.CountryDetails.data.models.CountryDetailsRow;
import com.assignment.CountryDetails.data.models.CountrySingleton;
import com.assignment.CountryDetails.network.RestApiService;
import com.assignment.CountryDetails.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class CountryRepository {
    private ArrayList<CountryDetailsRow> arrayDetailsList = new ArrayList<>();
    private MutableLiveData<List<CountryDetailsRow>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public CountryRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<CountryDetailsRow>> getMutableLiveData() {
        RestApiService apiService = RetrofitInstance.getApiService();
        Call<CountryDetailsResponseMainModel> call = apiService.getCountryDetailsData();
        call.enqueue(new Callback<CountryDetailsResponseMainModel>() {
            @Override
            public void onResponse(Call<CountryDetailsResponseMainModel> call, Response<CountryDetailsResponseMainModel> response) {
                CountryDetailsResponseMainModel mCountryDetailsResponseMainModel = response.body();

                CountrySingleton.getInstance().setHeading(mCountryDetailsResponseMainModel.getTitle().toString());

                if (mCountryDetailsResponseMainModel != null && mCountryDetailsResponseMainModel.getRows() != null) {
                    arrayDetailsList = (ArrayList<CountryDetailsRow>) mCountryDetailsResponseMainModel.getRows();
                    mutableLiveData.setValue(arrayDetailsList);
                }
            }
            @Override
            public void onFailure(Call<CountryDetailsResponseMainModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return mutableLiveData;
    }
}
