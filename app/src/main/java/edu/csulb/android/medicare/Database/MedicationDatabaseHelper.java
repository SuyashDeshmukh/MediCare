package edu.csulb.android.medicare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.csulb.android.medicare.Model.Contact;
import edu.csulb.android.medicare.Model.History;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;

/*
* Description: Database Helper for to store medications
* */

public class MedicationDatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MedicationDatabase";
    // Labels table name
    private static final String TABLE_MEDICATION = "Medication";
    private static final String TABLE_REMINDER = "Reminder";
    private static final String TABLE_MEDICATION_REMINDER = "Medication_Alarm";
    private static final String TABLE_HISTORY = "History";
    private static final String TABLE_CONTACTS="Contacts";
    // Labels Table Columns names
    private static final String KEY_MEDICATION_ID = "medication_id";
    private static final String KEY_REMINDER_ID = "reminder_id";
    private static final String KEY_NAME = "medicine_name";
    private static final String KEY_DOSAGE_UNIT = "dosage_unit";
    private static final String KEY_DOSAGE_QUANTITY = "dosage_quantity";
    private static final String KEY_INSTRUCTIONS = "instructions";
    private static final String KEY_REPEAT_TIME = "repeat_time";
    private static final String KEY_DAYS_OF_WEEK = "days_of_week";
    //private static final String KEY_NUMBER_OF_DAYS = "number_of_days";
    private static final String KEY_INTENT = "intent";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_REMINDER = "keyReminder";
    //Alarm table
    private static final String KEY_MEDICATION_REMINDER_ID = "medication_reminder_id";
    //History table
    private static final String KEY_HISTORY_ID = "history_id";
    private static final String KEY_DATE_TAKEN = "date_taken";

    //Contacts Table

    private static final String KEY_CONTACT_ID = "contact_id";
    private static final String KEY_CONTACT_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";

    // Category table medication query
    private static final String CREATE_MEDICATION_TABLE = "CREATE TABLE " +
            TABLE_MEDICATION + "("
            + KEY_MEDICATION_ID + " INTEGER PRIMARY KEY, "
            + KEY_NAME + " TEXT "
            + " );";

    // Table reminder
    // Fields: id, intent, hour, minute, medicine_name, day, dosage quantity, dosage unit, instructions, repeat_time
    String CREATE_REMINDER_TABLE = "CREATE TABLE " +
            TABLE_REMINDER + "("
            + KEY_REMINDER_ID + " INTEGER PRIMARY KEY, "
            + KEY_INTENT + " TEXT, "
            + KEY_HOUR + " INTEGER, "
            + KEY_MINUTE + " INTEGER, "
            + KEY_NAME + " TEXT, "
            + KEY_DAYS_OF_WEEK + " INTEGER, "
            + KEY_DOSAGE_QUANTITY + " INTEGER, "
            + KEY_DOSAGE_UNIT + " TEXT, "
            + KEY_INSTRUCTIONS + " TEXT, "
            + KEY_REPEAT_TIME + " TEXT "
            + " );";

    // Category table medication and reminder key mapping
    private static final String CREATE_MEDICATION_REMINDER_TABLE = "CREATE TABLE " +
            TABLE_MEDICATION_REMINDER + "("
            + KEY_MEDICATION_REMINDER_ID + " INTEGER PRIMARY KEY, "
            + KEY_MEDICATION_ID + " INTEGER NOT NULL, "
            + KEY_REMINDER_ID + " INTEGER NOT NULL "
            + " );";

    // Table history
    // Fields: id, medicine_name, date, hour, minute,
    String CREATE_HISTORY_TABLE = "CREATE TABLE " +
            TABLE_HISTORY + "("
            + KEY_HISTORY_ID + " INTEGER PRIMARY KEY, "
            + KEY_NAME + " TEXT, "
            + KEY_HOUR + " TEXT, "
            + KEY_MINUTE + " TEXT, "
            + KEY_DATE_TAKEN + " TEXT "
            + " );";

    // Table Contacts
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " +
            TABLE_CONTACTS + "("
            + KEY_CONTACT_ID + " INTEGER PRIMARY KEY, "
            + KEY_CONTACT_NAME + " TEXT, "
            + KEY_PHONE + " TEXT, "
            + KEY_EMAIL + " TEXT "
            + " );";

    public MedicationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MEDICATION_TABLE);
        db.execSQL(CREATE_REMINDER_TABLE);
        db.execSQL(CREATE_MEDICATION_REMINDER_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_MEDICATION_REMINDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
    }

    /*
    * Function Name: Add medicine
    * Input Parameter: Medicine name
    * */
    public long insertMedication(Medication medication){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, medication.getMedicationName());
        // Inserting Row
        long medication_id = db.insert(TABLE_MEDICATION, null, values);
        db.close();
        // Closing database connection
        return medication_id;
    }

    /*
    * Function Name: Add reminder
    * Input Parameter: id, intent, hour, minute, medicine_name, day, dosage quantity, dosage unit, instructions, repeat_time
    * */
    public long[] insertReminder(Reminder reminder, long medicine_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long[] reminder_ids = new long[7];
        // Insert record for each day of week for given alarm
        int index = 0;
        for (boolean day : reminder.getDaysOfWeek()) {
            if (day) {
                ContentValues values = new ContentValues();
                values.put(KEY_HOUR, reminder.getHour());
                values.put(KEY_MINUTE, reminder.getMinute());
                values.put(KEY_DAYS_OF_WEEK, index + 1);
                values.put(KEY_NAME, reminder.getMedicineName());
                values.put(KEY_DOSAGE_QUANTITY, reminder.getDosageQuantity());
                values.put(KEY_DOSAGE_UNIT, reminder.getDosageUnit());
                values.put(KEY_REPEAT_TIME, reminder.getRepeatTime());
                values.put(KEY_INSTRUCTIONS, reminder.getInstructions());

                long reminder_id = db.insert(TABLE_REMINDER, null, values);
                reminder_ids[index] = reminder_id;
                Log.e("ReminderID",reminder_id+"");
                createMedicineReminderLink(medicine_id, reminder_id);
            }
            index++;
        }
        return reminder_ids;
    }
    /*
    * Function Name: createMedicineReminderLink
    * Description: For each reminder, add corresponding medication link
    * Input Parameter: medication_id, reminder_id
    * */
    private long createMedicineReminderLink(long medication_id, long reminder_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEDICATION_ID, medication_id);
        values.put(KEY_REMINDER_ID, reminder_id);

        long medRemLink_id = db.insert(TABLE_MEDICATION_REMINDER, null, values);

        return medRemLink_id;
    }

    /*
    * Function Name: addToHistory
    * Description: add record to history when medicine is marked as taken
    * Input Parameter: name, date, hour, minute
    * */
    public void addToHistory(History history) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, history.getMedicineName());
        values.put(KEY_DATE_TAKEN, history.getDateTaken());
        values.put(KEY_HOUR, history.getHourTaken());
        values.put(KEY_MINUTE, history.getMinuteTaken());

        /** Insert row */
        db.insert(TABLE_HISTORY, null, values);
    }

    /*
        * Function Name: getHistoryList
        * Description: get all record from history
        * */
    public List<History> getHistoryList(){
        List<History> histories = new ArrayList<History>();
        String MY_QUERY = "SELECT * FROM "
                + TABLE_HISTORY ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                histories.add(new History(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MINUTE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_TAKEN))));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        return histories;
    }

    /*
    * Function Name: getMedicationByName
    * Description: get medicine object from medicine name
    * Input Parameter: name
    * */
    public Medication getMedicationByName(String medicineName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbPill = "SELECT * FROM "
                + TABLE_MEDICATION        + " WHERE "
                + KEY_NAME      + " = "
                + "'"   + medicineName  + "'";

        Cursor c = db.rawQuery(dbPill, null);

        Medication medication = new Medication();

        if (c.moveToFirst() && c.getCount() >= 1) {
            medication.setId(c.getLong(c.getColumnIndex(KEY_MEDICATION_ID)));
            medication.setMedicationName(c.getString(c.getColumnIndex(KEY_NAME)));
            c.close();
        }
        return medication;
    }

    /**
     * allows the pillBox to retrieve all the pill rows from database
     * @return a list of pill model objects
     */
    public List<Medication> getAllMedicines() {
        List<Medication> medications = new ArrayList<>();
        String dbPills = "SELECT * FROM " + TABLE_MEDICATION;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbPills, null);

        /** Loops through all rows, adds to list */
        if (c.moveToFirst()) {
            do {
                Medication medication = new Medication();
                medication.setId(c.getLong(c.getColumnIndex(KEY_MEDICATION_ID)));
                medication.setMedicationName(c.getString(c.getColumnIndex(KEY_NAME)));

                medications.add(medication);
            } while (c.moveToNext());
        }
        c.close();
        return medications;
    }


    /**
     * Allows pillBox to retrieve all Alarms linked to a Pill
     * uses combineAlarms helper method
     */
    public List<Reminder> getAllRemindersByMedication(String medicationName) throws URISyntaxException {
        List<Reminder> remindersByMedication = new ArrayList<Reminder>();

        String selectQuery = "SELECT * FROM "       +
                TABLE_REMINDER         + " reminder, "    +
                TABLE_MEDICATION          + " medication, "     +
                TABLE_MEDICATION_REMINDER    + " medicationReminder WHERE "           +
                "medication."             + KEY_NAME      + " = '"    + medicationName + "'" +
                " AND medication."        + KEY_MEDICATION_ID         + " = "     +
                "medicationReminder."        + KEY_MEDICATION_ID  +
                " AND reminder."       + KEY_REMINDER_ID         + " = "     +
                "medicationReminder."        + KEY_REMINDER_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(c.getInt(c.getColumnIndex(KEY_REMINDER_ID)));
                reminder.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
                reminder.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                reminder.setMedicineName(c.getString(c.getColumnIndex(KEY_NAME)));
                reminder.setDosageQuantity(c.getString(c.getColumnIndex(KEY_DOSAGE_QUANTITY)));
                reminder.setDosageUnit(c.getString(c.getColumnIndex(KEY_DOSAGE_UNIT)));
                reminder.setInstructions(c.getString(c.getColumnIndex(KEY_INSTRUCTIONS)));
                reminder.setRepeatTime(c.getString(c.getColumnIndex(KEY_REPEAT_TIME)));
                remindersByMedication.add(reminder);
            } while (c.moveToNext());
        }

        c.close();


        return combineAlarms(remindersByMedication);
    }

    /**
     * returns all individual alarms that occur on a certain day of the week,
     * alarms returned do not know of their counterparts that occur on different days
     * @param day an integer that represents the day of week
     * @return a list of Alarms (not combined into full-model-alarms)
     */
    public List<Reminder> getRemindersByDayOfWeek(int day) {
        List<Reminder> remindersByDay = new ArrayList<Reminder>();

        String selectQuery = "SELECT * FROM "       +
                TABLE_REMINDER     + " reminder WHERE "   +
                "reminder."        + KEY_DAYS_OF_WEEK      +
                " = '"          + day               + "'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(c.getInt(c.getColumnIndex(KEY_REMINDER_ID)));
                reminder.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
                reminder.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
                reminder.setMedicineName(c.getString(c.getColumnIndex(KEY_NAME)));
                reminder.setDosageQuantity(c.getString(c.getColumnIndex(KEY_DOSAGE_QUANTITY)));
                reminder.setDosageUnit(c.getString(c.getColumnIndex(KEY_DOSAGE_UNIT)));
                reminder.setInstructions(c.getString(c.getColumnIndex(KEY_INSTRUCTIONS)));
                reminder.setRepeatTime(c.getString(c.getColumnIndex(KEY_REPEAT_TIME)));

                remindersByDay.add(reminder);
            } while (c.moveToNext());
        }
        c.close();

        return remindersByDay;
    }


    /**
     *
     */
    public Reminder getAlarmById(long reminder_id) throws URISyntaxException {

        String dbAlarm = "SELECT * FROM "   +
                TABLE_REMINDER + " WHERE "     +
                KEY_REMINDER_ID   + " = "         + reminder_id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        Reminder reminder = new Reminder();
        reminder.setId(c.getInt(c.getColumnIndex(KEY_REMINDER_ID)));
        reminder.setHour(c.getInt(c.getColumnIndex(KEY_HOUR)));
        reminder.setMinute(c.getInt(c.getColumnIndex(KEY_MINUTE)));
        reminder.setMedicineName(c.getString(c.getColumnIndex(KEY_NAME)));
        reminder.setDosageQuantity(c.getString(c.getColumnIndex(KEY_DOSAGE_QUANTITY)));
        reminder.setDosageUnit(c.getString(c.getColumnIndex(KEY_DOSAGE_UNIT)));
        reminder.setInstructions(c.getString(c.getColumnIndex(KEY_INSTRUCTIONS)));
        reminder.setRepeatTime(c.getString(c.getColumnIndex(KEY_REPEAT_TIME)));

        c.close();

        return reminder;
    }

    /**
     * Private helper function that combines rows in the databse back into a
     * full model-alarm with a dayOfWeek array.
     * @return a list of model-alarms
     * @throws URISyntaxException
     */
    private List<Reminder> combineAlarms(List<Reminder> reminders) throws URISyntaxException {
        List<String> timesOfDay = new ArrayList<>();
        List<Reminder> combinedAlarms = new ArrayList<>();

        for (Reminder reminder : reminders) {
            if (timesOfDay.contains(reminder.getStringTime())) {
                /** Add this db row to alarm object */
                for (Reminder reminderObj : combinedAlarms) {
                    if (reminderObj.getStringTime().equals(reminder.getStringTime())) {
                        int day = getDayOfWeek(reminder.getId());
                        boolean[] days = reminderObj.getDaysOfWeek();
                        days[day-1] = true;
                        reminderObj.setDaysOfWeek(days);
                        reminderObj.addMedicineIds(reminder.getId());
                    }
                }
            } else {
                /** Create new Alarm object with day of week array */
                Reminder newAlarm = new Reminder();
                boolean[] days = new boolean[7];

                newAlarm.setMedicineName(reminder.getMedicineName());
                newAlarm.setMinute(reminder.getMinute());
                newAlarm.setHour(reminder.getHour());
                newAlarm.addMedicineIds(reminder.getId());
                newAlarm.setDosageQuantity(reminder.getDosageQuantity());
                newAlarm.setDosageUnit(reminder.getDosageUnit());
                newAlarm.setInstructions(reminder.getInstructions());
                newAlarm.setRepeatTime(reminder.getRepeatTime());

                int day = getDayOfWeek(reminder.getId());
                days[day-1] = true;
                newAlarm.setDaysOfWeek(days);

                timesOfDay.add(reminder.getStringTime());
                combinedAlarms.add(newAlarm);
            }
        }

        Collections.sort(combinedAlarms);
        return combinedAlarms;
    }

    /**
     * Get a single pillapp.Model-Alarm
     * Used as a helper function
     */
    public int getDayOfWeek(long reminder_id) throws URISyntaxException {
        SQLiteDatabase db = this.getReadableDatabase();

        String dbAlarm = "SELECT * FROM "   +
                TABLE_REMINDER + " WHERE "     +
                KEY_REMINDER_ID   + " = "         + reminder_id;

        Cursor c = db.rawQuery(dbAlarm, null);

        if (c != null)
            c.moveToFirst();

        int dayOfWeek = c.getInt(c.getColumnIndex(KEY_DAYS_OF_WEEK));
        c.close();

        return dayOfWeek;
    }

    public long insertContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME, contact.getName());
        values.put(KEY_EMAIL,contact.getEmail());
        values.put(KEY_PHONE,contact.getNumber());
        // Inserting Row
        long contact_id = db.insert(TABLE_CONTACTS, null, values);
        db.close();
        // Closing database connection
        return contact_id;
    }

    public List<Contact> getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Contact> contacts = new ArrayList<>();
        String namesQuery = "SELECT * FROM "+TABLE_CONTACTS;

        Cursor cursor = db.rawQuery(namesQuery,null);
        if(cursor.moveToFirst())
        {
            do {
                contacts.add(new Contact(cursor.getString(1), cursor.getString(2), cursor.getString(3)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;

    }



    /*public List<Medication> getAllMedications(){
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

    public void deleteMedication(Long medicationID){
        SQLiteDatabase db = this.getWritableDatabase();
        // Deleting Row
        Long reminderID = getReminderIDFromMedicationID(medicationID);
        db.delete(TABLE_REMINDER, KEY_REMINDER_ID + "=" + reminderID, null);
        db.delete(TABLE_MEDICATION, KEY_MEDICATION_ID + "=" + medicationID, null);
        db.close(); // Closing database connection
    }




    public List<MedicationInformation> getMedicationInformationByDay(String day){
        List<MedicationInformation> medicationInformationList = new ArrayList<>();
        String MY_QUERY = "SELECT * FROM "
                + TABLE_MEDICATION
                + " med INNER JOIN "
                + TABLE_REMINDER +
                " rem ON med."+KEY_REMINDER+" = rem."+KEY_REMINDER_ID+" WHERE rem."+KEY_DAYS_OF_WEEK+" like %"+day+"%";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                medicationInformationList.add(new MedicationInformation(
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
        return medicationInformationList;
    }*/
}
