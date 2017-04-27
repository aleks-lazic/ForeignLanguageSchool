package ch.hes.foreignlanguageschool.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DAO.Teacher;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

import static android.R.id.list;

public class LectureEdit extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDescription;
    private Spinner spinnerTeacher;
    private ListView listViewStudents;
    private Button btnValidate;

    private DatabaseHelper db;
    private DBStudent dbStudent;
    private DBTeacher dbTeacher;
    private DBLecture dbLecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_edit);

        setTitle("New Lecture");

        //fill layout objects
        txtTitle = (EditText) findViewById(R.id.editTxtName);
        txtDescription = (EditText) findViewById(R.id.editTxtDescription);
        spinnerTeacher = (Spinner) findViewById(R.id.spinnerTeacher);
        listViewStudents = (ListView) findViewById(R.id.listViewStudents);

        //create database objects
        db = DatabaseHelper.getInstance(this);
        dbStudent = new DBStudent(db);
        dbTeacher = new DBTeacher(db);
        dbLecture = new DBLecture(db);

        //Fill in spinner with all teachers
        ArrayList<Teacher> teachers = dbTeacher.getAllTeachers();
        String[] teachersName = new String[teachers.size()];

        for (int i = 0; i < teachersName.length; i++) {
            teachersName[i] = teachers.get(i).getFirstName() + " " + teachers.get(i).getLastName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, teachersName);
        spinnerTeacher.setAdapter(adapter);


        //Fill in the listview

        //getallstudents
        ArrayList<Student> students = dbStudent.getAllStudentsOrderById();
        String[] studentsName = new String[students.size()];

        for (int i = 0; i < studentsName.length; i++) {
            studentsName[i] = students.get(i).getFirstName() + " " + students.get(i).getLastName();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, studentsName);

        listViewStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listViewStudents.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_lecture_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save_lecture) {


            //check if the title is filled
            if (txtTitle.getText().toString().trim().equals("")) {
                txtTitle.setError("Title is required!");
                return super.onOptionsItemSelected(item);
            }

            //check if minimum one student is selected

            SparseBooleanArray checked = listViewStudents.getCheckedItemPositions();

            if (checked.size() == 0) {
                Toast toast = Toast.makeText(this, "Please select minimum 1 student", Toast.LENGTH_SHORT);
                toast.show();
                return super.onOptionsItemSelected(item);


            }


            //get students from selected list
            ArrayList<Student> students = new ArrayList<Student>();
            Student student = null;

            for (int i = 0; i < checked.size(); i++) {
                // Item position in adapter
                int position = checked.keyAt(i);
                // Add sport if it is checked i.e.) == TRUE!
                if (checked.valueAt(i))
                    student = dbStudent.getStudentById(position+1);
                    students.add(student);
            }


            //get the teacher from DB
            String selectedTeacher = spinnerTeacher.getSelectedItem().toString();
            Teacher teacher = dbTeacher.getTeacherByName(selectedTeacher);


            //insert everything in DB
            dbLecture.insertValues(txtTitle.getText().toString(), txtDescription.getText().toString(), teacher.getId());

        }
        return super.onOptionsItemSelected(item);


    }
}
