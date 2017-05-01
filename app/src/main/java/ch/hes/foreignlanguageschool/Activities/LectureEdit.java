package ch.hes.foreignlanguageschool.Activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import ch.hes.foreignlanguageschool.DAO.Assignment;
import ch.hes.foreignlanguageschool.DAO.Day;
import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DAO.Student;
import ch.hes.foreignlanguageschool.DB.DBDay;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.R;


public class LectureEdit extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtTeacherName;
    private Spinner spinnerDays;
    private ListView listViewStudents;
    private EditText editTxtTimePickerFrom;
    private EditText editTxtTimePickerTo;

    private ArrayList<Student> students;
    private ArrayAdapter<Student> adapterStudent;
    private Student student;

    private ArrayList<Day> days;
    private ArrayAdapter<Day> adapterDay;
    private Day day;

    private DatabaseHelper db;
    private DBStudent dbStudent;
    private DBLecture dbLecture;
    private DBDay dbDay;

    private Lecture lecture;


    private SparseBooleanArray checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_edit);

        String title = getResources().getString(R.string.Lecture);

        setTitle(title);

        //fill layout objects
        txtTitle = (EditText) findViewById(R.id.editTxtName);
        txtDescription = (EditText) findViewById(R.id.editTxtDescription);
        txtTeacherName = (TextView) findViewById(R.id.teacherName);
        listViewStudents = (ListView) findViewById(R.id.listViewStudents);
        spinnerDays = (Spinner) findViewById(R.id.spinnerDay);
        editTxtTimePickerFrom = (EditText) findViewById(R.id.timePickerFrom);
        editTxtTimePickerTo = (EditText) findViewById(R.id.timePickerTo);

        editTxtTimePickerTo.setText("");
        editTxtTimePickerFrom.setText("");
        txtTeacherName.setText(NavigationActivity.currentTeacher.toString());

        //create database objects
        db = DatabaseHelper.getInstance(this);
        dbDay = new DBDay(db);
        dbStudent = new DBStudent(db);
        dbLecture = new DBLecture(db);

        //get the intent to check if it is an update or a new lecture
        Intent intent = getIntent();

        if (intent.getSerializableExtra("lecture") != null) {

            //fill values for update
            lecture = (Lecture) intent.getSerializableExtra("lecture");
            txtTitle.setText(lecture.getName());
            txtDescription.setText(lecture.getDescription());
            editTxtTimePickerFrom.setText(lecture.getStartTime());
            editTxtTimePickerTo.setText(lecture.getEndTime());

            //onFocus for title and description
            onFocusListenerForTitleAndDescription();

            //create the time picker
            createTimePicker();

            //create spinnerDay and set default position
            days = dbDay.getAllDays();
            adapterDay = new ArrayAdapter<Day>(this, android.R.layout.simple_spinner_dropdown_item, days);
            spinnerDays.setAdapter(adapterDay);
            day = dbDay.getDayById(lecture.getIdDay());
            setDefaultValueSpinner(spinnerDays, day.getId());

            //set listview
            students = dbStudent.getStudentsListNotInLecture(lecture.getId());
            adapterStudent = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_multiple_choice, students);
            listViewStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listViewStudents.setAdapter(adapterStudent);
        } else {


            //set days spinner
            days = dbDay.getAllDays();
            adapterDay = new ArrayAdapter<Day>(this, android.R.layout.simple_spinner_dropdown_item, days);
            spinnerDays.setAdapter(adapterDay);


            onFocusListenerForTitleAndDescription();

            createTimePicker();

            //Fill in the listview with all students
            students = dbStudent.getAllStudents();

            adapterStudent = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_multiple_choice, students);
            listViewStudents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listViewStudents.setAdapter(adapterStudent);

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

            //get students from selected list
            students = new ArrayList<Student>();
            student = null;

            for (int i = 0; i < checked.size(); i++) {
                // Item position in adapter
                int position = checked.keyAt(i);
                // Add sport if it is checked i.e.) == TRUE!
                if (checked.valueAt(i))
                    student = dbStudent.getStudentById(position + 1);
                students.add(student);
            }

            //get the day from spinner
            day = (Day) spinnerDays.getSelectedItem();

            //insert everything in DB
            String title = txtTitle.getText().toString();
            String description = txtDescription.getText().toString();
            int idTeacher = NavigationActivity.currentTeacher.getId();
            int idDay = day.getId();
            String timeFrom = editTxtTimePickerFrom.getText().toString();
            String timeTo = editTxtTimePickerTo.getText().toString();

            if (lecture != null) {
                dbLecture.updateLectureNameAndDescription(lecture.getId(), title, description);
                dbLecture.updateDayTime(lecture.getId(), lecture.getIdDay(), idDay, timeFrom, timeTo);
                dbLecture.addStudentsToLecture(students, lecture.getId());
            } else {
                dbLecture.insertLectureWithTeacherDayAndHoursAndStudents
                        (title, description, idTeacher, idDay, timeFrom, timeTo, students);
            }


        }
        finish();
        return super.onOptionsItemSelected(item);

    }

    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean checkEndTimeAfterStartTime(int fromTime, int endTime) {
        if (fromTime > endTime) {
            Toast toast = Toast.makeText(this, " " + getResources().getString(R.string.TimeAlert), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;
    }

    public boolean checkTitleFilled(String title) {
        if (title.trim().equals("")) {
            txtTitle.setError(getResources().getString(R.string.TitleAlert));
            return false;
        }
        return true;
    }

    public boolean checkFromTimeAndEndTimeFilled(String fromTime, String endTime) {
        if (fromTime.trim().equals("")) {
            editTxtTimePickerFrom.setError(getResources().getString(R.string.HourAlert));
            return false;
        }

        if (endTime.trim().equals("")) {
            editTxtTimePickerTo.setError(getResources().getString(R.string.HourAlert));
            return false;
        }

        return true;
    }

    public boolean checkSizeSelectedStudents(int size, Lecture lecture) {

        if(lecture != null ){
            return true;
        }

        if (size == 0) {
            Toast toast = Toast.makeText(this, " " + getResources().getString(R.string.StudentAlert), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;
    }

    //set timepicker
    public void createTimePicker() {
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
                mTimePicker.setTitle(getResources().getString(R.string.SelectTime));
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
                mTimePicker.setTitle(getResources().getString(R.string.SelectTime));
                mTimePicker.show();
            }
        });
    }

    public boolean checkEverythingOnSaveClick() {

        if (!checkTitleFilled(txtTitle.getText().toString())) {
            return false;
        }

        if (!checkFromTimeAndEndTimeFilled(editTxtTimePickerFrom.getText().toString(), editTxtTimePickerTo.getText().toString())) {

            return false;

        }


        //check if from time is before "To" time
        int fromTime = Integer.parseInt(editTxtTimePickerFrom.getText().toString().replaceAll(":", ""));
        int toTime = Integer.parseInt(editTxtTimePickerTo.getText().toString().replaceAll(":", ""));

        if (!checkEndTimeAfterStartTime(fromTime, toTime)) {
            return false;
        }

        //check if minimum one student is selected
        checked = listViewStudents.getCheckedItemPositions();

        if (!checkSizeSelectedStudents(checked.size(), lecture)) {
            return false;
        }

        return true;

    }

    public void onFocusListenerForTitleAndDescription() {
        //hide keyboard when out of edit text
        txtTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyBoard(v);
                }
            }
        });

        txtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyBoard(v);
                }
            }
        });
    }

    public void setDefaultValueSpinner(final Spinner spinner, final int id) {
        spinner.post(new Runnable() {
            public void run() {
                spinner.setSelection(id - 1);
            }
        });
    }
}
