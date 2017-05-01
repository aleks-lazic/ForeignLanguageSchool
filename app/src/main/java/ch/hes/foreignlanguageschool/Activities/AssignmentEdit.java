package ch.hes.foreignlanguageschool.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ch.hes.foreignlanguageschool.DAO.Assignment;
import ch.hes.foreignlanguageschool.DAO.Teacher;
import ch.hes.foreignlanguageschool.DB.DBAssignment;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

import static ch.hes.foreignlanguageschool.R.id.spinnerTeacher;

public class AssignmentEdit extends AppCompatActivity {

    private TextView txtViewTitle;
    private TextView txtViewDescription;
    private TextView txtViewDueDate;
    private Spinner spinnerTeachers;

    private Assignment assignment;

    private ArrayList<Teacher> teachers;
    private ArrayAdapter<Teacher> adapter;
    private Teacher teacher;

    private DatabaseHelper db;
    private DBTeacher dbTeacher;
    private DBAssignment dbAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_edit);

        String title = getResources().getString(R.string.Assignment);

        setTitle(title);

        txtViewTitle = (TextView) findViewById(R.id.activity_assignment_edit_title);
        txtViewDescription = (TextView) findViewById(R.id.activity_assignment_edit_description);
        txtViewDueDate = (TextView) findViewById(R.id.datePicker);
        spinnerTeachers = (Spinner) findViewById(spinnerTeacher);

        txtViewDueDate.setText("");

        //create database objects
        db = DatabaseHelper.getInstance(this);
        dbTeacher = new DBTeacher(db);
        dbAssignment = new DBAssignment(db);

        Intent intent = getIntent();

        if (intent.getSerializableExtra("assignment") != null) {
            assignment = (Assignment) intent.getSerializableExtra("assignment");
            txtViewTitle.setText(assignment.getTitle());
            txtViewDescription.setText(assignment.getDescription());
            txtViewDueDate.setText(assignment.getDate());

            createDatePicker();

            //create spinnerTeacher and set default position
            teachers = dbTeacher.getAllTeachers();
            adapter = new ArrayAdapter<Teacher>(this, android.R.layout.simple_spinner_dropdown_item, teachers);
            spinnerTeachers.setAdapter(adapter);

            teacher = assignment.getTeacher();
            setDefaultValueSpinner(spinnerTeachers, teacher.getId());


        } else {

            createDatePicker();

            //create database objects
            db = DatabaseHelper.getInstance(this);
            dbTeacher = new DBTeacher(db);
            dbAssignment = new DBAssignment(db);

            //getallteachers
            teachers = dbTeacher.getAllTeachers();
            adapter = new ArrayAdapter<Teacher>(this, android.R.layout.simple_spinner_dropdown_item, teachers);

            spinnerTeachers.setAdapter(adapter);


        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save) {


            //check if the title is filled
            if (txtViewTitle.getText().toString().trim().equals("")) {
                txtViewTitle.setError(getResources().getString(R.string.TitleAlert));
                return super.onOptionsItemSelected(item);
            }

            //get the teacher from spinner
            teacher = (Teacher) spinnerTeachers.getSelectedItem();

            //get the teacher from DB
            String title = txtViewTitle.getText().toString();
            String description = txtViewDescription.getText().toString();
            String date = txtViewDueDate.getText().toString();
            int idTeacher = teacher.getId();


            //insert everything in DB
            dbAssignment.insertValues(title, description, date, idTeacher);

            finish();

        }

        finish();
        return super.onOptionsItemSelected(item);

    }

    public void createDatePicker() {
        txtViewDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AssignmentEdit.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    /*      get date   */
                        selectedmonth = selectedmonth + 1;
                        txtViewDueDate.setText("" + selectedday + "." + selectedmonth + "." + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle(getResources().getString(R.string.SelectDate));
                mDatePicker.getDatePicker().setMinDate(new Date().getTime());
                mDatePicker.show();
            }
        });
    }

    public void setDefaultValueSpinner(final Spinner spinner, final int id){
        spinner.post(new Runnable() {
            public void run() {
                spinner.setSelection(id-1);
            }
        });
    }
}
