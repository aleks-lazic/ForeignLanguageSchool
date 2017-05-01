package ch.hes.foreignlanguageschool.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.DAO.Assignment;
import ch.hes.foreignlanguageschool.R;

/**
 * Created by Aleksandar on 25.04.2017.
 */

public class CustomAdapterAssignment extends ArrayAdapter<Assignment> {

    private final Activity activity;
    private ArrayList<Assignment> assignments;

    public CustomAdapterAssignment(Activity activity, ArrayList<Assignment> assignments) {
        super(activity, R.layout.listview_image_text, assignments);

        this.activity = activity;
        this.assignments = assignments;

    }

    public View getView(int position, View view, ViewGroup parent) {

        View v = view;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_image_text, null);
        }

        Assignment assignment = (Assignment) getItem(position);

        if (assignment != null) {
            TextView txtTitle = (TextView) v.findViewById(R.id.calendar_lectures_name);
            ImageView imgLecture = (ImageView) v.findViewById(R.id.calendar_lectures_image);

            txtTitle.setText(assignments.get(position).toString());
            int id = activity.getResources().getIdentifier("ch.hes.foreignlanguageschool:drawable/" + assignments.get(position).getImageName(), null, null);
            imgLecture.setImageResource(id);

        }

        return v;

    }


}
