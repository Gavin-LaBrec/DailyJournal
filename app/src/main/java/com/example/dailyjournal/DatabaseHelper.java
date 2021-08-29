package com.example.dailyjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String ENTRIES_TABLE = "ENTRIES_TABLE";
    public static final String COLUMN_IMPROVEMENT = "IMPROVEMENT";
    public static final String COLUMN_GRATITUDE = "GRATITUDE";
    public static final String COLUMN_DATE_CREATED = "DATE_CREATED";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use for locating paths to the the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Entries.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + ENTRIES_TABLE
                                    + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    + COLUMN_DATE_CREATED + " DATE, "
                                    + COLUMN_IMPROVEMENT + " LONGTEXT, "
                                    + COLUMN_GRATITUDE + " LONGTEXT) ";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean addDatabaseEntry(Entry Entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE_CREATED, Entry.getDate());

        cv.put(COLUMN_IMPROVEMENT, Entry.getImproveText());
        cv.put(COLUMN_GRATITUDE, Entry.getGratitudeText());

        long insert = db.insert(ENTRIES_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets journal entry from the database given the entry's date.
     *
     * @param date date of journal entry to find in database
     *
     * @return entry journal entry found in database
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Entry getEntry(String queryDate) throws Exception {

        String queryString = "SELECT " + COLUMN_DATE_CREATED + ", " + COLUMN_IMPROVEMENT + ", " + COLUMN_GRATITUDE + " FROM " + ENTRIES_TABLE + " WHERE "
                + COLUMN_DATE_CREATED + "='" + queryDate + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        // Test if database has entries
        if (!(cursor.moveToFirst())) {
            throw new Exception();
        }

        String improveText = cursor.getString(1);
        String gratitudeText = cursor.getString(2);
        Entry entry = new Entry(queryDate, improveText, gratitudeText);
        cursor.close();
        db.close();
        return entry;
    }

    /**
     * Finds the most recent date of an entry in the database
     *
     * @return most recent date in database
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  String getRecentDate() {
        String queryString = "SELECT " + COLUMN_DATE_CREATED + " FROM "
                + ENTRIES_TABLE + " ORDER BY ID DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        String recentDate = cursor.getString(0);
        return recentDate;
    }

    /**
     * Gets the databases containing the entries
     *
     * @return the database
     */
    public SQLiteDatabase getDatabase() {
        return (this.getReadableDatabase());
    }

    /**
     * Formats the given date to match the database
     *  formatted
     * @param date date to format
     *
     * @return date in database format
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatDate(LocalDateTime date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int year = date.getYear();
        String formattedDate = month + "/" + day + "/" + year;
        return formattedDate;
    }

}
