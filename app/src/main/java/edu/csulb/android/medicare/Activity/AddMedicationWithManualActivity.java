package edu.csulb.android.medicare.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import edu.csulb.android.medicare.AlarmReceiver;
import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Database.PillBox;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.Model.TimeQuantity;
import edu.csulb.android.medicare.R;

public class AddMedicationWithManualActivity extends AppCompatActivity {

    private AutoCompleteTextView medicineName;
    private Button saveMedication;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Activity activity = this;
    private Spinner spinnerFrequency, spinnerDosage, spinnerRepeatUnit;
    RadioGroup radioGroupDays;
    RadioGroup radioGroupInstructions;
    TextView textDosage, textStartTime, textStartDate;
    EditText repeatTime;
    private String reminderTimeQuntity;
    private String reminderTimeUnit;
    private String duration;
    private String days;
    private int startHour;
    private int startMinute;
    private String startDate;
    private String instructions="No Food Instructions";
    private String dosageUnit;
    private String daysOfWeek = "Everyday";
    private int dosageValue=1;
    private String todayDate;
    String[] dosage_options;
    String dosage;
    private int duration_no_of_days=0;
    ArrayList<String> selectedDays;
    int year,month,day;
    String medName;
    LinearLayout dosagelayout, startTimeLayout, startDateLayout;
    int currentHour, currentMinute;
    private boolean dayOfWeekList[] = new boolean[7];
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    PillBox pillBox = new PillBox();


    public String getTodayDate(){
        String strMonth="", strDay="";
        Calendar mcurrentDate = Calendar.getInstance();
        int year = mcurrentDate.get(Calendar.YEAR);
        int month = mcurrentDate.get(Calendar.MONTH);
        if(month<10)
            strMonth = "0"+month;
        else
            strMonth = ""+month;
         int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        if(day<10)
            strDay = "0"+day;
        else
            strDay = ""+day;
        startDate=strMonth+"/"+strDay+"/"+year;
        return strMonth+"/"+strDay+"/"+year;
    }

    /**
     * This method takes hours and minute as input and returns
     * a string that is like "12:01pm"
     */
    public String setTime(int hour, int minute) {
        String am_pm = (hour < 12) ? "am" : "pm";
        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;
        String minuteWithZero;
        if (minute < 10)
            minuteWithZero = "0" + minute;
        else
            minuteWithZero = "" + minute;
        return nonMilitaryHour + ":" + minuteWithZero + am_pm;
    }

    public void getCurrentHourMinute(){
        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
    }

