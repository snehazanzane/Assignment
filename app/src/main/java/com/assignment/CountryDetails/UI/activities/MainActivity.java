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
import com.assignment.CountryDetails.data.DB.CountryDatabase;
import com.assignment.CountryDetails.data.adapter.CountryDetailsAdapter;
import com.assignment.CountryDetails.data.LivedataDB.MainViewModel;
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

    CountryDatabase mCountryDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        if (NetworkUtility.isConnected(MainActivity.this)) {
            getCountryDetailsData();
        } else {
            NetworkUtility.showAlert(MainActivity.this);
            selLocalOfflineDatabase();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtility.isConnected(MainActivity.this)) {
                    getCountryDetailsData();
                } else {
                    NetworkUtility.showAlert(MainActivity.this);
                    selLocalOfflineDatabase();
                }
            }
        });
    }

    private void initViews() {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setActionbarTitle(CountrySingleton.getInstance().getHeading());

        mCountryDatabase = CountryDatabase.getAppDatabase(MainActivity.this);

        listView = findViewById(R.id.listview_MainActivity);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_MainActivity);

        mCountryDetailsAdapter = new CountryDetailsAdapter(getApplicationContext(), new ArrayList<>());
        listView.setAdapter(mCountryDetailsAdapter);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    public void getCountryDetailsData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mainViewModel.getAll().observe(this, new Observer<List<CountryDetailsRow>>() {
            @Override
            public void onChanged(@Nullable List<CountryDetailsRow> list) {
                mSwipeRefreshLayout.setRefreshing(false);
                setActionbarTitle(CountrySingleton.getInstance().getHeading());
                //Setting data to the listview
                prepareListViewData(list);
            }
        });

    }

    private void prepareListViewData(List<CountryDetailsRow> countryDetailsList) {

        if (countryDetailsList != null) {
            mCountryDetailsAdapter.setArrayCountryDetails(countryDetailsList);
            //Insert data into Local DB
            mCountryDatabase.countryDao().insertAll((ArrayList<CountryDetailsRow>) countryDetailsList);
        } else {
            Toast.makeText(this, "" + getString(R.string.str_no_more_data_availble), Toast.LENGTH_SHORT).show();
        }
        mCountryDetailsAdapter.notifyDataSetChanged();

    }

    private void setActionbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void selLocalOfflineDatabase() {
        ArrayList<CountryDetailsRow> arr = new ArrayList<>();
        arr = (ArrayList<CountryDetailsRow>) mCountryDatabase.countryDao().getAllRec();
        System.out.println("Size : " + arr.size());

        mCountryDetailsAdapter.setArrayCountryDetails(arr);
        mCountryDetailsAdapter.notifyDataSetChanged();
    }

}