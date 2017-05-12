package edu.csulb.android.medicare.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;

public class ViewMedicationActivity extends AppCompatActivity {

    private TextView txtMedicineName, txtStartTime, txtStartDate, txtRepeatTime, txtNumberOfDays, txtDaysOfWeek, txtDosage, txtInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medication);

        initialize();
        Bundle bundle = getIntent().getExtras();
        Long medication_id = bundle.getLong("medication_id");
        viewMedication(medication_id);
    }

    public void initialize(){
        txtMedicineName = (TextView) findViewById(R.id.textMedicine);
        txtMedicineName.setSelected(true);
        txtStartTime = (TextView) findViewById(R.id.startTime);
        txtStartDate = (TextView) findViewById(R.id.startDate);
        txtRepeatTime = (TextView) findViewById(R.id.repeatTime);
        txtNumberOfDays = (TextView) findViewById(R.id.numberOfDays);
        txtDaysOfWeek = (TextView) findViewById(R.id.daysOfWeek);
        txtDosage = (TextView) findViewById(R.id.dosage);
        txtInstructions = (TextView) findViewById(R.id.instructions);
    }

    public void viewMedication(long medication_id){
        /*MedicationDatabaseHelper db = new MedicationDatabaseHelper(getApplicationContext());
        *//*Medication medication = db.getMedicationFromID(medication_id);
        Reminder reminder = db.getReminderForMedication(medication.getKeyReminder());*//*
        MedicationInformation medicationInformation = db.getMedicationInformationFromID(medication_id);
        txtMedicineName.setText(medicationInformation.getMedicationName());
        int hour = medicationInformation.getHour();
        int minute = medicationInformation.getMinute();
        String strHour, strMin;
        if(hour<10)
            strHour = "0"+hour;
        else
            strHour = ""+hour;
        if(minute<10)
            strMin = "0"+minute;
        else
            strMin=""+minute;
        txtStartTime.setText(strHour+":"+strMin);
        txtStartDate.setText(medicationInformation.getStartDate());
        txtRepeatTime.setText(medicationInformation.getRepeatTime());
        int no_of_days = medicationInformation.getNumberOfDays();
        Log.e("DURATION",no_of_days+"");
        String duration = "";
        if(no_of_days == 0){
            duration = "Continuous";
        } else {
            duration = no_of_days+" days";
        }
        txtNumberOfDays.setText(duration);
        txtDaysOfWeek.setText(medicationInformation.getDaysOfWeek());
        txtDosage.setText(medicationInformation.getDosageQuantity()+" "+medicationInformation.getDosageUnit());
        txtInstructions.setText(medicationInformation.getInstructions());*/
    }
}
