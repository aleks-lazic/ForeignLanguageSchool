package ch.hes.foreignlanguageschool.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

public class StudentEdit extends AppCompatActivity {
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtAddress;
    private TextView txtCountry;
    private TextView txtMail;
    private EditText editTxtDatePickerStart;
    private EditText editTxtDatePickerEnd;

    private Student student;

    private DatabaseHelper db;
    private DBStudent dbStudent;

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit);

        String title = getResources().getString(R.string.Student);

        setTitle(title);

        //fill layout objects
        txtFirstName = (EditText) findViewById(R.id.editTxtFirstName);
        txtLastName = (EditText) findViewById(R.id.editTxtLastName);
        txtAddress = (EditText) findViewById(R.id.editTxtAddress);
        txtCountry = (EditText) findViewById(R.id.editTxtCountry);
        txtMail = (EditText) findViewById(R.id.editTxtMail);
        editTxtDatePickerStart = (EditText) findViewById(R.id.datePickerStart);
        editTxtDatePickerEnd = (EditText) findViewById(R.id.datePickerEnd);

        editTxtDatePickerStart.setText("");
        editTxtDatePickerEnd.setText("");

        //create database objects
        db = DatabaseHelper.getInstance(this);
        dbStudent = new DBStudent(db);

        //get the intent to check if it is an update or a new lecture
        Intent intent = getIntent();

        if (intent.getSerializableExtra("student") != null) {
            student = (Student) intent.getSerializableExtra("student");
            txtFirstName.setText(student.getFirstName());
            txtLastName.setText(student.getLastName());
            txtAddress.setText(student.getAddress());
            txtCountry.setText(student.getCountry());
            txtMail.setText(student.getMail());
            editTxtDatePickerStart.setText(student.getStartDate());
            editTxtDatePickerEnd.setText(student.getEndDate());

            createDatePicker();
        } else {

            createDatePicker();

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save) {

            hideKeyboard();

            if (!checkEverythingOnSaveClick()) {

                return super.onOptionsItemSelected(item);
            }

            String firstname = txtFirstName.getText().toString();
            String lastname = txtLastName.getText().toString();
            String address = txtAddress.getText().toString();
            String country = txtCountry.getText().toString();
            String mail = txtMail.getText().toString();
            String startDate = editTxtDatePickerStart.getText().toString();
            String endDate = editTxtDatePickerEnd.getText().toString();


            if (student != null) {
                dbStudent.updateStudentById(student.getId(), firstname, lastname, address, country, mail, startDate, endDate);
            } else {
                dbStudent.insertValues(firstname, lastname, address, country, mail, startDate, endDate);
            }
            finish();
        }

        finish();
        return super.onOptionsItemSelected(item);
    }

    public void createDatePicker() {
        editTxtDatePickerStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(StudentEdit.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    /*      get date   */
                        selectedmonth = selectedmonth + 1;
                        editTxtDatePickerStart.setText("" + selectedday + "." + selectedmonth + "." + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle(getResources().getString(R.string.SelectDate));
                mDatePicker.getDatePicker().setMinDate(new Date().getTime());
                mDatePicker.show();
            }
        });

        editTxtDatePickerEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(StudentEdit.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    /*      get date   */
                        selectedmonth = selectedmonth + 1;
                        editTxtDatePickerEnd.setText("" + selectedday + "." + selectedmonth + "." + selectedyear);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle(getResources().getString(R.string.SelectDate));
                mDatePicker.getDatePicker().setMinDate(new Date().getTime());
                mDatePicker.show();
            }
        });
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
        else if (txtCountry.getText().toString().trim().equals("")) {
            txtCountry.setError(getResources().getString(R.string.TitleAlert));
            return false;
        }


        //check if the mail is filled
        else if (txtMail.getText().toString().trim().equals("")) {
            txtMail.setError(getResources().getString(R.string.TitleAlert));
            return false;
        }

        //check if the mail is filled
        else if (editTxtDatePickerStart.getText().toString().trim().equals("")) {
            editTxtDatePickerStart.setError(getResources().getString(R.string.TitleAlert));
            return false;
        }


        return true;
    }

    public void hideKeyboard() {
        closeKeyboard(StudentEdit.this, txtFirstName.getWindowToken());
        closeKeyboard(StudentEdit.this, txtLastName.getWindowToken());
        closeKeyboard(StudentEdit.this, txtAddress.getWindowToken());
        closeKeyboard(StudentEdit.this, txtCountry.getWindowToken());
        closeKeyboard(StudentEdit.this, txtMail.getWindowToken());
    }
}
