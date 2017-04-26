package ch.hes.foreignlanguageschool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ch.hes.foreignlanguageschool.DAO.Assignment;
import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DB.DBAssignment;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

import static android.R.attr.description;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        DBStudent dbStudent = new DBStudent(db);

        Intent intent = getIntent();

        Student student = (Student) intent.getSerializableExtra("list");
        setTitle(student.getFirstName()+" "+student.getLastName());

        TextView address = (TextView)findViewById(R.id.student_address);

        address.setText(student.getAddress());

        TextView country = (TextView)findViewById(R.id.student_country);

        country.setText(student.getCountry());

        TextView mail = (TextView)findViewById(R.id.student_mail);

        mail.setText(student.getMail());

        TextView startDate = (TextView)findViewById(R.id.student_startdate);

        startDate.setText(student.getStartDate());

        TextView endDate = (TextView)findViewById(R.id.student_enddate);

        endDate.setText(student.getEndDate());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_student);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
