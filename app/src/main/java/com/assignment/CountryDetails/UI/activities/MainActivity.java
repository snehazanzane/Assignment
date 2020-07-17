package com.assignment.CountryDetails.UI.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.assignment.CountryDetails.R;
import com.assignment.CountryDetails.data.adapter.CountryDetailsAdapter;
import com.assignment.CountryDetails.data.db.MainViewModel;
import com.assignment.CountryDetails.data.models.CountryDetailsRow;
import com.assignment.CountryDetails.data.models.CountrySingleton;
import com.assignment.CountryDetails.network.NetworkUtility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    MainViewModel mainViewModel;

    CountryDetailsAdapter mCountryDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        if (NetworkUtility.isConnected(MainActivity.this)) {
            getCountryDetailsData();
        } else {
            NetworkUtility.showAlert(MainActivity.this);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkUtility.isConnected(MainActivity.this)) {
                    getCountryDetailsData();
                } else {
                    NetworkUtility.showAlert(MainActivity.this);
                }

            }
        });
    }

    private void initViews() {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(CountrySingleton.getInstance().getHeading());

        listView = findViewById(R.id.listview_MainActivity);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_MainActivity);

        mCountryDetailsAdapter = new CountryDetailsAdapter(getApplicationContext(), new ArrayList<>());
        listView.setAdapter(mCountryDetailsAdapter);
    }

    public void getCountryDetailsData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mainViewModel.getAll().observe(this, new Observer<List<CountryDetailsRow>>() {
            @Override
            public void onChanged(@Nullable List<CountryDetailsRow> list) {
                mSwipeRefreshLayout.setRefreshing(false);

                getSupportActionBar().setTitle(CountrySingleton.getInstance().getHeading());

                prepareListViewData(list);


            }
        });

    }

    private void prepareListViewData(List<CountryDetailsRow> countryDetailsList) {

        if (countryDetailsList != null) {
            mCountryDetailsAdapter.setArrayCountryDetails(countryDetailsList);
        } else {
            Toast.makeText(this, "" + getString(R.string.str_no_more_data_availble), Toast.LENGTH_SHORT).show();
        }
        mCountryDetailsAdapter.notifyDataSetChanged();

    }

}