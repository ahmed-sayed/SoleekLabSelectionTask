package com.example.android.soleeklabselectiontask;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CountryAdapter extends ArrayAdapter<Country> {
    public CountryAdapter(Context context, List<Country> Countries) {
        super(context, 0, Countries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_view, parent, false);
        }

        Country current_Country = getItem(position);

        // name
        String name = current_Country.getmName();
        TextView name_textView = (TextView) listItemView.findViewById(R.id.name);
        name_textView.setText(name);

        // region
        String region = current_Country.getmRegion();
        TextView region_textView = (TextView) listItemView.findViewById(R.id.region);
        region_textView.setText(region);

        // subRegion
        String sub_region = current_Country.getmSubregion();
        TextView subRegion_textView = (TextView) listItemView.findViewById(R.id.sub_region);
        subRegion_textView.setText(sub_region);

        return listItemView;


    }
}
