package ch.hes.foreignlanguageschool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ch.hes.foreignlanguageschool.DAO.Teacher;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

public class TeacherActivity extends AppCompatActivity {
    private Teacher teacher;
    private DatabaseHelper db;
    private DBTeacher dbTeacher;

    private TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //database
        db = DatabaseHelper.getInstance(getApplicationContext());
        dbTeacher = new DBTeacher(db);

        Intent intent = getIntent();

        teacher = (Teacher) intent.getSerializableExtra("teacher");
        setTitle(teacher.getFirstName()+" "+teacher.getLastName());

        mail = (TextView)findViewById(R.id.teacher_mail);

        mail.setText(teacher.getMail());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_teacher);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherActivity.this, TeacherEdit.class);
                intent.putExtra("teacher", teacher);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //we don't want to delete a teacher
//        int id = item.getItemId();
//
//        if (id == R.id.action_delete) {
//            dbTeacher.deleteTeacher(teacher.getId());
//            finish();
//            Toast toast = Toast.makeText(this, teacher.toString() + " " + getResources().getString(R.string.TeacherTitle) + " " + getResources().getString(R.string.DeletedSuccess), Toast.LENGTH_SHORT);
//            toast.show();
//        } else{
//            finish();
//            return true;
//        }
        finish();
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.more_menu, menu);
//
//        // return true so that the menu pop up is opened
//        return true;
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
