package ch.hes.foreignlanguageschool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.Fragments.LecturesFragment;
import ch.hes.foreignlanguageschool.R;

import static android.R.attr.key;

public class LectureActivity extends AppCompatActivity {

    private ListView studentsLecture;
    private ArrayList<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        DBLecture dbLecture = new DBLecture(db);

        Intent intent = getIntent();

        Lecture lecture = (Lecture)intent.getSerializableExtra("list");
        setTitle(lecture.getName());

        TextView description = (TextView)findViewById(R.id.lecture_description);

        description.setText(lecture.getDescription());

        TextView teacher = (TextView)findViewById(R.id.lecture_teacher);

        teacher.setText(lecture.getTeacher().getFirstName()+ " " + lecture.getTeacher().getLastName());
//        teacher.setOnClickListener();

        // Set the list of students
        studentsLecture = (ListView) findViewById(R.id.lectures_students);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
