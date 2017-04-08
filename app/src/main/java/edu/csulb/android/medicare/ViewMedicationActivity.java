package edu.csulb.android.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;

public class ViewMedicationActivity extends AppCompatActivity {

    TextView txtMedicineName, txtStartTime, txtStartDate, txtRepeatTime, txtNumberOfDays, txtDaysOfWeek, txtDosage, txtInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medication);
        txtMedicineName = (TextView) findViewById(R.id.textMedicine);
        txtStartTime = (TextView) findViewById(R.id.startTime);
        txtStartDate = (TextView) findViewById(R.id.startDate);
        txtRepeatTime = (TextView) findViewById(R.id.repeatTime);
        txtNumberOfDays = (TextView) findViewById(R.id.numberOfDays);
        txtDaysOfWeek = (TextView) findViewById(R.id.daysOfWeek);
        txtDosage = (TextView) findViewById(R.id.dosage);
        txtInstructions = (TextView) findViewById(R.id.instructions);

        Bundle bundle = getIntent().getExtras();
        Long medication_id = bundle.getLong("medication_id");
        viewMedication(medication_id);
    }

    /*
        Fetches medicine information from the database
        display the details in the listview rows.
     */

    public void viewMedication(long medication_id){
        MedicationDatabaseHelper db = new MedicationDatabaseHelper(getApplicationContext());
        /*Medication medication = db.getMedicationFromID(medication_id);
        Reminder reminder = db.getReminderForMedication(medication.getKeyReminder());*/
        MedicationInformation medicationInformation = db.getMedicationInformationFromID(medication_id);
        txtMedicineName.setText(medicationInformation.getMedicationName());
        txtStartTime.setText(medicationInformation.getHour()+":"+medicationInformation.getMinute());
        txtStartDate.setText(medicationInformation.getStartDate());
        txtRepeatTime.setText(medicationInformation.getRepeatTime());
        txtNumberOfDays.setText(medicationInformation.getNumberOfDays()+"");
        txtDaysOfWeek.setText(medicationInformation.getDaysOfWeek());
        txtDosage.setText(medicationInformation.getDosageQuantity()+" "+medicationInformation.getDosageUnit());
        txtInstructions.setText(medicationInformation.getInstructions());
    }
}
