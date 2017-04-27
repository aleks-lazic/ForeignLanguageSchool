package ch.hes.foreignlanguageschool.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import ch.hes.foreignlanguageschool.DAO.Assignment;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

public class AssignmentEdit extends AppCompatActivity {

    private TextView txtViewTitle;
    private TextView txtViewDescription;
    private TextView txtViewDueDate;

    private Assignment assignment;

    private DatabaseHelper db;
    private DBTeacher dbTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_edit);

        String title = getResources().getString(R.string.Assignment);

        setTitle(title);

        final Intent intent = getIntent();

        txtViewTitle = (TextView) findViewById(R.id.activity_assignment_edit_title);
        txtViewDescription = (TextView) findViewById(R.id.activity_assignment_edit_description);
        txtViewDueDate = (TextView) findViewById(R.id.datePicker);

        if (intent.getSerializableExtra("assignment") != null) {
            assignment = (Assignment) intent.getSerializableExtra("assignment");
            txtViewTitle.setText(assignment.getTitle());
            txtViewDescription.setText(assignment.getDescription());
            txtViewDueDate.setText(assignment.getDate());

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
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();
                }
            });


        } else {
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
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_assignment_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save_assignment) {


            //check if the title is filled
            if (txtViewTitle.getText().toString().trim().equals("")) {
                txtViewTitle.setError("Title is required!");
                return super.onOptionsItemSelected(item);
            }

//
//            //get students from selected list
//            ArrayList<Student> students = new ArrayList<Student>();
//            Student student = null;
//
//            for (int i = 0; i < checked.size(); i++) {
//                // Item position in adapter
//                int position = checked.keyAt(i);
//                // Add sport if it is checked i.e.) == TRUE!
//                if (checked.valueAt(i))
//                    student = dbStudent.getStudentById(position+1);
//                students.add(student);
//            }
//
//
//            //get the teacher from DB
//            String selectedTeacher = spinnerTeacher.getSelectedItem().toString();
//            Teacher teacher = dbTeacher.getTeacherByName(selectedTeacher);
//
//
//            //insert everything in DB
//            dbLecture.insertValues(txtTitle.getText().toString(), txtDescription.getText().toString(), teacher.getId());

        }

        finish();
        return super.onOptionsItemSelected(item);

    }
}
