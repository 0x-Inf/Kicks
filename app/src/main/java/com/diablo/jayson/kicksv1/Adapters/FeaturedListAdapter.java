package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diablo.jayson.kicksv1.R;

import java.util.List;

public class FeaturedListAdapter extends ArrayAdapter {


    public FeaturedListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = View.inflate(getContext(),R.layout.featured_list_item,parent);
        return root;
    }
}
