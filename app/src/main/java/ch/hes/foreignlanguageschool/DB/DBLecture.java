package ch.hes.foreignlanguageschool.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.hes.foreignlanguageschool.DAO.Lecture;

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

        Log.d("Aleks", "je suis dans le dbLecture");
        ContentValues values = new ContentValues();
        values.put(db.getLECTURE_NAME(), name);
        values.put(db.getLECTURE_DESCRIPTION(), description);
        values.put(db.getIMAGE_NAME(), "lecture-icon.png");
        values.put(db.getLECTURE_FKTEACHER(), idTeacher);

        sql.insert(db.getTableLecture(), null, values);
        sql.close();
    }

    public List<Lecture> getAllLectures() {
        SQLiteDatabase sql = db.getWritableDatabase();

        List<Lecture> lecturesList = new ArrayList<Lecture>();
        String selectQuery = "SELECT * FROM " + db.getTableLecture();

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
//              lecture.setStudentsList(student.getStudentsListByLecture(Integer.parseInt(cursor.getString(5))));

                // Adding lecture to list
                lecturesList.add(lecture);
            } while (cursor.moveToNext());
        }

        // return lectures list
        return lecturesList;
    }
}
