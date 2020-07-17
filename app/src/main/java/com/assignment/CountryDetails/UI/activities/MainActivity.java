package com.assignment.CountryDetails.UI.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
import java.util.zip.Inflater;

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

        /**
         * Binding Data to list view online/offline
         */
        if (NetworkUtility.isConnected(MainActivity.this)) {
            getCountryDetailsData();
        } else {
            NetworkUtility.showAlert(MainActivity.this);
            selLocalOfflineDatabase();
        }

        /***
         * Pull to refresh country listview
         */
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

        //Fetch the data from Rest API with live data
        mainViewModel.getAll().observe(this, new Observer<List<CountryDetailsRow>>() {
            @Override
            public void onChanged(@Nullable List<CountryDetailsRow> list) {
                //complete the pull to refresh functionality
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
        mCountryDetailsAdapter.setArrayCountryDetails(arr);
        mCountryDetailsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //Display confirmation dialog for deleting the records
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("");
                builder.setMessage(getString(R.string.msg_delete_record_confirmation))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.str_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                mCountryDatabase.countryDao().deleteAllRows();
                                mCountryDetailsAdapter.removeAll();
                            }
                        })
                        .setNegativeButton(getString(R.string.str_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    
}