    public void initialize(){

        medicineName = (AutoCompleteTextView) findViewById(R.id.cardview_name_text);
        dosagelayout = (LinearLayout) findViewById(R.id.layoutDosage);
        startTimeLayout = (LinearLayout) findViewById(R.id.layoutStartTime);
        startDateLayout = (LinearLayout) findViewById(R.id.layoutStartDate);
        textDosage = (TextView) findViewById(R.id.dosage);
        textStartTime = (TextView) findViewById(R.id.startTime);
        textStartDate = (TextView) findViewById(R.id.startDate);
        //repeatTime = (EditText) findViewById(R.id.edit_repeat);
        //spinnerRepeatUnit = (Spinner) findViewById(R.id.spinnerRepeatUnit);
        radioGroupDays = (RadioGroup) findViewById(R.id.radioGroupDays);
        radioGroupInstructions = (RadioGroup) findViewById(R.id.radioGroupInstructions);
        saveMedication = (Button) findViewById(R.id.buttonSaveMedication);
        for(int i=0;i<dayOfWeekList.length;i++){
            dayOfWeekList[i] = true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication_with_manual);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
        Intent intent=getIntent();
        medName=intent.getStringExtra("Medicine Name");

        if(medName!=null)
            medicineName.setText(medName);
        dosageUnit = getResources().getStringArray(R.array.dosage_options)[0];
// Create an ArrayAdapter using the string array and a default spinner layout
/*        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.repeat_unit_options, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerRepeatUnit.setAdapter(adapter);

        spinnerRepeatUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //repeatTime.setText("");
                reminderTimeUnit = getResources().getStringArray(R.array.repeat_unit_options)[position].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        textStartTime.setText(setTime(currentHour,currentMinute));
        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(AddMedicationWithManualActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        textStartTime.setText(setTime(selectedHour, selectedMinute));
                        startHour = selectedHour;
                        startMinute = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.setTitle("Select Start Time");
                timePicker.show();
            }
        });
        textStartDate.setText(getTodayDate());
        /*startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
            }
        });*/
        textDosage.setText(dosageValue+" "+dosageUnit);
        dosagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDosagePicker();

            }
        });

        saveMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminderForMedication();
                finish();
                //save all data
            }
        });


    }

    private void saveReminderForMedication() {
        int checkBoxCounter = 0;
        boolean cancel = false;
        //Medication Name
        if(medicineName.getText().toString().trim().equals("")){
            medicineName.setError("Medicine name is required!");
            cancel = true;
        }
        /*//Medication Name
        if(repeatTime.getText().toString().trim().equals("")){
            repeatTime.setError("Repeat time is required!");
            cancel = true;
        }*/

        Reminder reminder = new Reminder();

        String med_name = medicineName.getText().toString();

        /** If Pill does not already exist */
        if (!pillBox.pillExist(getApplicationContext(),med_name)) {
            Medication medication = new Medication();
            medication.setMedicationName(med_name);
            reminder.setHour(startHour);
            reminder.setMinute(startMinute);
            reminder.setMedicineName(med_name);
            reminder.setDosageQuantity(dosageValue+"");
            reminder.setDosageUnit(dosageUnit);
            reminder.setInstructions(instructions);
            reminder.setRepeatTime(reminderTimeQuntity+" "+reminderTimeUnit);
            reminder.setDaysOfWeek(dayOfWeekList);
            medication.addReminder(reminder);
            long pillId = pillBox.addPill(getApplicationContext() ,medication);
            medication.setId(pillId);
            pillBox.addAlarm(getApplicationContext(),reminder,medication);
        } else { // If Pill already exists
            Medication medication = pillBox.getPillByName(getApplicationContext(), med_name);
            reminder.setHour(startHour);
            reminder.setMinute(startMinute);
            reminder.setMedicineName(med_name);
            reminder.setDosageQuantity(dosageValue+"");
            reminder.setDosageUnit(dosageUnit);
            reminder.setInstructions(instructions);
            reminder.setRepeatTime(reminderTimeQuntity+" "+reminderTimeUnit);
            reminder.setDaysOfWeek(dayOfWeekList);
            medication.addReminder(reminder);
            pillBox.addAlarm(getApplicationContext(),reminder,medication);
        }
        List<Long> ids = new LinkedList<Long>();
        try {
            List<Reminder> reminders = pillBox.getAlarmByPill(getApplicationContext(), med_name);
            for(Reminder temp: reminders) {
                if(temp.getHour() == startHour && temp.getMinute() == startMinute) {
                    ids = temp.getMedicineIds();
                    break;
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for(int i=0; i<7; i++) {
            if (dayOfWeekList[i] && med_name.length() != 0) {

                int dayOfWeek = i;

                long _id = ids.get(checkBoxCounter);
                int id = (int) _id;
                checkBoxCounter++;

                /** This intent invokes the activity AlertActivity, which in turn opens the AlertAlarm window */
                //Intent intent = new Intent(getBaseContext(), AlertActivity.class);
                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                intent.putExtra("medicine_name", med_name);

                //pendingIntent = PendingIntent.getActivity(getBaseContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent = PendingIntent.getBroadcast(getBaseContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                /** Getting a reference to the System Service ALARM_SERVICE */
                alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

                /** Creating a calendar object corresponding to the date and time set by the user */
                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, startHour);
                calendar.set(Calendar.MINUTE, startMinute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                /** Converting the date and time in to milliseconds elapsed since epoch */
                long alarm_time = calendar.getTimeInMillis();

                if (calendar.before(Calendar.getInstance()))
                    alarm_time += AlarmManager.INTERVAL_DAY * 7;

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_time,
                        alarmManager.INTERVAL_DAY * 7, pendingIntent);
            }
        }
        /** Input form is not completely filled out */
        if(!cancel) { // Input form is completely filled out
            Toast.makeText(getBaseContext(), "Reminder for " + med_name + " is set successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NavigationDrawerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void displayDosagePicker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.select_dosage);
        builder.setCancelable(true);

        LayoutInflater inflater = (activity).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_dialog_dosage,null);
        NumberPicker numberPicker = (NumberPicker) layout.findViewById(R.id.numberPicker);
        loadNumberPicker(numberPicker,500);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                dosageValue = newVal;
            }
        });
        spinnerDosage = (Spinner) layout.findViewById(R.id.spinnerDosage);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddMedicationWithManualActivity.this,
                R.array.dosage_options, android.R.layout.simple_spinner_item);
        dosage_options = getResources().getStringArray(R.array.dosage_options);
        spinnerDosage.setAdapter(adapter);
        spinnerDosage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dosageUnit = dosage_options[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder = new AlertDialog.Builder(activity);
        builder.setView(layout);
        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dosage = dosageValue+" "+dosageUnit;
                textDosage.setText(dosage);
                dialog.dismiss();
                //  You can write the code  to save the selected item here
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }


    private void displayDatePicker() {
        Calendar mcurrentDate = Calendar.getInstance();
        int year = mcurrentDate.get(Calendar.YEAR);
        int month = mcurrentDate.get(Calendar.MONTH);
        int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker;
        datePicker = new DatePickerDialog(AddMedicationWithManualActivity.this, new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String strMonth="",strDay="";
                if(month<10)
                    strMonth = "0"+month;
                if(dayOfMonth<10)
                    strDay = "0"+dayOfMonth;
                textStartDate.setText(strMonth+"/"+strDay+"/"+year);
                startDate = strMonth+"/"+strDay+"/"+year;
            }
        }, year, month, day);
        datePicker.setTitle("Set Start Date");
        datePicker.show();
    }
    /*public void saveMedication(){
        MedicationDatabaseHelper databaseHelper = new MedicationDatabaseHelper(getApplicationContext());
        boolean cancel = false;
        //Medication Name
        if(medicineName.getText().toString().trim().equals("")){
            medicineName.setError("Medicine name is required!");
            cancel = true;
        }
        //Medication Name
        if(repeatTime.getText().toString().trim().equals("")){
            repeatTime.setError("Repeat time is required!");
            cancel = true;
        }
        reminderTimeQuntity = repeatTime.getText().toString();
        long reminder_id = addReminder();
        long medication_id = addMedication(reminder_id);
        MedicationInformation medicationInformation = databaseHelper.getMedicationInformationFromID(medication_id);
        int hour = medicationInformation.getHour();
        int minute = medicationInformation.getMinute();
        // Date Format: month/day/year
        String date = medicationInformation.getStartDate();
        //Log.d("date",date);
        addAlarm(hour,minute,medication_id);
        if(!cancel) {
            Intent intent = new Intent(AddMedicationWithManualActivity.this, NavigationDrawerActivity.class);
            startActivity(intent);
            finish();
        }
    }*/

    /*public long addReminder(){
        MedicationDatabaseHelper databaseHelper = new MedicationDatabaseHelper(getApplicationContext());
        Reminder reminder = new Reminder();
        reminder.setRepeatTime(reminderTimeQuntity+" "+reminderTimeUnit);
        reminder.setDaysOfWeek(daysOfWeek);
        reminder.setNumberOfDays(duration_no_of_days);
        reminder.setHour(startHour);
        reminder.setMinute(startMinute);
        reminder.setStartDate(startDate);
        long reminder_id = databaseHelper.insertReminder(reminder);
        databaseHelper.close();
        return reminder_id;
    }*/

    private void addAlarm(int hr,int min,long id) {
        String[] dividedDate = startDate.split("/");
        int repeatAfterMins;
        if(reminderTimeUnit.matches("min"))
            repeatAfterMins = Integer.parseInt(reminderTimeQuntity);
        else
            repeatAfterMins = Integer.parseInt(reminderTimeQuntity)*60;
        Log.d("Message","Alarm to repeat after "+repeatAfterMins+" Mins");

        Intent myIntent = new Intent(this , AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this,(int)id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int startYear=Integer.parseInt(dividedDate[2]);
        int startDay=Integer.parseInt(dividedDate[1]);
        int startMonth=Integer.parseInt(dividedDate[0]);
        ///Create a calendar object corresponding to the time set by the user
        Calendar calendar = Calendar.getInstance();
        /*calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);*/
        Log.e("Details :","Year:"+startYear);
        Log.e("Details :","Month:"+startMonth);
        Log.e("Details :","Day:"+startDay);
        Log.e("Details :","Hour:"+hr);
        Log.e("Details :","Mins:"+min);

        calendar.set(startYear,startMonth,startDay,hr,min);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*repeatAfterMins,pendingIntent);
    }

    /*public long addMedication(long reminder_id){
        MedicationDatabaseHelper databaseHelper = new MedicationDatabaseHelper(getApplicationContext());
        Medication medication = new Medication();
        medication.setMedicationName(medicineName.getText().toString());
        medication.setDosageUnit(dosageUnit);
        medication.setDosageQuantity(dosageValue+"");
        medication.setInstructions(instructions);
        medication.setKeyReminder(reminder_id);
        long medication_id = databaseHelper.insertMedication(medication);
        databaseHelper.close();
        return medication_id;
    }*/

    public void onClickDays(View view){
        final String[] daysofweek = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        switch(view.getId())
        {
            case R.id.radioEveryDay:
                RadioButton button = (RadioButton) findViewById(R.id.radioEveryDay);
                days = button.getText().toString();
                daysOfWeek = days;
                for(int i=0;i<daysofweek.length;i++){
                    dayOfWeekList[i] = true;
                }
                break;
            case R.id.radioSpecificDay:
                for(int i=0;i<daysofweek.length;i++){
                    dayOfWeekList[i] = false;
                }
                selectedDays = new ArrayList<String>();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle(R.string.select_days);
                builder.setCancelable(true);
                builder.setMultiChoiceItems(daysofweek, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedDays.add(daysofweek[indexSelected]);
                            if(indexSelected+1 == 7)
                                dayOfWeekList[0] = true;
                            else
                                dayOfWeekList[indexSelected+1] = true;
                        } else if (selectedDays.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            selectedDays.remove(Integer.valueOf(indexSelected));
                            dayOfWeekList[indexSelected] = false;
                        }
                    }
                })
                        // Add action buttons
                        .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                StringBuilder sb = new StringBuilder();
                                String separator = ",";
                                for (String s : selectedDays) {
                                    sb.append(separator).append(s);
                                }

                                daysOfWeek = sb.substring(separator.length()); // remove leading separator
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel
                                dialog.dismiss();
                            }
                        });

                builder.create();
                builder.show();
                break;
        }
    }

    public void onClickInstructions(View view){
        RadioButton button;
        switch(view.getId())
        {
            case R.id.radioBefore:
                button = (RadioButton) findViewById(R.id.radioBefore);
                instructions = button.getText().toString();
                break;
            case R.id.radioWith:
                button = (RadioButton) findViewById(R.id.radioWith);
                instructions = button.getText().toString();
                break;
            case R.id.radioAfter:
                button = (RadioButton) findViewById(R.id.radioAfter);
                instructions = button.getText().toString();
                break;
            case R.id.radioNo:
                button = (RadioButton) findViewById(R.id.radioNo);
                instructions = button.getText().toString();
                break;
        }
    }

    public void loadNumberPicker(NumberPicker numberPicker,int size){
        String[] nums = new String[size];
        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i+1);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(nums.length);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(nums);
        numberPicker.setValue(1);
    }
}
