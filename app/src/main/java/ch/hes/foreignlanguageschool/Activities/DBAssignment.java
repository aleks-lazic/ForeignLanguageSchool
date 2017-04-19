package ch.hes.foreignlanguageschool.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.hes.foreignlanguageschool.DAO.Assignment;

/**
 * Created by patrickclivaz on 11.04.17.
 */

public class DBAssignment {

    private DatabaseHelper db;

    public DBAssignment(DatabaseHelper db) {this.db =  db;}

    public void insertValues(String title, String description, String date, int idTeacher) {

        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.getASSIGNMENT_TITLE(), title);
        values.put(db.getASSIGNMENT_DESCRIPTION(), description);
        values.put(db.getASSIGNMENT_DATE(), date);
        values.put(db.getASSIGNMENT_FKTEACHER(), idTeacher);

        sql.insert(db.getTableAssignement(), null, values);
        sql.close();
    }

    public List<Assignment> getAllAssignments() {

        SQLiteDatabase sql = db.getWritableDatabase();

        List<Assignment> assignmentsList = new ArrayList<Assignment>();
        String selectQuery = "SELECT * FROM " + db.getTableAssignement();

        Cursor cursor = sql.rawQuery(selectQuery, null);

        DBTeacher teacher = new DBTeacher(db);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Assignment assignment = new Assignment();
                assignment.setId(Integer.parseInt(cursor.getString(0)));
                assignment.setTitle(cursor.getString(1));
                assignment.setDescription(cursor.getString(2));
                assignment.setDate(cursor.getString(3));
                assignment.setTeacher(teacher.getTeacherById(Integer.parseInt(cursor.getString(4))));

                // Adding assignment to list
                assignmentsList.add(assignment);
            } while (cursor.moveToNext());
        }

        // return assignments list
        return assignmentsList;
    }
}
