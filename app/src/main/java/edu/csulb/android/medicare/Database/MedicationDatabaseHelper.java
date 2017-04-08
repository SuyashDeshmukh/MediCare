package edu.csulb.android.medicare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;

/**
 * Created by Samruddhi on 4/2/2017.
 */

public class MedicationDatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MedicationDatabase";
    // Labels table name
    private static final String TABLE_MEDICATION = "Medication";
    private static final String TABLE_REMINDER = "Reminder";
    // Labels Table Columns names
    private static final String KEY_MEDICATION_ID = "medication_id";
    private static final String KEY_REMINDER_ID = "reminder_id";
    private static final String KEY_NAME = "medicine_name";
    private static final String KEY_DOSAGE_UNIT = "dosage_unit";
    private static final String KEY_DOSAGE_QUANTITY = "dosage_quantity";
    private static final String KEY_INSTRUCTIONS = "instructions";
    private static final String KEY_REPEAT_TIME = "repeat_time";
    private static final String KEY_DAYS_OF_WEEK = "days_of_week";
    private static final String KEY_NUMBER_OF_DAYS = "number_of_days";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_REMINDER = "keyReminder";
    public MedicationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table medication query
        String CREATE_MEDICATION_TABLE = "CREATE TABLE " +
                TABLE_MEDICATION + "("
                + KEY_MEDICATION_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_DOSAGE_UNIT + " TEXT, "
                + KEY_DOSAGE_QUANTITY + " TEXT, "
                + KEY_INSTRUCTIONS + " TEXT, "
                + KEY_REMINDER + " LONG "
                + " );";

        // Category table reminder query
        String CREATE_REMINDER_TABLE = "CREATE TABLE " +
                TABLE_REMINDER + "("
                + KEY_REMINDER_ID + " INTEGER PRIMARY KEY, "
                + KEY_REPEAT_TIME + " TEXT, "
                + KEY_DAYS_OF_WEEK + " TEXT, "
                + KEY_NUMBER_OF_DAYS + " INTEGER, "
                + KEY_HOUR + " INTEGER, "
                + KEY_MINUTE + " INTEGER, "
                + KEY_START_DATE + " TEXT "
                + " );";

        db.execSQL(CREATE_MEDICATION_TABLE);
        db.execSQL(CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        // Create tables again
        onCreate(db);
    }


    /*
        Adds reminder to the database and returns unique reminder id.
        Values : repeatTime, DaysofWeek, NumberofDays, Hour, Minute, StartDate
     */
    public long insertReminder(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REPEAT_TIME,reminder.getRepeatTime());
        values.put(KEY_DAYS_OF_WEEK,reminder.getDaysOfWeek());
        values.put(KEY_NUMBER_OF_DAYS,reminder.getNumberOfDays());
        values.put(KEY_HOUR,reminder.getHour());
        values.put(KEY_MINUTE,reminder.getMinute());
        values.put(KEY_START_DATE,reminder.getStartDate());
        // Inserting Row
        long reminder_id = db.insert(TABLE_REMINDER, null, values);
        db.close();
        // Closing database connection
        return reminder_id;
    }

    /*
        Adds Medicine details to the database.
        Values : MedicationName, DosageUnit, DosageQuantity, Instructions, KeyReminder
     */
    public long insertMedication(Medication medication){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, medication.getMedicationName());
        values.put(KEY_DOSAGE_UNIT, medication.getDosageUnit());
        values.put(KEY_DOSAGE_QUANTITY, medication.getDosageQuantity());
        values.put(KEY_INSTRUCTIONS,medication.getInstructions());
        values.put(KEY_REMINDER,medication.getKeyReminder());
        // Inserting Row
        long medication_id = db.insert(TABLE_MEDICATION, null, values);
        db.close();
        // Closing database connection
        return medication_id;
    }

    /*
        Retrieves all the medication information from medicine table.
     */

    public List<Medication> getAllMedications(){
        List<Medication> medications = new ArrayList<Medication>();
        // Select All Query
        String selectQuery = "SELECT * FROM " +
                TABLE_MEDICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                medications.add(new Medication(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning medications
        return medications;
    }

    /*
        Retrieves all medication information by performing join operation
        between Medicine and Reminder table.
     */

    public List<MedicationInformation> getAllMedicationInformationList(){
        List<MedicationInformation> medicationInformations = new ArrayList<MedicationInformation>();
        String MY_QUERY = "SELECT * FROM "
                + TABLE_MEDICATION
                + " med INNER JOIN "
                + TABLE_REMINDER +
                " rem ON med."+KEY_REMINDER+" = rem."+KEY_REMINDER_ID+"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                medicationInformations.add(new MedicationInformation(
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_MEDICATION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DOSAGE_UNIT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DOSAGE_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_INSTRUCTIONS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REPEAT_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DAYS_OF_WEEK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NUMBER_OF_DAYS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MINUTE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_START_DATE))));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        return medicationInformations;
    }

    /*
        Retrieves medication information from medicine id by
        join operation between medicine and reminder table.
     */
    public MedicationInformation getMedicationInformationFromID(long medication_id){
        MedicationInformation medicationInformation = new MedicationInformation();
        String MY_QUERY = "SELECT * FROM "
                + TABLE_MEDICATION
                + " med INNER JOIN "
                + TABLE_REMINDER +
                " rem ON med."+KEY_REMINDER+" = rem."+KEY_REMINDER_ID+" WHERE med."+KEY_MEDICATION_ID+"="+medication_id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                medicationInformation = new MedicationInformation(
                        cursor.getLong(cursor.getColumnIndexOrThrow(KEY_MEDICATION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DOSAGE_UNIT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DOSAGE_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_INSTRUCTIONS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_REPEAT_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DAYS_OF_WEEK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NUMBER_OF_DAYS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MINUTE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_START_DATE)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        return medicationInformation;
    }

    /*
        Fetches particular medicine by providing medicine id.
     */
    public Medication getMedicationFromID(Long id){
        Medication medication;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] Projection = {
                KEY_MEDICATION_ID,
                KEY_NAME,
                KEY_DOSAGE_UNIT,
                KEY_DOSAGE_QUANTITY,
                KEY_INSTRUCTIONS,
                KEY_REMINDER
        };

        String selection = KEY_MEDICATION_ID + " = ?";
        String[] selectionArgs = { ""+id };

        Cursor cursor = db.query(
                TABLE_MEDICATION,
                Projection,
                selection,
                selectionArgs,
                null,
                null,
                null

        );
        cursor.moveToNext();
        medication = new Medication(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getLong(5));
        return medication;
    }

    /*
        Fetches reminder details for a particular medication with the help of reminder id.
     */
    public Reminder getReminderForMedication(long reminder_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] Projection = {
                KEY_REPEAT_TIME,
                KEY_DAYS_OF_WEEK,
                KEY_NUMBER_OF_DAYS,
                KEY_HOUR,
                KEY_MINUTE,
                KEY_START_DATE
        };

        String selection = KEY_REMINDER_ID + " = ?";
        String[] selectionArgs = { ""+reminder_id };

        Cursor cursor = db.query(
                TABLE_REMINDER,
                Projection,
                selection,
                selectionArgs,
                null,
                null,
                null

        );

        cursor.moveToNext();
        Reminder reminder;
        reminder = new Reminder(cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
        return reminder;
    }

       /*
        Gets reminder id form the medication table by providing medication id.
         */

    public Long getReminderIDFromMedicationID(Long id){
        Medication medication;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] Projection = {
                KEY_REMINDER
        };

        String selection = KEY_MEDICATION_ID + " = ?";
        String[] selectionArgs = { ""+id };

        Cursor cursor = db.query(
                TABLE_MEDICATION,
                Projection,
                selection,
                selectionArgs,
                null,
                null,
                null

        );
        cursor.moveToNext();
        Long reminderID = cursor.getLong(0);
        return reminderID;
    }

    /*
        Deletes particular medicine details from medicine and reminder tables.
     */
    public void deleteMedication(Long medicationID){
        SQLiteDatabase db = this.getWritableDatabase();
        // Deleting Row
        Long reminderID = getReminderIDFromMedicationID(medicationID);
        db.delete(TABLE_REMINDER, KEY_REMINDER_ID + "=" + reminderID, null);
        db.delete(TABLE_MEDICATION, KEY_MEDICATION_ID + "=" + medicationID, null);
        db.close(); // Closing database connection
    }
}
