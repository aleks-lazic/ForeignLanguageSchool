package ch.hes.foreignlanguageschool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

import static ch.hes.foreignlanguageschool.R.id.activity_lecture_fab;

public class LectureActivity extends AppCompatActivity {

    private ListView listView_students;
    private ArrayList<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        Lecture lecture = (Lecture) intent.getSerializableExtra("list");
        setTitle(lecture.getName());

        TextView description = (TextView) findViewById(R.id.activity_lecture_description);

        description.setText(lecture.getDescription());

        TextView teacher = (TextView) findViewById(R.id.activity_lecture_teacher);

        teacher.setText(lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName());

        // Set the list of students
        listView_students = (ListView) findViewById(R.id.activity_lecture_students);

        //set the students from the lecture
        students = lecture.getStudentsList();
        Log.d("Aleks", " "+ students.size());
        String[] studentsName = new String[students.size()];
        for (int i = 0; i < studentsName.length; i++) {
            studentsName[i] = students.get(i).getFirstName() + " " + students.get(i).getLastName();
            Log.d("Aleks", studentsName[i]);
        }

        listView_students.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, studentsName));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_lecture_fab);
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
