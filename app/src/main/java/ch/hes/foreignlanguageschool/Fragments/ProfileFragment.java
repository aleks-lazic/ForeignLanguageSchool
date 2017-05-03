package ch.hes.foreignlanguageschool.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import ch.hes.foreignlanguageschool.Activities.NavigationActivity;
import ch.hes.foreignlanguageschool.Activities.StudentEdit;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtMail;

    private DatabaseHelper db;
    private DBTeacher dbTeacher;

    private FloatingActionButton fab;


    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
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
//      Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtFirstName = (EditText) view.findViewById(R.id.teacher_firstname);
        txtFirstName.setText(NavigationActivity.currentTeacher.getFirstName());

        txtLastName = (EditText) view.findViewById(R.id.teacher_lastname);
        txtLastName.setText(NavigationActivity.currentTeacher.getLastName());

        txtMail = (EditText) view.findViewById(R.id.teacher_mail);
        txtMail.setText(NavigationActivity.currentTeacher.getMail());

        setEditable(false);

        //create database objects
        db = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        dbTeacher = new DBTeacher(db);

        fab = (FloatingActionButton) view.findViewById(R.id.fragment_fab_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(true);
                setHasOptionsMenu(true);
                fab.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {

            if (!checkEverythingOnSaveClick()) {

                return super.onOptionsItemSelected(item);
            }

            String fistname = txtFirstName.getText().toString();
            String lastname = txtLastName.getText().toString();
            String mail = txtMail.getText().toString();

            dbTeacher.updateTeacherById(NavigationActivity.currentTeacher.getId(),fistname, lastname, mail);
            NavigationActivity.currentTeacher=dbTeacher.getTeacherById(1);

            setEditable(false);
            hideKeyboard();
            setHasOptionsMenu(false);

            fab.setVisibility(View.VISIBLE);

        }

        return super.onOptionsItemSelected(item);
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

    public void hideKeyboard() {
        closeKeyboard(getActivity(), txtFirstName.getWindowToken());
        closeKeyboard(getActivity(), txtLastName.getWindowToken());
        closeKeyboard(getActivity(), txtMail.getWindowToken());
    }


    public void setEditable (boolean bool) {
        txtFirstName.setCursorVisible(bool);
        txtFirstName.setClickable(bool);
        txtFirstName.setFocusable(bool);
        txtFirstName.setFocusableInTouchMode(bool);

        txtLastName.setCursorVisible(bool);
        txtLastName.setClickable(bool);
        txtLastName.setFocusable(bool);
        txtLastName.setFocusableInTouchMode(bool);

        txtMail.setCursorVisible(bool);
        txtMail.setClickable(bool);
        txtMail.setFocusable(bool);
        txtMail.setFocusableInTouchMode(bool);
    }

    public boolean checkEverythingOnSaveClick() {
        //check if the firsname is filled
        if (txtFirstName.getText().toString().trim().equals("")) {
            txtFirstName.setError(getResources().getString(R.string.TitleAlert));
            return false;
        }

        //check if the lastname is filled
        else if (txtLastName.getText().toString().trim().equals("")) {
            txtLastName.setError(getResources().getString(R.string.TitleAlert));
            return false;
        }

        //check if the mail is filled
        else if (txtMail.getText().toString().trim().equals("")) {
            txtMail.setError(getResources().getString(R.string.TitleAlert));
            return false;
        }

        return true;
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
