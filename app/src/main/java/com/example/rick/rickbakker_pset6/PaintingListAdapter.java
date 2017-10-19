package com.example.rick.rickbakker_pset6;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Created by Rick on 19-10-2017.
 */

public class PaintingListAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] itemname;
    //private final Integer[] imgid;

    public PaintingListAdapter(Activity context, String[] itemname) {
        super(context, R.layout.my_mood_list, itemname);

        this.context = context;
        this.itemname = itemname;
        //this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_mood_list, null, true);


        //ImageView painting = (ImageView) rowView.findViewById(R.id.paintimg);
        TextView title = (TextView) rowView.findViewById(R.id.title_text);
        TextView maker = (TextView) rowView.findViewById(R.id.maker_text);
        TextView link = (TextView) rowView.findViewById(R.id.link_text);

        title.setText(itemname[position]);
        //painting.setImageResource(imgid[position]);
        maker.setText(itemname[position]);
        link.setText(itemname[position]);
        return rowView;

    }
}

