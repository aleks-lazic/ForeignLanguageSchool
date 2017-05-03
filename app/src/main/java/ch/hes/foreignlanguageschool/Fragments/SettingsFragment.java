package ch.hes.foreignlanguageschool.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.preference.PreferenceManager;

import java.util.Locale;

import ch.hes.foreignlanguageschool.Activities.LectureActivity;
import ch.hes.foreignlanguageschool.Activities.LectureEdit;
import ch.hes.foreignlanguageschool.Activities.NavigationActivity;
import ch.hes.foreignlanguageschool.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btnLanguage;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnLanguage = (Button) view.findViewById(R.id.btnLanguage);
        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferencesName(getActivity().getApplicationContext(), )

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle(getResources().getString(R.string.chooseLanguage));
                String[] languages = getResources().getStringArray(R.array.languages);
                alertDialog.setItems(languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Aleks", "" + which);
                        changeLanguage(view, which);
                    }
                });
                alertDialog.show();
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

    public void changeLanguage(View view, int position) {

        switch (position) {
            case 0:
                changeToEN(view);
                return;
            case 1:
                changeToFR(view);
                return;
            case 2:
                changeToGE(view);
                return;
        }
    }

    public void changeToFR(View v) {
        String languageToLoad = "fr";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, v.getResources().getDisplayMetrics());


        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit().putString("LANGUAGE", "fr").commit();

        Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
        myIntent.putExtra("tag", "Settings");
        startActivity(myIntent);
    }


    public void changeToGE(View v) {
        String languageToLoad = "de";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, v.getResources().getDisplayMetrics());

        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit().putString("LANGUAGE", "de").commit();

        Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
        myIntent.putExtra("tag", "Settings");
        startActivity(myIntent);
    }

    public void changeToEN(View v) {
        String languageToLoad = "en";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, v.getResources().getDisplayMetrics());

        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit().putString("LANGUAGE", "en").commit();

        Intent myIntent = new Intent(v.getContext(), NavigationActivity.class);
        myIntent.putExtra("tag", "Settings");
        startActivity(myIntent);
    }
}
