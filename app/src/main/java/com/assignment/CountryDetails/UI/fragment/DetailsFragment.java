package com.assignment.CountryDetails.UI.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.CountryDetails.R;
import com.assignment.CountryDetails.UI.activities.MainActivity;
import com.assignment.CountryDetails.data.models.CountryDetailsRow;
import com.assignment.CountryDetails.data.models.CountrySingleton;
import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment {

    View view;

    TextView textHeading, textDetails;
    ImageView image;

    CountryDetailsRow obj;

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailsFragment newInstance(int index, CountryDetailsRow objCountryDetails) {
        DetailsFragment f = new DetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putSerializable("obj", objCountryDetails);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index");
    }

    public CountryDetailsRow getShownObject() {
        return (CountryDetailsRow) getArguments().getSerializable("obj");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(""+getString(R.string.str_fragment_details));
        view = inflater.inflate(R.layout.fragment_details, container, false);
        init();
        setData();
        return view;
    }

    private void init() {
        textHeading = view.findViewById(R.id.textTitle_DetailsFragment);
        textDetails = view.findViewById(R.id.textDescription_DetailsFragment);
        image = view.findViewById(R.id.image_DetailsFragment);
    }

    private void setData() {
        obj = getShownObject();
        textHeading.setText(obj.getTitle());
        textDetails.setText(obj.getDescription());
        Picasso.get()
                .load(obj.getImageHref())
                .placeholder(R.drawable.icon_default_image)
                .into(image);

    }

}