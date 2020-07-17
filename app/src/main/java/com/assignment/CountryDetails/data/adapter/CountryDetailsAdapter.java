package com.assignment.CountryDetails.data.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.CountryDetails.R;
import com.assignment.CountryDetails.data.models.CountryDetailsRow;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CountryDetailsAdapter extends ArrayAdapter<CountryDetailsRow> {
    private List<CountryDetailsRow> arrayCountryDetails;
    Context context;

    public CountryDetailsAdapter(Context context, List<CountryDetailsRow> arrayCountryDetails) {

        super(context, 0, arrayCountryDetails);
        this.context = context;
        this.arrayCountryDetails = arrayCountryDetails;
    }

    public void setArrayCountryDetails(List<CountryDetailsRow> arrayCountryDetails) {
        this.arrayCountryDetails = arrayCountryDetails;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CountryDetailsRow object = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_item, parent, false);
        }

        TextView textTitle = (TextView) convertView.findViewById(R.id.textTitle_ListViewItem);
        TextView textDescription = (TextView) convertView.findViewById(R.id.textDescription_ListViewItem);
        ImageView imageViewDetails = (ImageView) convertView.findViewById(R.id.imageDescription_ListViewItem);

        if (object.getTitle() == null) {
            textTitle.setText(context.getString(R.string.hint_no_data));
        } else {
            textTitle.setText(object.getTitle());
        }

        if (object.getDescription() == null) {
            textDescription.setText(context.getString(R.string.hint_no_data));
        } else {
            textDescription.setText(object.getDescription());
        }

        Picasso.get()
                .load(object.getImageHref())
                .placeholder(R.drawable.icon_default_image)
                .into(imageViewDetails);

        // Return the completed view to render on screen
        return convertView;
    }


}
