package com.example.rick.rickbakker_pset6;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 *
 */

class PaintingListAdapter extends ArrayAdapter<Object> {

    private final Activity context;
    private final String[] titles;
    private final String[] makers;

    PaintingListAdapter(Activity context, String[] titles, String[] makers) {
        super(context, R.layout.my_mood_list, titles);

        this.context = context;
        this.titles = titles;
        this.makers = makers;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_mood_list, null, true);

        TextView title = (TextView) rowView.findViewById(R.id.title_text);
        TextView maker = (TextView) rowView.findViewById(R.id.maker_text);

        title.setText(titles[position]);
        maker.setText(makers[position]);
        return rowView;
    }
}

