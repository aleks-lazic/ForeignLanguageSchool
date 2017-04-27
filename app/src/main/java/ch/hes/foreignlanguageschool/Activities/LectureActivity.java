package ch.hes.foreignlanguageschool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.R;

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

        Lecture lecture = (Lecture) intent.getSerializableExtra("lecture");
        setTitle(lecture.getName());

        TextView description = (TextView) findViewById(R.id.activity_lecture_description);

        description.setText(lecture.getDescription());

        TextView teacher = (TextView) findViewById(R.id.activity_lecture_teacher);

        teacher.setText(lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName());

        TextView startTime = (TextView) findViewById(R.id.activity_lecture_starttime);

        startTime.setText(lecture.getStartTime());

        TextView endTime = (TextView) findViewById(R.id.activity_lecture_endtime);

        endTime.setText(lecture.getEndTime());

        // Set the list of students
        listView_students = (ListView) findViewById(R.id.activity_lecture_students);

        //set the students from the lecture
        students = lecture.getStudentsList();
        String[] studentsName = new String[students.size()];
        for (int i = 0; i < studentsName.length; i++) {
            studentsName[i] = students.get(i).getFirstName() + " " + students.get(i).getLastName();
        }

        listView_students.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, studentsName));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_lecture_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }
}
