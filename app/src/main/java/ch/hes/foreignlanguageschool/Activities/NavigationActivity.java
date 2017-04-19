package ch.hes.foreignlanguageschool.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ch.hes.foreignlanguageschool.DAO.Teacher;
import ch.hes.foreignlanguageschool.Fragments.AssignmentsFragment;
import ch.hes.foreignlanguageschool.Fragments.CalendarFragment;
import ch.hes.foreignlanguageschool.Fragments.HomeFragment;
import ch.hes.foreignlanguageschool.Fragments.LecturesFragment;
import ch.hes.foreignlanguageschool.Fragments.SettingsFragment;
import ch.hes.foreignlanguageschool.Fragments.StudentsFragment;
import ch.hes.foreignlanguageschool.Fragments.TeachersFragment;
import ch.hes.foreignlanguageschool.R;

import static android.R.id.toggle;

public class NavigationActivity extends AppCompatActivity {


    //tags used to attach the fragments
    private final String TAG_HOME = "Home";
    private final String TAG_CALENDAR = "Calendar";
    private final String TAG_ASSIGNMENTS = "Assignments";
    private final String TAG_LECTURES = "Lectures";
    private final String TAG_STUDENTS = "Students";
    private final String TAG_TEACHERS = "Teachers";

    private String CURRENT_TAG = "";

    //index to identify current nav menu item
    private int navItemIndex = 0;

    private boolean shouldLoadHomeFragOnBackPress = true;

    //toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setUpNavigationView();

        //everything about floating action button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Patrick enculé", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.MenuItems);

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
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

    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                } else {
                    navItemIndex = 0;
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
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    // Sometimes, when fragment has huge data, screen seems hanging
    // when switching between navigation menus
    // So using runnable, the fragment is loaded with cross fade effect
    // This effect can be seen in GMail app
    Runnable mPendingRunnable = new Runnable() {
        @Override
        public void run() {
            // update the main content by replacing fragments
            Fragment fragment = getHomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.drawer_layout, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    };

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
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
            case 5:
                // teachers
                TeachersFragment teachersFragment = new TeachersFragment();
                return teachersFragment;
            case 6:
                // settings
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new HomeFragment();
        }
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

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.drawer_layout, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

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
