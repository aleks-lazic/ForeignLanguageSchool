package ch.hes.foreignlanguageschool.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ch.hes.foreignlanguageschool.Activities.AssignmentActivity;
import ch.hes.foreignlanguageschool.Activities.LectureActivity;
import ch.hes.foreignlanguageschool.DAO.Assignment;
import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DB.DBAssignment;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listViewLectures;
    private ListView listViewAssignments;

    //the home will display all the lectures and assignments OF THE DAY
    private ArrayList<Lecture> lectures;
    private ArrayList<Assignment> assignments;

    //classes needed for the database
    private DatabaseHelper db;
    private DBLecture dbLecture;
    private DBAssignment dbAssignment;

    private String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());


    public TodayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        //Everything related to the database
        db = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        dbLecture = new DBLecture(db);
        dbAssignment = new DBAssignment(db);

        //SQL to get every lecture for current day
        lectures = dbLecture.getLecturesForCurrentDateInHome(currentDate);

        //SQL to get every lecture for current day
        assignments = dbAssignment.getAllAssignmentsForSpecialDate(currentDate);

        // Set the list of lectures
        listViewLectures = (ListView) view.findViewById(R.id.home_lectures);

        listViewLectures.setAdapter(new ArrayAdapter<Lecture>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, lectures));

        listViewLectures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), LectureActivity.class);
                DBStudent dbStudent = new DBStudent(db);

                Lecture lecture = (Lecture) parent.getItemAtPosition(position);
                lecture.setStudentsList(dbStudent.getStudentsListByLecture(lecture.getId()));
                myIntent.putExtra("lecture", lecture);

                startActivity(myIntent);
            }
        });

        // Set the list of assignments
        listViewAssignments = (ListView) view.findViewById(R.id.home_assignments);

        listViewAssignments.setAdapter(new ArrayAdapter<Assignment>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, assignments));

        listViewAssignments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), AssignmentActivity.class);

                Assignment assignment = (Assignment) parent.getItemAtPosition(position);
                myIntent.putExtra("assignment", assignment);

                startActivity(myIntent);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        lectures = dbLecture.getLecturesForCurrentDateInHome(currentDate);

        listViewLectures.setAdapter(new ArrayAdapter<Lecture>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, lectures));

        assignments = dbAssignment.getAllAssignmentsForSpecialDate(currentDate);

        listViewAssignments.setAdapter(new ArrayAdapter<Assignment>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, assignments));


    }
}
