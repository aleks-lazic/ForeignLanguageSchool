package ch.hes.foreignlanguageschool.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import ch.hes.foreignlanguageschool.DAO.Assignment;
import ch.hes.foreignlanguageschool.DB.DBAssignment;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

public class AssignmentEdit extends AppCompatActivity {

    private TextView txtViewTitle;
    private TextView txtViewDescription;
    private TextView txtViewDueDate;
    private TextView txtViewCurrentTeacher;

    private Assignment assignment;

    private DatabaseHelper db;
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
        txtViewCurrentTeacher = (TextView) findViewById(R.id.teacherName);

        txtViewDueDate.setText("");
        txtViewCurrentTeacher.setText(NavigationActivity.currentTeacher.toString());


        //create database objects
        db = DatabaseHelper.getInstance(this);
        dbAssignment = new DBAssignment(db);

        Intent intent = getIntent();

        /**if we have something in the intent it means that we want to update an assignment
         if our intent contains nothing it means that we want to create a new one
         this is to prevent us from creating new activities becauses the fields are quite
         the same
         **/
        if (intent.getSerializableExtra("assignment") != null) {
            assignment = (Assignment) intent.getSerializableExtra("assignment");
            txtViewTitle.setText(assignment.getTitle());
            txtViewDescription.setText(assignment.getDescription());
            txtViewDueDate.setText(assignment.getDate());

            createDatePicker();

        } else {

            createDatePicker();

            //create database objects
            db = DatabaseHelper.getInstance(this);
            dbAssignment = new DBAssignment(db);

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

            //get the teacher from DB
            String title = txtViewTitle.getText().toString();
            String description = txtViewDescription.getText().toString();
            String date = txtViewDueDate.getText().toString();
            int idTeacher = NavigationActivity.currentTeacher.getId();

            if (assignment != null) {
                //update the current assignment
                dbAssignment.updateAssignmentById(assignment.getId(), title, description, date);
            } else {
                //insert everything in DB
                dbAssignment.insertValues(title, description, date, idTeacher);
            }


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
                        String day = ""+selectedday;
                        String month = ""+selectedmonth;
                        if(selectedday< 10){
                            day = "0"+selectedday;
                        }
                        if(selectedmonth < 10){
                            month = "0"+selectedmonth;
                        }
                        txtViewDueDate.setText(day + "." + month + "." + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle(getResources().getString(R.string.SelectDate));
                mDatePicker.getDatePicker().setMinDate(new Date().getTime());
                mDatePicker.show();
            }
        });
    }



}
