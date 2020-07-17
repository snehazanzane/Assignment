package com.assignment.CountryDetails.network;

import com.assignment.CountryDetails.data.models.CountryDetailsResponseMainModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApiService {
    @GET(NetworkAPIUrls.GET_COUNTRY_DETAILS_URL)
    Call<CountryDetailsResponseMainModel> getCountryDetailsData();
}
