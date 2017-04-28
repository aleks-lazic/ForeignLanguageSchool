package ch.hes.foreignlanguageschool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

public class LectureActivity extends AppCompatActivity {

    private ListView listView_students;
    private Lecture lecture;

    private TextView description;
    private TextView teacher;
    private TextView startTime;
    private TextView endTime;

    private DatabaseHelper db;
    private DBLecture dbLecture;

    private ArrayList<Student> students;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        lecture = (Lecture) intent.getSerializableExtra("lecture");
        description = (TextView) findViewById(R.id.activity_lecture_description);
        teacher = (TextView) findViewById(R.id.activity_lecture_teacher);
        startTime = (TextView) findViewById(R.id.activity_lecture_starttime);
        endTime = (TextView) findViewById(R.id.activity_lecture_endtime);
        listView_students = (ListView) findViewById(R.id.activity_lecture_students);


        //set textviews
        setTitle(lecture.getName());
        description.setText(lecture.getDescription());
        teacher.setText(lecture.getTeacher().getFirstName() + " " + lecture.getTeacher().getLastName());
        startTime.setText(lecture.getStartTime());
        endTime.setText(lecture.getEndTime());

        //database
        db = DatabaseHelper.getInstance(this);
        dbLecture = new DBLecture(db);

        // Set the list of students

        //set the students from the lecture
        students = lecture.getStudentsList();
        String[] studentsName = new String[students.size()];
        for (int i = 0; i < studentsName.length; i++) {
            studentsName[i] = students.get(i).getFirstName() + " " + students.get(i).getLastName();
        }

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R)
        listView_students.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, studentsName));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_lecture_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LectureActivity.this, LectureEdit.class);
                intent.putExtra("lecture", lecture);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete) {
            dbLecture.deleteLecture(lecture.getId());
            finish();
            Toast toast = Toast.makeText(this, lecture.toString() + " lecture deleted successfully", Toast.LENGTH_SHORT);
            toast.show();
        } else{
            finish();
            return true;
        }

        return true;

    }

    @Override
    public void onBackPressed() {
        Log.d("Aleks", "Je ne rentre jamais la dedans");
        super.onBackPressed();
        finish();
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }
}
