package ch.hes.foreignlanguageschool.Activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import ch.hes.foreignlanguageschool.DAO.Day;
import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DAO.Teacher;
import ch.hes.foreignlanguageschool.DB.DBDay;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;

import static android.R.attr.checked;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static ch.hes.foreignlanguageschool.R.string.day;

public class LectureEdit extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDescription;
    private Spinner spinnerTeacher;
    private Spinner spinnerDays;
    private ListView listViewStudents;
    private EditText editTxtTimePickerFrom;
    private EditText editTxtTimePickerTo;

    private DatabaseHelper db;
    private DBStudent dbStudent;
    private DBTeacher dbTeacher;
    private DBLecture dbLecture;
    private DBDay dbDay;

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
        spinnerDays = (Spinner) findViewById(R.id.spinnerDay);
        editTxtTimePickerFrom = (EditText) findViewById(R.id.timePickerFrom);
        editTxtTimePickerTo = (EditText) findViewById(R.id.timePickerTo);

        editTxtTimePickerTo.setText("");
        editTxtTimePickerFrom.setText("");

        //set days spinner
        db = DatabaseHelper.getInstance(this);
        dbDay = new DBDay(db);
        ArrayList<Day> days = dbDay.getAllDays();
        String[] daysName = getResources().getStringArray(R.array.DaysOfWeekUntilSaturday);
        ArrayAdapter<Day> adapterDay = new ArrayAdapter<Day>(this, android.R.layout.simple_spinner_dropdown_item, days);
        spinnerDays.setAdapter(adapterDay);


        //open timepicker from on edittextclick
        editTxtTimePickerFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(LectureEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editTxtTimePickerFrom.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        //open timepicker to on edittextclick
        editTxtTimePickerTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(LectureEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editTxtTimePickerTo.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        //create database objects
        dbStudent = new DBStudent(db);
        dbTeacher = new DBTeacher(db);
        dbLecture = new DBLecture(db);

        //Fill in spinner with all teachers
        ArrayList<Teacher> teachers = dbTeacher.getAllTeachers();
        ArrayAdapter<Teacher> adapterTeacher = new ArrayAdapter<Teacher>(this, android.R.layout.simple_spinner_dropdown_item, teachers);
        spinnerTeacher.setAdapter(adapterTeacher);


        //getallstudents
        ArrayList<Student> students = dbStudent.getAllStudentsOrderById();
        String[] studentsName = new String[students.size()];

        for (int i = 0; i < studentsName.length; i++) {
            studentsName[i] = students.get(i).getFirstName() + " " + students.get(i).getLastName();
        }

        //Fill in the listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, studentsName);

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


            //check that the hours are not null
            if (editTxtTimePickerFrom.getText().toString().equals("") || editTxtTimePickerTo.getText().toString().equals("")) {
                Toast toast = Toast.makeText(this, "Please fill in the hours first", Toast.LENGTH_SHORT);
                toast.show();
                return super.onOptionsItemSelected(item);
            }


            //check if from time is before "To" time
            int fromTime = Integer.parseInt(editTxtTimePickerFrom.getText().toString().replaceAll(":", ""));
            int toTime = Integer.parseInt(editTxtTimePickerTo.getText().toString().replaceAll(":", ""));

            if (fromTime > toTime) {
                Toast toast = Toast.makeText(this, "End Time must be after Start Time", Toast.LENGTH_SHORT);
                toast.show();
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
                    student = dbStudent.getStudentById(position + 1);
                students.add(student);
            }

            //get the teacher from spinner
            Teacher selectedTeacher = (Teacher) spinnerTeacher.getSelectedItem();

            //get the day from spinner
            Day day = (Day) spinnerDays.getSelectedItem();

            //insert everything in DB
            String title = txtTitle.getText().toString();
            String description = txtDescription.getText().toString();
            int idTeacher = selectedTeacher.getId();
            int idDay = day.getId();
            String timeFrom = editTxtTimePickerFrom.getText().toString();
            String timeTo = editTxtTimePickerTo.getText().toString();

            Log.d("Aleks", "" + dbLecture.getNumberOfRowsInTableLecture());
            dbLecture.insertLectureWithTeacherDayAndHoursAndStudents
                    (title, description, idTeacher, idDay, timeFrom, timeTo, students);
            Log.d("Aleks", "" + dbLecture.getNumberOfRowsInTableLecture());


            finish();

        }
        return super.onOptionsItemSelected(item);


    }
}
