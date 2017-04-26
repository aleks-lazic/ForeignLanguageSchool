package ch.hes.foreignlanguageschool.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DAO.Student;

import static android.R.attr.description;
import static android.R.attr.name;

/**
 * Created by patrickclivaz on 11.04.17.
 */

public class DBLecture {

    private DatabaseHelper db;

    public DBLecture(DatabaseHelper db) {
        this.db = db;
    }

    public void insertValues(String name, String description, int idTeacher) {

        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.getLECTURE_NAME(), name);
        values.put(db.getLECTURE_DESCRIPTION(), description);
        values.put(db.getIMAGE_NAME(), "lecture-icon");
        values.put(db.getLECTURE_FKTEACHER(), idTeacher);

        sql.insert(db.getTableLecture(), null, values);
        sql.close();
    }

    public ArrayList<Lecture> getAllLectures() {
        SQLiteDatabase sql = db.getWritableDatabase();

        ArrayList<Lecture> lecturesList = new ArrayList<Lecture>();
//        String selectQuery = "SELECT * FROM " + db.getTableLecture() + " ORDER BY " + db.getLECTURE_NAME();
        String selectQuery = "SELECT * FROM " + db.getTableLecture();

        Cursor cursor = sql.rawQuery(selectQuery, null);

        DBTeacher dbTeacher = new DBTeacher(db);
        DBStudent dbStudent = new DBStudent(db);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lecture lecture = new Lecture();
                lecture.setId(Integer.parseInt(cursor.getString(0)));
                lecture.setName(cursor.getString(1));
                lecture.setDescription(cursor.getString(2));
                lecture.setImageName(cursor.getString((3)));
                lecture.setTeacher(dbTeacher.getTeacherById(Integer.parseInt(cursor.getString(4))));
                lecture.setStudentsList(dbStudent.getStudentsListByLecture(Integer.parseInt(cursor.getString(0))));

                // Adding lecture to list
                lecturesList.add(lecture);
            } while (cursor.moveToNext());
        }

        sql.close();


        // return lectures list
        return lecturesList;
    }

    public void addStudentToLecture(int idStudent, int idLecture) {

        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.getLECTURESTUDENT_FKLECTURE(), idLecture);
        values.put(db.getLECTURESTUDENT_FKSTUDENT(), idStudent);

        sql.insert(db.getTableLecturestudent(), null, values);

        sql.close();

    }

    public void addStudentsToLecture(Student[] students, int idLecture) {

        for (Student s : students
                ) {
            addStudentToLecture(s.getId(), idLecture);

        }
    }

    public void addDayAndHoursToLecture(int idLecture, int idDay, String startTime, String endTime) {
        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.getLECTUREDATE_FKLECTURE(), idLecture);
        values.put(db.getLECTUREDATE_FKDAY(), idDay);
        values.put(db.getLECTUREDATE_STARTTIME(), startTime);
        values.put(db.getLECTUREDATE_ENDTIME(), endTime);

        sql.insert(db.getTableLecturedate(), null, values);
        sql.close();
    }

    public ArrayList<Lecture> getLecturesForSpecialDate(int year, int month, int dayOfMonth) {

        //adapt the current date to the sqlite format
        String monthString;

        if (month < 10)
            monthString = Integer.toString(month + 1);
        else {
            monthString = Integer.toString(month);
        }

        Calendar cal = new GregorianCalendar(year, Integer.parseInt(monthString), dayOfMonth);
        int result = cal.get(Calendar.DAY_OF_WEEK);
        int dayOfWeek = getIdOfDayWeek(result);

        SQLiteDatabase sql = db.getWritableDatabase();

        ArrayList<Lecture> lecturesList = new ArrayList<Lecture>();
        String selectQuery = "SELECT "
                + db.getKeyId() + ", "
                + db.getLECTURE_NAME() + ", "
                + db.getLECTURE_DESCRIPTION() + ", "
                + db.getIMAGE_NAME() + ", "
                + db.getLECTURE_FKTEACHER() + ", "
                + db.getLECTUREDATE_FKDAY() + ", "
                + db.getLECTUREDATE_STARTTIME() + ", "
                + db.getLECTUREDATE_ENDTIME() + " "
                + "FROM " + db.getTableLecture()
                + " LEFT JOIN " + db.getTableLecturedate() + " ON " + db.getTableLecture() + "." + db.getKeyId() + " = " + db.getTableLecturedate() + "." + db.getLECTUREDATE_FKLECTURE()
                + " WHERE " + db.getLECTUREDATE_FKDAY() + " = " + dayOfWeek;


        Cursor cursor = sql.rawQuery(selectQuery, null);

        DBTeacher teacher = new DBTeacher(db);
        DBStudent student = new DBStudent(db);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lecture lecture = new Lecture();
                lecture.setId(Integer.parseInt(cursor.getString(0)));
                lecture.setName(cursor.getString(1));
                lecture.setDescription(cursor.getString(2));
                lecture.setImageName(cursor.getString((3)));
                lecture.setTeacher(teacher.getTeacherById(Integer.parseInt(cursor.getString(4))));
                lecture.setIdDay(Integer.parseInt(cursor.getString(5)));
                lecture.setStartTime(cursor.getString(6));
                lecture.setStartTime(cursor.getString(7));

                // Adding lecture to list
                lecturesList.add(lecture);
            } while (cursor.moveToNext());
        }

        sql.close();


        // return lectures list
        return lecturesList;
    }

    public ArrayList<Lecture> getLecturesForCurrentDateInHome(String date) {

        //25.04.2017

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));

        Log.d("Aleks", "" + day + "." + month + "." + year);

        Calendar cal = new GregorianCalendar(year, month, day);
        int result = cal.get(Calendar.DAY_OF_WEEK);
        int dayOfWeek = getIdOfDayWeek(result);

        SQLiteDatabase sql = db.getWritableDatabase();

        ArrayList<Lecture> lecturesList = new ArrayList<Lecture>();
        String selectQuery = "SELECT "
                + db.getKeyId() + ", "
                + db.getLECTURE_NAME() + ", "
                + db.getLECTURE_DESCRIPTION() + ", "
                + db.getIMAGE_NAME() + ", "
                + db.getLECTURE_FKTEACHER() + ", "
                + db.getLECTUREDATE_FKDAY() + ", "
                + db.getLECTUREDATE_STARTTIME() + ", "
                + db.getLECTUREDATE_ENDTIME() + " "
                + "FROM " + db.getTableLecture()
                + " LEFT JOIN " + db.getTableLecturedate() + " ON " + db.getTableLecture() + "." + db.getKeyId() + " = " + db.getTableLecturedate() + "." + db.getLECTUREDATE_FKLECTURE()
                + " WHERE " + db.getLECTUREDATE_FKDAY() + " = " + dayOfWeek;


        Cursor cursor = sql.rawQuery(selectQuery, null);

        DBTeacher teacher = new DBTeacher(db);
        DBStudent student = new DBStudent(db);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lecture lecture = new Lecture();
                lecture.setId(Integer.parseInt(cursor.getString(0)));
                lecture.setName(cursor.getString(1));
                lecture.setDescription(cursor.getString(2));
                lecture.setImageName(cursor.getString((3)));
                lecture.setTeacher(teacher.getTeacherById(Integer.parseInt(cursor.getString(4))));
                lecture.setIdDay(Integer.parseInt(cursor.getString(5)));
                lecture.setStartTime(cursor.getString(6));
                lecture.setStartTime(cursor.getString(7));

                // Adding lecture to list
                lecturesList.add(lecture);
            } while (cursor.moveToNext());
        }

        sql.close();


        // return lectures list
        return lecturesList;
    }


    private int getIdOfDayWeek(int day) {
        switch (day) {
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 4;
            case 1:
                return 5;
            case 2:
                return 6;
        }

        return 7;
    }
}
