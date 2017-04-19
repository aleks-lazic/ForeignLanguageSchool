package ch.hes.foreignlanguageschool.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.hes.foreignlanguageschool.DAO.Student;

/**
 * Created by patrickclivaz on 11.04.17.
 */

public class DBStudent {

    private DatabaseHelper db;

    public DBStudent(DatabaseHelper db) {this.db = db;}

    public void insertValues(String firstName, String lastName, String address, String country, String mail, String startDate, String endDate) {

        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.getSTUDENT_FIRSTNAME(), firstName);
        values.put(db.getSTUDENT_LASTNAME(), lastName);
        values.put(db.getSTUDENT_ADDRESS(), address);
        values.put(db.getSTUDENT_COUNTRY(), country);
        values.put(db.getSTUDENT_MAIL(), mail);
        values.put(db.getSTUDENT_STARTDATE(), startDate);
        values.put(db.getSTUDENT_ENDDATE(), endDate);

        sql.insert(db.getTableStudent(), null, values);
        sql.close();
    }

    public Student getStudentById(int idStudent) {
        SQLiteDatabase sql = db.getWritableDatabase();

        Student student = new Student();
        String selectQuery = "SELECT * FROM " + db.getTableStudent() + " WHERE " + db.getKeyId() + " = " + idStudent;

        Cursor cursor = sql.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        student.setId(Integer.parseInt(cursor.getString(0)));
        student.setFirstName(cursor.getString(1));
        student.setLastName(cursor.getString(2));
        student.setAddress(cursor.getString(3));
        student.setCountry(cursor.getString(4));
        student.setMail(cursor.getString(5));
        student.setStartDate(cursor.getString(6));
        student.setEndDate(cursor.getString(7));

        // return teacher
        return student;
    }

    public List<Student> getStudentsListByLecture(int idLecture) {
        SQLiteDatabase sql = db.getWritableDatabase();

        List<Student> studentsList = new ArrayList<Student>();

        String selectQuery = "SELECT * FROM " + db.getTableLecturestudent() + " WHERE " + db.getLECTURESTUDENT_FKLECTURE() + " = " + idLecture;

        Cursor cursor = sql.rawQuery(selectQuery, null);

        DBStudent studentDb = new DBStudent(db);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = studentDb.getStudentById(Integer.parseInt(cursor.getString(1)));

                // Adding student to list
                studentsList.add(student);
            } while (cursor.moveToNext());
        }
        // return teacher
        return studentsList;
    }

    public List<Student> getAllStudents() {

        SQLiteDatabase sql = db.getWritableDatabase();

        List<Student> studentsList = new ArrayList<Student>();
        String selectQuery = "SELECT * FROM " + db.getTableStudent();

        Cursor cursor = sql.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(Integer.parseInt(cursor.getString(0)));
                student.setFirstName(cursor.getString(1));
                student.setLastName(cursor.getString(2));
                student.setAddress(cursor.getString(3));
                student.setCountry(cursor.getString(4));
                student.setMail(cursor.getString(5));
                student.setStartDate(cursor.getString(6));
                student.setEndDate(cursor.getString(7));

                // Adding student to list
                studentsList.add(student);
            } while (cursor.moveToNext());
        }

        // return students list
        return studentsList;
    }


}
