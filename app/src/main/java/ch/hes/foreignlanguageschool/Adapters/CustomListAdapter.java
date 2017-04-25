package ch.hes.foreignlanguageschool.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ch.hes.foreignlanguageschool.R;

import static android.R.attr.id;

/**
 * Created by Aleksandar on 25.04.2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemName;
    private final String[] imageName;

    public CustomListAdapter(Activity context, String[] itemName, String[] imageName) {
        super(context, R.layout.listview_image_text, itemName);

        this.context = context;
        this.itemName = itemName;
        this.imageName = imageName;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_image_text, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.calendar_lectures_name);
        ImageView imgLecture = (ImageView) rowView.findViewById(R.id.calendar_lectures_image);

        txtTitle.setText(itemName[position]);
        int id = context.getResources().getIdentifier("ch.hes.foreignlanguageschool:drawable/"+imageName[position], null, null);
        imgLecture.setImageResource(id);

        Log.d("Aleks", "" + id);
        return rowView;

    }
}
