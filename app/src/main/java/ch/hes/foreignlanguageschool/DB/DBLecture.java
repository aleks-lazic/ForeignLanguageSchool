package ch.hes.foreignlanguageschool.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.hes.foreignlanguageschool.DAO.Lecture;
import ch.hes.foreignlanguageschool.DAO.Student;

import static android.R.attr.description;
import static android.R.attr.end;
import static android.R.attr.id;
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
        values.put(db.getIMAGE_NAME(), "lecture_icon");
        values.put(db.getLECTURE_FKTEACHER(), idTeacher);

        sql.insert(db.getTableLecture(), null, values);
        sql.close();
    }

    public ArrayList<Lecture> getLecturesForTeacher(int idTeacher) {
        SQLiteDatabase sql = db.getReadableDatabase();

        ArrayList<Lecture> lecturesList = new ArrayList<Lecture>();
        String selectQuery = "SELECT * "
                + "FROM " + db.getTableLecture()
                + " WHERE " + db.getLECTURE_FKTEACHER() + " = " + idTeacher;


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

                // Adding lecture to list
                lecturesList.add(lecture);
            } while (cursor.moveToNext());
        }

        sql.close();


        // return lectures list
        return lecturesList;
    }

    public ArrayList<Lecture> getLecturesForStudent(int idStudent) {
        SQLiteDatabase sql = db.getReadableDatabase();

        ArrayList<Lecture> lecturesList = new ArrayList<Lecture>();
        String selectQuery = "SELECT " + db.getLECTURESTUDENT_FKLECTURE()
                + " FROM " + db.getTableLecturestudent()
                + " WHERE " + db.getLECTURESTUDENT_FKSTUDENT() + " = " + idStudent;


        Cursor cursor = sql.rawQuery(selectQuery, null);

        DBTeacher teacher = new DBTeacher(db);
        DBStudent student = new DBStudent(db);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lecture lecture = getLectureById(Integer.parseInt(cursor.getString(0)));

                // Adding lecture to list
                lecturesList.add(lecture);
            } while (cursor.moveToNext());
        }

        sql.close();


        // return lectures list
        return lecturesList;
    }

    public Lecture getLectureById(int idLecture) {

        SQLiteDatabase sql = db.getReadableDatabase();

        String selectQuery = "SELECT * "
                + "FROM " + db.getTableLecture()
                + " WHERE " + db.getKeyId() + " = " + idLecture;


        Cursor cursor = sql.rawQuery(selectQuery, null);

        DBTeacher teacher = new DBTeacher(db);

        // looping through all rows and adding to list

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Lecture lecture = new Lecture();
        lecture.setId(Integer.parseInt(cursor.getString(0)));
        lecture.setName(cursor.getString(1));
        lecture.setDescription(cursor.getString(2));
        lecture.setImageName(cursor.getString((3)));


        sql.close();


        // return lectures
        return lecture;
    }

    public Lecture getLectureByIdForUpdate(int idLecture) {
        SQLiteDatabase sql = db.getReadableDatabase();

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
                + " LEFT JOIN " + db.getTableLecturedate() + " ON " + db.getTableLecture() + "." + db.getKeyId() + " = " + db.getTableLecturedate() + "." + db.getLECTUREDATE_FKLECTURE();


        Cursor cursor = sql.rawQuery(selectQuery, null);

        DBTeacher teacher = new DBTeacher(db);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Lecture lecture = new Lecture();
        lecture.setId(Integer.parseInt(cursor.getString(0)));
        lecture.setName(cursor.getString(1));
        lecture.setDescription(cursor.getString(2));
        lecture.setImageName(cursor.getString((3)));
        lecture.setTeacher(teacher.getTeacherById(Integer.parseInt(cursor.getString(4))));
        lecture.setIdDay(Integer.parseInt(cursor.getString(5)));
        lecture.setStartTime(cursor.getString(6));
        lecture.setEndTime(cursor.getString(7));


        sql.close();


        // return lectures list
        return lecture;
    }

    public ArrayList<Lecture> getAllLectures() {
        SQLiteDatabase sql = db.getReadableDatabase();

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
                + " LEFT JOIN " + db.getTableLecturedate() + " ON " + db.getTableLecture() + "." + db.getKeyId() + " = " + db.getTableLecturedate() + "." + db.getLECTUREDATE_FKLECTURE();


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
                lecture.setEndTime(cursor.getString(7));

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

    public void addStudentsToLecture(ArrayList<Student> students, int idLecture) {

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
                lecture.setEndTime(cursor.getString(7));

                // Adding lecture to list
                lecturesList.add(lecture);
            } while (cursor.moveToNext());
        }

        sql.close();


        // return lectures list
        return lecturesList;
    }

    public int getMaxId() {
        SQLiteDatabase sql = db.getWritableDatabase();

        int id = 0;
        String query = "SELECT MAX(" + db.getKeyId() + ") FROM " + db.getTableLecture();

        Cursor cursor = sql.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }

        sql.close();

        return id;
    }

    public void insertLectureWithTeacherDayAndHoursAndStudents(String title, String description, int idTeacher, int idDay, String beginTime, String endTime, ArrayList<Student> students) {

        insertValues(title, description, idTeacher);

        int id = getMaxId();

        addDayAndHoursToLecture(id, idDay, beginTime, endTime);

        addStudentsToLecture(students, id);

    }


    public long getNumberOfRowsInTableLecture() {

        SQLiteDatabase sql = db.getReadableDatabase();

        String query = "SELECT Count(*) FROM " + db.getTableLecture();


        long nbRows = DatabaseUtils.queryNumEntries(sql, db.getTableLecture());
        return nbRows;
    }

    public ArrayList<Lecture> getLecturesForCurrentDateInHome(String date) {

        //25.04.2017

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));


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
                lecture.setEndTime(cursor.getString(7));

                // Adding lecture to list
                lecturesList.add(lecture);
            } while (cursor.moveToNext());
        }

        sql.close();


        // return lectures list
        return lecturesList;
    }

    public void deleteLecture(int idLecture) {
        deleteLectureById(idLecture);
        deleteLectureFromLectureStudent(idLecture);
        deleteDayFromLecture(idLecture);
    }


    private void deleteLectureById(int idLecture) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.delete(db.getTableLecture(), db.getKeyId() + " = ?",
                new String[]{String.valueOf(idLecture)});
        sql.close();

    }

    private void deleteLectureFromLectureStudent(int idLecture) {
        SQLiteDatabase sql = db.getWritableDatabase();

        sql.delete(db.getTableLecturestudent(), db.getLECTURESTUDENT_FKLECTURE() + " = ?",
                new String[]{String.valueOf(idLecture)});
        sql.close();


    }

    private void deleteDayFromLecture(int idLecture) {
        SQLiteDatabase sql = db.getWritableDatabase();

        sql.delete(db.getTableLecturedate(), db.getLECTUREDATE_FKLECTURE() + " = ?",
                new String[]{String.valueOf(idLecture)});
        sql.close();
    }


    public int updateLectureNameAndDescription(int idLecture, String name, String description) {
        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(db.getLECTURE_NAME(), name);
        values.put(db.getLECTURE_DESCRIPTION(), description);

        return sql.update(db.getTableLecture(), values, db.getKeyId() + " = ?",
                new String[]{String.valueOf(idLecture)});
    }

    public int updateDayTime(int idLecture, int oldIdDay, int idDay, String beginTime, String endTime) {
        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(db.getLECTUREDATE_FKDAY(), idDay);
        values.put(db.getLECTUREDATE_STARTTIME(), beginTime);
        values.put(db.getLECTUREDATE_ENDTIME(), endTime);


        return sql.update(db.getTableLecturedate(),
                values,
                db.getLECTUREDATE_FKLECTURE() + " = ? AND " + db.getLECTUREDATE_FKDAY() + " = ?",
                new String[]{String.valueOf(idLecture), String.valueOf(oldIdDay)});
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
