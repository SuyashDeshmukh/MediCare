package edu.csulb.android.medicare.Database;

import android.content.Context;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import edu.csulb.android.medicare.Model.History;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.Reminder;

/*
* Description: PillBox Model
* */
public class PillBox {
    private MedicationDatabaseHelper db;
    private static List<Long> tempIds; // Ids of the alarms to be deleted or edited
    private static String tempName; // Ids of the alarms to be deleted or edited

    public List<Long> getTempIds() { return Collections.unmodifiableList(tempIds); }

    public void setTempIds(List<Long> tempIds) { this.tempIds = tempIds; }

    public String getTempName() { return tempName; }

    public void setTempName(String tempName) { this.tempName = tempName; }

    public List<Medication> getPills(Context c) {
        db = new MedicationDatabaseHelper(c);
        List<Medication> allPills = db.getAllMedicines();
        db.close();
        return allPills;
    }

    public long addPill(Context c, Medication pill) {
        db = new MedicationDatabaseHelper(c);
        long pillId = db.insertMedication(pill);
        pill.setId(pillId);
        db.close();
        return pillId;
    }

    public Medication getPillByName(Context c, String pillName){
        db = new MedicationDatabaseHelper(c);
        Medication wantedPill = db.getMedicationByName(pillName);
        db.close();
        return wantedPill;
    }

    public void addAlarm(Context c, Reminder alarm, Medication pill){
        db = new MedicationDatabaseHelper(c);
        db.insertReminder(alarm, pill.getId());
        db.close();
    }

    public List<Reminder> getAlarms(Context c, int dayOfWeek) throws URISyntaxException {
        db = new MedicationDatabaseHelper(c);
        List<Reminder> daysAlarms= db.getRemindersByDayOfWeek(dayOfWeek);
        db.close();
        Collections.sort(daysAlarms);
        return daysAlarms;
    }

    public List<Reminder> getAlarmByPill (Context c, String pillName) throws URISyntaxException {
        db = new MedicationDatabaseHelper(c);
        List<Reminder> pillsAlarms = db.getAllRemindersByMedication(pillName);
        db.close();
        return pillsAlarms;
    }

    public boolean pillExist(Context c, String pillName) {
        db = new MedicationDatabaseHelper(c);
        for(Medication pill: this.getPills(c)) {
            if(pill.getMedicationName().equals(pillName))
                return true;
        }
        return false;
    }

    /*public void deletePill(Context c, String pillName) throws URISyntaxException {
        db = new MedicationDatabaseHelper(c);
        db.deletePill(pillName);
        db.close();
    }

    public void deleteAlarm(Context c, long alarmId) {
        db = new MedicationDatabaseHelper(c);
        db.deleteAlarm(alarmId);
        db.close();
    }*/

    public void addToHistory(Context c, History h){
        db = new MedicationDatabaseHelper(c);
        db.addToHistory(h);
        db.close();
    }

    public List<History> getHistory (Context c){
        db = new MedicationDatabaseHelper(c);
        List<History> history = db.getHistoryList();
        db.close();
        return history;
    }

    public Reminder getAlarmById(Context c, long alarm_id) throws URISyntaxException{
        db = new MedicationDatabaseHelper(c);
        Reminder alarm = db.getAlarmById(alarm_id);
        db.close();
        return alarm;
    }

    public int getDayOfWeek(Context c, long alarm_id) throws URISyntaxException{
        db = new MedicationDatabaseHelper(c);
        int getDayOfWeek = db.getDayOfWeek(alarm_id);
        db.close();
        return getDayOfWeek;
    }
}
