package ch.hes.foreignlanguageschool.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.hes.foreignlanguageschool.DAO.Teacher;
import ch.hes.foreignlanguageschool.DB.DBAssignment;
import ch.hes.foreignlanguageschool.DB.DBDay;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.Fragments.AssignmentsFragment;
import ch.hes.foreignlanguageschool.Fragments.CalendarFragment;
import ch.hes.foreignlanguageschool.Fragments.LecturesFragment;
import ch.hes.foreignlanguageschool.Fragments.SettingsFragment;
import ch.hes.foreignlanguageschool.Fragments.StudentsFragment;
import ch.hes.foreignlanguageschool.Fragments.TodayFragment;
import ch.hes.foreignlanguageschool.R;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //tags used to attach the fragments
    public final String TAG_TODAY = "Today";
    public final String TAG_CALENDAR = "Calendar";
    public final String TAG_ASSIGNMENTS = "Assignments";
    public final String TAG_LECTURES = "Lectures";
    public final String TAG_STUDENTS = "Students";
//    public final String TAG_TEACHERS = "Teachers";
    public final String TAG_SETTINGS = "Settings";

    public String CURRENT_TAG = "";

    //index to identify current nav menu item
    public int navItemIndex = 0;

    //toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Handler mHandler;

    //Database
    private DatabaseHelper databaseHelper;
    private DBAssignment dbAssignment;
    private DBDay dbDay;
    private DBLecture dbLecture;
    private DBStudent dbStudent;
    private DBTeacher dbTeacher;

    public static Teacher currentTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        addToDatabase();

        //create the current teacher like if it was in a database
        currentTeacher = Teacher.getInstance(databaseHelper, 1);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.MenuItems);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_TODAY;
            loadHomeFragment();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_today) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_TODAY;
        } else if (id == R.id.nav_calendar) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_CALENDAR;
        } else if (id == R.id.nav_assignments) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_ASSIGNMENTS;
        } else if (id == R.id.nav_lectures) {
            navItemIndex = 3;
            CURRENT_TAG = TAG_LECTURES;
        } else if (id == R.id.nav_students) {
            navItemIndex = 4;
            CURRENT_TAG = TAG_STUDENTS;
//        } else if (id == R.id.nav_teachers) {
//            navItemIndex = 5;
//            CURRENT_TAG = TAG_TEACHERS;
        } else if (id == R.id.nav_settings) {
            CURRENT_TAG = TAG_SETTINGS;
            navItemIndex = 6;
        }
        //Checking if the item in in checked state or not, if not make it checked
        if (menuItem.isChecked()) {
            menuItem.setChecked(false);
        } else {
            menuItem.setChecked(true);
        }

        menuItem.setChecked(true);
        loadHomeFragment();
        return true;
    }

    public void loadHomeFragment() {

        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
//            toggleFab();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                //to replace the current fragment with another one
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
//        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    public Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                TodayFragment todayFragment = new TodayFragment();
                return todayFragment;
            case 1:
                // calendar
                CalendarFragment calendarFragment = new CalendarFragment();
                return calendarFragment;
            case 2:
                // assignments
                AssignmentsFragment assignmentsFragment = new AssignmentsFragment();
                return assignmentsFragment;
            case 3:
                // Lectures
                LecturesFragment lecturesFragment = new LecturesFragment();
                return lecturesFragment;

            case 4:
                // students
                StudentsFragment studentsFragment = new StudentsFragment();
                return studentsFragment;
//            case 5:
//                // teachers
//                TeachersFragment teachersFragment = new TeachersFragment();
//                return teachersFragment;
            case 6:
                // settings
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new TodayFragment();
        }
    }

    public void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    public void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void addToDatabase() {
        //Don't delete for the moment
        this.deleteDatabase("DBForeignSchool");

        //everything about database
        databaseHelper = DatabaseHelper.getInstance(this);

        //adding teachers
        dbTeacher = new DBTeacher(databaseHelper);
        dbTeacher.insertValues("Predrag", "Ljubicic", "predrag.ljubicic@gmail.srb");

        //adding days
        dbDay = new DBDay(databaseHelper);
        dbDay.insertValues("Monday");
        dbDay.insertValues("Tuesday");
        dbDay.insertValues("Wednesday");
        dbDay.insertValues("Thursday");
        dbDay.insertValues("Friday");
        dbDay.insertValues("Saturday");

        //adding lectures
        dbLecture = new DBLecture(databaseHelper);
        dbLecture.insertValues("English", "English advanced course", dbTeacher.getTeacherById(1).getId());
        dbLecture.insertValues("Written Communication", "Written communication course for beginners", dbTeacher.getTeacherById(1).getId());

        //adding students
        dbStudent = new DBStudent(databaseHelper);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        Log.d("Date", currentDate);
        dbStudent.insertValues("Aleksandar", "Lazic", "Rue Centrale 8", "Serbia", "aleks.lazic@hotmail.com", currentDate, "30.06.2017");
        dbStudent.insertValues("Bernard", "Dubois", "Route des anémones 40", "Suisse", "bernard.dubois@gmail.com", currentDate, "30.07.207");
        dbStudent.insertValues("Kristijan", "Palesko", "Rue de la gare 8", "Croatia", "kiki.palesh@hotmail.com", currentDate, "30.08.2017");
        dbStudent.insertValues("Vlado", "Mitrovic", "Rue de l'armée 15", "Bosnia", "vlado.mitro@myarmy.com", currentDate, "30.09.2017");
        dbStudent.insertValues("Noah", "Bonvin", "Rue de Tsarbouye 45", "Ouganda", "noah.b@hevs.ch", currentDate, "30.05.2017");

        //adding assignments
        dbAssignment = new DBAssignment(databaseHelper);
        dbAssignment.insertValues("Correction IT exams", null, currentDate, dbTeacher.getTeacherById(1).getId());
        dbAssignment.insertValues("Prepare English course", "Organize the presentations", currentDate, dbTeacher.getTeacherById(1).getId());


        //adding students to lecture
        dbLecture.addStudentToLecture(1, 1);
        dbLecture.addStudentToLecture(2, 1);
        dbLecture.addStudentToLecture(3, 1);
        dbLecture.addStudentToLecture(4, 1);
        dbLecture.addStudentToLecture(5, 1);
        dbLecture.addStudentToLecture(1, 2);


        //adding lectures to a date
        dbLecture.addDayAndHoursToLecture(1, 1, "08:30", "10:00");
        dbLecture.addDayAndHoursToLecture(2, 2, "08:30", "10:00");

    }

}
