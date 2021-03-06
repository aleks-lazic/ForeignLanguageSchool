package ch.hes.foreignlanguageschool.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ch.hes.foreignlanguageschool.DAO.Day;

/**
 * Created by patrickclivaz on 11.04.17.
 */

public class DBDay {

    private DatabaseHelper db;

    public DBDay(DatabaseHelper db) {
        this.db = db;
    }

    /**
     * insert values for day in db
     * @param name
     */
    public void insertValues(String name) {

        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(db.getDAY_NAME(), name);

        sql.insert(db.getTableDay(), null, values);
        sql.close();
    }

    /**
     * get all days from db
     * @return
     */
    public ArrayList<Day> getAllDays() {

        SQLiteDatabase sql = db.getWritableDatabase();

        ArrayList<Day> daysList = new ArrayList<Day>();
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

        sql.close();


        // return days list
        return daysList;
    }

    /**
     * get a day by id
     * @param idDay
     * @return
     */
    public Day getDayById(int idDay) {

        SQLiteDatabase sql = db.getReadableDatabase();

        Day day = new Day();

        String selectQuery = "SELECT * FROM " + db.getTableDay() +
                " WHERE " + db.getKeyId() + " = " + idDay;

        Cursor cursor = sql.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();

            day.setId(Integer.parseInt(cursor.getString(0)));
            day.setName(cursor.getString(1));
        }



        sql.close();

        // return day
        return day;
    }


    /**
     * Count the number of rows in the teacher table
     * @return
     */
    public long getNumberOfRowsInTableDay() {

        SQLiteDatabase sql = db.getReadableDatabase();

        long nbRows = DatabaseUtils.queryNumEntries(sql, db.getTableDay());
        return nbRows;
    }
}
