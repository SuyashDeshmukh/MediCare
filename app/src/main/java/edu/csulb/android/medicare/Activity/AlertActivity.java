package edu.csulb.android.medicare.Activity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.csulb.android.medicare.AlarmReceiver;
import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Fragment.AlertReminderDialogFragment;
import edu.csulb.android.medicare.Model.History;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.R;
/*
* Description: to display alert as a dialog box
* */
public class AlertActivity extends FragmentActivity {
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    MedicationDatabaseHelper db;
    public static boolean isFirstAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MedicationDatabaseHelper(getApplicationContext());
       // Create dialogn Window
        AlertReminderDialogFragment alertReminder = new AlertReminderDialogFragment();

        /** Opening the Alert Dialog Window. This will be opened when the alarm goes off */
        alertReminder.show(getSupportFragmentManager(), "AlertAlarm");
    }

    // Snooze
    public void doNeutralClick(String medicineName){
        final int _id = (int) System.currentTimeMillis();
        final long minute = 60000;
        long snoozeLength = 1;
        long currTime = System.currentTimeMillis();
        long min = currTime + minute * snoozeLength;

        Intent intent = new Intent(getBaseContext(), AlertActivity.class);
        intent.putExtra("medicine_name", medicineName);

        pendingIntent = PendingIntent.getActivity(getBaseContext(), _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, min, pendingIntent);
        Toast.makeText(getBaseContext(), "Alarm for " + medicineName + " was snoozed for 1 minute", Toast.LENGTH_SHORT).show();

        finish();

    }

    // I took medicine
    public void doPositiveClick(String medicationName){
        Medication medication = db.getMedicationByName(medicationName);
        History history = new History();

        Calendar takeTime = Calendar.getInstance();
        Date date = takeTime.getTime();
        String dateString = new SimpleDateFormat("MMM d, yyyy").format(date);

        int hour = takeTime.get(Calendar.HOUR_OF_DAY);
        int minute = takeTime.get(Calendar.MINUTE);
        String am_pm = (hour < 12) ? "am" : "pm";

        history.setHourTaken(hour);
        history.setMinuteTaken(minute);
        history.setDateTaken(dateString);
        history.setMedicineName(medicationName);

        db.addToHistory(history);

        String stringMinute;
        if (minute < 10)
            stringMinute = "0" + minute;
        else
            stringMinute = "" + minute;

        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;

        Toast.makeText(getBaseContext(),  medicationName + " was taken at "+ nonMilitaryHour + ":" + stringMinute + " " + am_pm + ".", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getBaseContext(), NavigationDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    // I skipped
    public void doNegativeClick(){
        finish();
    }
}
