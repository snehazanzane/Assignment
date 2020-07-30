package com.assignment.CountryDetails.UI.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.assignment.CountryDetails.R;
import com.assignment.CountryDetails.UI.activities.DetailsActivity;
import com.assignment.CountryDetails.UI.activities.MainActivity;
import com.assignment.CountryDetails.data.DB.CountryDatabase;
import com.assignment.CountryDetails.data.LivedataDB.MainViewModel;
import com.assignment.CountryDetails.data.adapter.CountryDetailsAdapter;
import com.assignment.CountryDetails.data.models.CountryDetailsRow;
import com.assignment.CountryDetails.data.models.CountrySingleton;
import com.assignment.CountryDetails.utilsFiles.NetworkUtility;
import com.assignment.CountryDetails.utilsFiles.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    View view;
    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MainViewModel mainViewModel;
    CountryDetailsAdapter mCountryDetailsAdapter;
    CountryDatabase mCountryDatabase;

    boolean dualPane;
    int curCheckPosition = 0;

//    OnCountryListSelectedListener mCountrySelectedListener;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);

        init();
        /**
         * Binding Data to list view online/offline
         */
        if (NetworkUtility.isConnected(getActivity())) {
            getCountryDetailsData();
        } else {
            NetworkUtility.showAlert(getActivity());
            selLocalOfflineDatabase();
        }

        /***
         * Pull to refresh country list
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtility.isConnected(getActivity())) {
                    getCountryDetailsData();
                } else {
                    NetworkUtility.showAlert(getActivity());
                    selLocalOfflineDatabase();
                }
            }
        });

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details_MainActivity);
        dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        //dualPane = true;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            curCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (dualPane) {
            // In dual-pane mode, the list view highlights the selected
            // item.
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(curCheckPosition);
        } else {
            // We also highlight in uni-pane just for fun
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setItemChecked(curCheckPosition, true);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showDetails(position);
            }
        });

        return view;
    }

    private void init() {
        mCountryDatabase = CountryDatabase.getAppDatabase(getActivity());

        listView = view.findViewById(R.id.listview_MainActivity);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_MainActivity);

        mCountryDetailsAdapter = new CountryDetailsAdapter(getActivity(), new ArrayList<>());
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
                ((MainActivity) getActivity())
                        .setActionbarTitle(CountrySingleton.getInstance().getHeading());
                SharedPref.getInstance(getActivity()).saveStringToSharedPref(getString(R.string.key_heading), CountrySingleton.getInstance().getHeading());
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
            Toast.makeText(getActivity(), "" + getActivity().getString(R.string.str_no_more_data_availble), Toast.LENGTH_SHORT).show();
        }
        mCountryDetailsAdapter.notifyDataSetChanged();

    }

    /**
     * Setting dat when Internet connection is not available
     */
    private void selLocalOfflineDatabase() {
        ((MainActivity) getActivity())
                .setActionbarTitle(SharedPref.getInstance(getActivity()).getSharedPref(getString(R.string.key_heading)));
        CountrySingleton.getInstance().setHeading(SharedPref.getInstance(getActivity()).getSharedPref(getString(R.string.key_heading)));
        ArrayList<CountryDetailsRow> arr = new ArrayList<>();
        arr = (ArrayList<CountryDetailsRow>) mCountryDatabase.countryDao().getAllRec();
        mCountryDetailsAdapter.setArrayCountryDetails(arr);
        mCountryDetailsAdapter.notifyDataSetChanged();
    }

    void showDetails(int index) {
        curCheckPosition = index;

        if (dualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            listView.setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            DetailsFragment details = (DetailsFragment)
                    getActivity().getSupportFragmentManager().findFragmentById(R.id.details_MainActivity);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(index, mCountryDetailsAdapter.getItem(index));

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                if (index == 0) {
                    ft.replace(R.id.details_MainActivity, details);
                } else {
                    ft.replace(R.id.a_item, details);
                }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            intent.putExtra("obj", mCountryDetailsAdapter.getItem(index));
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", curCheckPosition);
    }

}