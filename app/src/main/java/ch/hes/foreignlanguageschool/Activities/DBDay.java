package ch.hes.foreignlanguageschool.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.hes.foreignlanguageschool.DAO.Day;

/**
 * Created by patrickclivaz on 11.04.17.
 */

public class DBDay {

    private DatabaseHelper db;

    public DBDay(DatabaseHelper db) {this.db = db;}

    public void insertValues(String name) {

        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.getDAY_NAME(), name);

        sql.insert(db.getTableDay(), null, values);
        sql.close();
    }

    public List<Day> getAllDays() {

        SQLiteDatabase sql = db.getWritableDatabase();

        List<Day> daysList = new ArrayList<Day>();
        String selectQuery = "SELECT * FROM " + db.getTableDay();

        Cursor cursor = sql.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Day day = new Day();
                day.setId(Integer.parseInt(cursor.getString(0)));
                day.setName(cursor.getString(1));

                // Adding day to list
                daysList.add(day);
            } while (cursor.moveToNext());
        }

        // return days list
        return daysList;
    }
}
