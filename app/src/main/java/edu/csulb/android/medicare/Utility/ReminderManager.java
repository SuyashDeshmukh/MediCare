package edu.csulb.android.medicare.Utility;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/*
* Description: Reminder Manager
* */

public class ReminderManager {
    private int _year;
    private int _month;
    private int _day;
    private int _hour;
    private int _minute;
    private String _title;
    private long _eventId;
    private String CALENDAR_URI_BASE="";
    private String DEBUG_TAG="DEBUG_TAG";

    // Add an event to the calendar of the user.
    public void addEvent(Context context) {
        GregorianCalendar calDate = new GregorianCalendar(this._year, this._month, this._day, this._hour, this._minute);

        try {
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, calDate.getTimeInMillis() + 60 * 60 * 1000);
            values.put(CalendarContract.Events.TITLE, this._title);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance()
                    .getTimeZone().getID());
            System.out.println(Calendar.getInstance().getTimeZone().getID());
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // Save the eventId into the Task object for possible future delete.
            this._eventId = Long.parseLong(uri.getLastPathSegment());
            // Add a 5 minute, 1 hour and 1 day reminders (3 reminders)
            setReminder(cr, this._eventId, 5, context);
            setReminder(cr, this._eventId, 60, context);
            setReminder(cr, this._eventId, 1440, context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // routine to add reminders with the event
    public void setReminder(ContentResolver cr, long eventID, int timeBefore, Context context) {
        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
            Cursor c = CalendarContract.Reminders.query(cr, eventID,
                    new String[]{CalendarContract.Reminders.MINUTES});
            if (c.moveToFirst()) {
                System.out.println("calendar"
                        + c.getInt(c.getColumnIndex(CalendarContract.Reminders.MINUTES)));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // function to remove an event from the calendar using the eventId stored within the Task object.
    public void removeEvent(Context context) {
        ContentResolver cr = context.getContentResolver();

        int iNumRowsDeleted = 0;

        Uri eventsUri = Uri.parse(CALENDAR_URI_BASE+"events");
        Uri eventUri = ContentUris.withAppendedId(eventsUri, this._eventId);
        iNumRowsDeleted = cr.delete(eventUri, null, null);

        Log.i(DEBUG_TAG, "Deleted " + iNumRowsDeleted + " calendar entry.");
    }


    public int updateEvent(Context context) {
        int iNumRowsUpdated = 0;
        GregorianCalendar calDate = new GregorianCalendar(this._year, this._month, this._day, this._hour, this._minute);

        ContentValues event = new ContentValues();

        event.put(CalendarContract.Events.TITLE, this._title);
        event.put("hasAlarm", 1); // 0 for false, 1 for true
        event.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
        event.put(CalendarContract.Events.DTEND, calDate.getTimeInMillis()+60*60*1000);

        Uri eventsUri = Uri.parse(CALENDAR_URI_BASE+"events");
        Uri eventUri = ContentUris.withAppendedId(eventsUri, this._eventId);

        iNumRowsUpdated = context.getContentResolver().update(eventUri, event, null,
                null);

        // TODO put text into strings.xml
        Log.i(DEBUG_TAG, "Updated " + iNumRowsUpdated + " calendar entry.");

        return iNumRowsUpdated;
    }
}
