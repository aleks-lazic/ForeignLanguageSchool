package ch.hes.foreignlanguageschool.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.hes.foreignlanguageschool.DB.DBAssignment;
import ch.hes.foreignlanguageschool.DB.DBDay;
import ch.hes.foreignlanguageschool.DB.DBLecture;
import ch.hes.foreignlanguageschool.DB.DBStudent;
import ch.hes.foreignlanguageschool.DB.DBTeacher;
import ch.hes.foreignlanguageschool.DB.DatabaseHelper;
import ch.hes.foreignlanguageschool.Fragments.AssignmentsFragment;
import ch.hes.foreignlanguageschool.Fragments.CalendarFragment;
import ch.hes.foreignlanguageschool.Fragments.HomeFragment;
import ch.hes.foreignlanguageschool.Fragments.LecturesFragment;
import ch.hes.foreignlanguageschool.Fragments.SettingsFragment;
import ch.hes.foreignlanguageschool.Fragments.StudentsFragment;
import ch.hes.foreignlanguageschool.Fragments.TeachersFragment;
import ch.hes.foreignlanguageschool.R;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //tags used to attach the fragments
    private final String TAG_HOME = "Home";
    private final String TAG_CALENDAR = "Calendar";
    private final String TAG_ASSIGNMENTS = "Assignments";
    private final String TAG_LECTURES = "Lectures";
    private final String TAG_STUDENTS = "Students";
    private final String TAG_TEACHERS = "Teachers";
    private final String TAG_SETTINGS = "Settings";

    private String CURRENT_TAG = "";

    //index to identify current nav menu item
    private int navItemIndex = 0;

    //toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Handler mHandler;

    //Database
    private DatabaseHelper databaseHelper;
    private DBAssignment dbAssignment;
    private DBDay dbDay;
    private DBLecture dbLecture;
    private DBStudent dbStudent;
    private DBTeacher dbTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //Don't delete for the moment
        this.deleteDatabase("DBForeignSchool");

        //everything about database
        databaseHelper = DatabaseHelper.getInstance(this);

        //adding teachers
        dbTeacher = new DBTeacher(databaseHelper);
        dbTeacher.insertValues("Alexandre", "Cotting", "alexandre.cotting@hevs.ch");
        dbTeacher.insertValues("Jean-Claude", "Rey", "j-c.rey@hotmail.com");
        dbTeacher.insertValues("Michel", "Favre", "michel.favre@gmail.com");
        dbTeacher.insertValues("Predrag", "Ljubicic", "pedjo.ljubo@mail.srb");

        //adding days
        dbDay = new DBDay(databaseHelper);
        dbDay.insertValues("Monday");
        dbDay.insertValues("Tuesday");
        dbDay.insertValues("Wednesday");
        dbDay.insertValues("Thursday");
        dbDay.insertValues("Friday");
        dbDay.insertValues("Saturday");
        dbDay.insertValues("Sunday");

        //adding lectures
        dbLecture = new DBLecture(databaseHelper);
        dbLecture.insertValues("Engish", "English advanced course", dbTeacher.getTeacherById(1).getId());
        dbLecture.insertValues("Written Communication", "Written communication course for beginners", dbTeacher.getTeacherById(1).getId());
        dbLecture.insertValues("IT", "IT course for beginners", dbTeacher.getTeacherById(2).getId());
        dbLecture.insertValues("Grammary", "English grammary course", dbTeacher.getTeacherById(2).getId());
        dbLecture.insertValues("Speaking", "Speaking course", dbTeacher.getTeacherById(3).getId());
        dbLecture.insertValues("Listening", "Listening course", dbTeacher.getTeacherById(3).getId());
        dbLecture.insertValues("Business", "Business course", dbTeacher.getTeacherById(4).getId());
        dbLecture.insertValues("Project Management", "This course will teach you how to handle a project", dbTeacher.getTeacherById(4).getId());

        //adding students
        dbStudent = new DBStudent(databaseHelper);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        dbStudent.insertValues("Aleksandar", "Lazic", "Rue Centrale 8", "Serbia", "aleks.lazic@hotmail.com", currentDate, "0");
        dbStudent.insertValues("Bernard", "Dubois", "Route des anémones 40", "Suisse", "bernard.dubois@gmail.com", currentDate, "0");
        dbStudent.insertValues("Kristijan", "Palesko", "Rue de la gare 8", "Croatia", "kiki.palesh@hotmail.com", currentDate, "0");
        dbStudent.insertValues("Vlado", "Mitrovic", "Rue de l'armée 15", "Bosnia", "vlado.mitro@myarmy.com", currentDate, "0");
        dbStudent.insertValues("Noah", "Bonvin", "Rue de Tsarbouye 45", "Ouganda", "noah.b@hevs.ch", currentDate, "0");

        //adding assignments
        dbAssignment = new DBAssignment(databaseHelper);
        dbAssignment.insertValues("Correction IT exams", null, currentDate, dbTeacher.getTeacherById(4).getId());


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.MenuItems);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
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
        } else if (id == R.id.nav_teachers) {
            navItemIndex = 5;
            CURRENT_TAG = TAG_TEACHERS;
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

    private void loadHomeFragment() {

        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
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


//        Log.d("Aleks", "Current tag "+CURRENT_TAG + fragment.getClass().getSimpleName());
        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                Log.d("Aleks", "GetHomeFragment " + navItemIndex);
                return homeFragment;
            case 1:
                // calendar
                CalendarFragment calendarFragment = new CalendarFragment();
                Log.d("Aleks", "GetHomeFragment " + navItemIndex);
                return calendarFragment;
            case 2:
                // assignments
                AssignmentsFragment assignmentsFragment = new AssignmentsFragment();
                Log.d("Aleks", "GetHomeFragment " + navItemIndex);
                return assignmentsFragment;
            case 3:
                // Lectures
                LecturesFragment lecturesFragment = new LecturesFragment();
                Log.d("Aleks", "GetHomeFragment " + navItemIndex);
                return lecturesFragment;

            case 4:
                // students
                StudentsFragment studentsFragment = new StudentsFragment();
                Log.d("Aleks", "GetHomeFragment " + navItemIndex);
                return studentsFragment;
            case 5:
                // teachers
                TeachersFragment teachersFragment = new TeachersFragment();
                Log.d("Aleks", "GetHomeFragment " + navItemIndex);
                return teachersFragment;
            case 6:
                // settings
                SettingsFragment settingsFragment = new SettingsFragment();
                Log.d("Aleks", "GetHomeFragment " + navItemIndex);
                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void toggleFab() {
        if (navItemIndex == 0) {
            fab.show();
        } else {
            fab.hide();
        }
    }

}
