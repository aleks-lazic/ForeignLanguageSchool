package ch.hes.foreignlanguageschool.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.Activities.StudentActivity;
import ch.hes.foreignlanguageschool.Activities.StudentEdit;
import ch.hes.foreignlanguageschool.Adapters.CustomAdapterStudent;
import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView mListView;

    private ArrayList<Student> students;

    private DatabaseHelper db;
    private DBStudent dbStudent;
    private DBLecture dbLecture;

    private CustomAdapterStudent adapterStudent;

    private FloatingActionButton fab;

    public StudentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentsFragment newInstance(String param1, String param2) {
        StudentsFragment fragment = new StudentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_students, container, false);

        // Set the list of assignments
        mListView = (ListView) view.findViewById(R.id.students_list);

        // Everything linked to the DB
        db = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        dbStudent = new DBStudent(db);

        students = dbStudent.getAllStudents();

        // Set a custom adapter
        adapterStudent = new CustomAdapterStudent(getActivity(), students);

        mListView.setAdapter(adapterStudent);

        // Set the listener to switch to the activity when an item is selected
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent myIntent = new Intent(view.getContext(), StudentActivity.class);

                Student student = (Student) parent.getItemAtPosition(position);
                dbLecture = new DBLecture(db);
                student.setLecturesList(dbLecture.getLecturesForStudent(student.getId()));

                myIntent.putExtra("student", student);

                startActivity(myIntent);
            }
        });

        // Show a floating action button
        fab = (FloatingActionButton) view.findViewById(R.id.fragment_fab_students);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StudentEdit.class);
                startActivity(intent);
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

    @Override
    public void onResume() {
        super.onResume();
        updateDisplay();
    }

    /**
     * Update the display when the user comes back
     */
    public void updateDisplay() {
        students = dbStudent.getAllStudents();

        adapterStudent = new CustomAdapterStudent(getActivity(), students);

        mListView.setAdapter(adapterStudent);

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
}
