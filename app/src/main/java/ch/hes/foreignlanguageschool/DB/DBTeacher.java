package ch.hes.foreignlanguageschool.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.DAO.Teacher;

/**
 * Created by Aleksandar on 06.04.2017.
 */

public class DBTeacher {

    private DatabaseHelper db;

    public DBTeacher(DatabaseHelper db) {
        this.db = db;
    }


    public void insertValues(String firstName, String lastName, String mail) {

        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.getTEACHER_FIRSTNAME(), firstName);
        values.put(db.getTEACHER_LASTNAME(), lastName);
        values.put(db.getTEACHER_MAIL(), mail);
        values.put(db.getIMAGE_NAME(), "teacher_icon");

        sql.insert(db.getTableTeacher(), null, values);

        sql.close();
    }

    public Teacher getTeacherById(int idTeacher) {
        SQLiteDatabase sql = db.getReadableDatabase();

        Teacher teacher = new Teacher();
        String selectQuery = "SELECT "
                + db.getKeyId() + ", "
                + db.getTEACHER_FIRSTNAME() + ", "
                + db.getTEACHER_LASTNAME() + ", "
                + db.getTEACHER_MAIL() + ", "
                + db.getIMAGE_NAME() +
                " FROM " + db.getTableTeacher() + " WHERE " + db.getKeyId() + " = " + idTeacher;

        Cursor cursor = sql.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        DBLecture dbLecture = new DBLecture(db);


        teacher.setId(Integer.parseInt(cursor.getString(0)));
        teacher.setFirstName(cursor.getString(1));
        teacher.setLastName(cursor.getString(2));
        teacher.setMail(cursor.getString(3));
        teacher.setImageName(cursor.getString(4));

        sql.close();

        teacher.setLecturesList(dbLecture.getLecturesForTeacher(idTeacher));


        // return teacher
        return teacher;
    }

//    public ArrayList<Teacher> getAllTeachers() {
//
//        SQLiteDatabase sql = db.getReadableDatabase();
//
//        ArrayList<Teacher> teachersList = new ArrayList<Teacher>();
//        String selectQuery = "SELECT * FROM " + db.getTableTeacher();
//
//        Cursor cursor = sql.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Teacher teacher = new Teacher();
//                teacher.setId(Integer.parseInt(cursor.getString(0)));
//                teacher.setFirstName(cursor.getString(1));
//                teacher.setLastName(cursor.getString(2));
//                teacher.setMail(cursor.getString(3));
//                teacher.setImageName(cursor.getString(4));
//
//                // Adding teacher to list
//                teachersList.add(teacher);
//            } while (cursor.moveToNext());
//        }
//
//        sql.close();
//
//
//        // return teachers list
//        return teachersList;
//    }


    //we don't want to delete a teacher

//    public void deleteTeacher(int idTeacher) {
//        deleteTeacherById(idTeacher);
//        deleteAllAssignmentsByIdTeacher(idTeacher);
//    }
//
//    private void deleteTeacherById(int idTeacher) {
//        SQLiteDatabase sql = db.getWritableDatabase();
//
//        sql.delete(db.getTableTeacher(), db.getKeyId() + " = ?",
//                new String[]{String.valueOf(idTeacher)});
//        sql.close();
//    }
//
//    private void deleteAllAssignmentsByIdTeacher(int idTeacher) {
//
//        SQLiteDatabase sql = db.getWritableDatabase();
//
//        sql.delete(db.getTableAssignement(), db.getASSIGNMENT_FKTEACHER() + " = ?",
//                new String[]{String.valueOf(idTeacher)});
//        sql.close();
//    }
}
