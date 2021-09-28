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
import java.util.Iterator;
import java.util.TreeSet;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Entries.db", null, 1);
        dates = this.getDateSet();
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
        if (dates.contains(Entry.getDate())) {
            // Check for entries with the same date
            String entryDate = Entry.getDate();
            int dateDuplicates = 0;
            Iterator dateIterator = dates.iterator();
            while (dateIterator.hasNext()) {
                String dateName = (String) dateIterator.next();
                if (dateName.contains(entryDate)) {
                    dateDuplicates++;
                }
            }
            // Rename duplicate entry date
            if (!(dateDuplicates == 0)) {
                String entryDateDuplicate = entryDate + " (" + dateDuplicates + ")";
                Entry.setDate(entryDateDuplicate);
            }
        }

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
     * Gets the closest newer or older date to the given one
     *
     * @param selectedDate currently selected date
     * @param direction input "newer" to get more recent date and "older" to get older date
     *                  defaults to newer
     *
     * @return next closest date in direction in ready to display format
     */
    public String getNextDate(String selectedDate, String direction) {
        // Determine if getting newer or older date
        int indexChange;
        if (direction == "older") {
            indexChange = -1;
        } else {
            indexChange = 1;
        }

        String formattedDate = selectedDate;
        String queryStringSelected = "SELECT ID FROM "
                + ENTRIES_TABLE + " WHERE DATE_CREATED='" + selectedDate + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorSelected = db.rawQuery(queryStringSelected, null);
        cursorSelected.moveToFirst();
        int recentDateIndex = cursorSelected.getInt(0) + indexChange;
        String queryStringWanted = "SELECT " + COLUMN_DATE_CREATED + " FROM "
                + ENTRIES_TABLE + " WHERE ID='" + recentDateIndex + "'";
        Cursor cursor = db.rawQuery(queryStringWanted, null);
        cursor.moveToFirst();
        String olderDate = cursor.getString(0);
        return olderDate;
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
     * Chronologically sorts dates of entries into hash set
     *
     * @return dates of entries
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeSet getDateSet() {
        TreeSet<String> dates = new TreeSet<>();
        String queryString = "SELECT " + COLUMN_DATE_CREATED + " FROM " + ENTRIES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String nextDate = cursor.getString(0);
            dates.add(nextDate);
        }
        return dates;
    }

    /**
     * Accessor for dates of entries in database
     *
     * @return TreeSet of dates of entries in chronological order
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TreeSet<String> getDates() {
        return dates;
    }

    /**
     * Formats the given date object to match the database
     *
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

    /**
     * Checks and formats any duplicate dates into ones to display
     *
     * @param date date to format
     *
     * @return date in ready to display format
     */
    public String getDateText(String date) {
        String formattedDate = date;
        if (date.contains("(")) {
            Character splitPoint = '6';
            String split = String.valueOf(splitPoint);
            String splitDate[] = date.split(" ");
            formattedDate = splitDate[0];
        }
        return formattedDate;
    }

    private TreeSet<String> dates;
}
