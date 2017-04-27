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
import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DB.DBAssignment;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

public class AssignmentActivity extends AppCompatActivity {

    private Assignment assignment;

    private TextView description;
    private TextView date;
    private TextView teacher;

    private DatabaseHelper db;
    private DBAssignment dbAssignment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = DatabaseHelper.getInstance(getApplicationContext());
        dbAssignment = new DBAssignment(db);

        Intent intent = getIntent();

        assignment = (Assignment) intent.getSerializableExtra("assignment");
        setTitle(assignment.getTitle());

        description = (TextView) findViewById(R.id.assignment_description);

        description.setText(assignment.getDescription());

        date = (TextView) findViewById(R.id.assignment_date);

        date.setText(assignment.getDate());

        teacher = (TextView) findViewById(R.id.assignment_teacher);

        teacher.setText(assignment.getTeacher().getFirstName() + " " + assignment.getTeacher().getLastName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_assignment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AssignmentEdit.class);
                intent.putExtra("assignment", assignment);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();



    }
}
