package ch.hes.foreignlanguageschool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import ch.hes.foreignlanguageschool.DAO.Teacher;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

public class TeacherEdit extends AppCompatActivity {
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtMail;

    private Teacher teacher;

    private DatabaseHelper db;
    private DBTeacher dbTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit);

        String title = getResources().getString(R.string.TeacherTitle);

        setTitle(title);

        //fill layout objects
        txtFirstName = (EditText) findViewById(R.id.editTxtFirstName_teacher);
        txtLastName = (EditText) findViewById(R.id.editTxtLastName_teacher);
        txtMail = (EditText) findViewById(R.id.editTxtMail_teacher);

        //create database objects
        db = DatabaseHelper.getInstance(this);
        dbTeacher = new DBTeacher(db);

        //get the intent to check if it is an update or a new lecture
        Intent intent = getIntent();

        if (intent.getSerializableExtra("teacher") != null) {
            teacher = (Teacher) intent.getSerializableExtra("teacher");
            txtFirstName.setText(teacher.getFirstName());
            txtLastName.setText(teacher.getLastName());
            txtMail.setText(teacher.getMail());
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

            if (!checkEverythingOnSaveClick()) {

                return super.onOptionsItemSelected(item);
            }

            String fistname = txtFirstName.getText().toString();
            String lastname = txtLastName.getText().toString();
            String mail = txtMail.getText().toString();

            dbTeacher.insertValues(fistname, lastname, mail);
            finish();
        }

        finish();
        return super.onOptionsItemSelected(item);
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
}
