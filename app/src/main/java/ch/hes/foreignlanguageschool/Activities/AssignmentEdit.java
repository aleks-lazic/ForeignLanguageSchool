package ch.hes.foreignlanguageschool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import ch.hes.foreignlanguageschool.DAO.Assignment;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

public class AssignmentEdit extends AppCompatActivity {

    private TextView txtViewTitle;
    private TextView txtViewDescription;
    private TextView txtViewDueDate;
    private Spinner spinnerTeachers;
    private Button btnValidate;

    private Assignment assignment;

    private DatabaseHelper db;
    private DBTeacher dbTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_edit);


        final Intent intent = getIntent();

        txtViewTitle = (TextView) findViewById(R.id.activity_assignment_edit_title);
        txtViewDescription = (TextView) findViewById(R.id.activity_assignment_edit_description);
        txtViewDueDate = (TextView) findViewById(R.id.activity_assignment_edit_dueDate);
        spinnerTeachers = (Spinner) findViewById(R.id.activity_assignment_spinner_teachers);
        btnValidate = (Button) findViewById(R.id.activity_assingment_btnValidate);


        if (intent.getSerializableExtra("assignment") != null) {
            assignment = (Assignment) intent.getSerializableExtra("assignment");
            txtViewTitle.setText(assignment.getTitle());
            txtViewDescription.setText(assignment.getDescription());
            txtViewDueDate.setText(assignment.getDate());
            spinnerTeachers.setVisibility(View.GONE);


            btnValidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //il faut encore coder le update
                    setResult(Activity.RESULT_OK);
                }
            });


        } else {




            btnValidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //il faut encore coder le create
                    setResult(Activity.RESULT_OK);
                }
            });

        }


    }

    private void loadSpinnerData(){
        db = DatabaseHelper.getInstance(this);
//        dbTeacher = new DBTeacher()

    }
}
