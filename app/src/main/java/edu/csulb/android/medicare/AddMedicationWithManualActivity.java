package edu.csulb.android.medicare;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;

public class AddMedicationWithManualActivity extends AppCompatActivity {

    private AutoCompleteTextView medicineName;
    private Button saveMedication;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Activity activity = this;
    Spinner spinnerFrequency, spinnerDosage, spinnerRepeatUnit;
    LinearLayout frequencylayout;
    ListView listviewFrequency;
    List<TimeQuantity> frequencies;
    RadioGroup radioGroupDuration;
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
    private String instructions;
    private String dosageUnit;
    private String daysOfWeek;
    private int dosageValue=1;
    private String todayDate;
    String[] dosage_options;
    String dosage;
    PendingIntent pendingIntent;
    private int duration_no_of_days=0;
    ArrayList<String> selectedDays;
    int year,month,day;
    String medName;
    LinearLayout dosagelayout, startTimeLayout, startDateLayout;
    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Are you sure?")
                        .setMessage("You have unsaved changes. Do you want quit without saving?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /*
        Fetches current system date to display by default.
     */
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

    /*
        Fetches current System Time to display by default.
     */
    public String getCurrentTime(){
        String strHour="", strMin="";
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        startHour = hour;
        int minute = mcurrentTime.get(Calendar.MINUTE);
        startMinute = minute;
        if(hour<10)
            strHour = "0"+hour;
        else
            strHour = ""+hour;
        if(minute<10)
            strMin = "0"+minute;
        else
            strMin=""+minute;
        return strHour+":"+strMin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication_with_manual);
        Intent intent=getIntent();
        medName=intent.getStringExtra("Medicine Name");
        medicineName = (AutoCompleteTextView) findViewById(R.id.cardview_name_text);
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        medicineName.setAdapter(autoCompleteAdapter);
        if(medName!=null)
            medicineName.setText(medName);
        dosagelayout = (LinearLayout) findViewById(R.id.layoutDosage);
        startTimeLayout = (LinearLayout) findViewById(R.id.layoutStartTime);
        startDateLayout = (LinearLayout) findViewById(R.id.layoutStartDate);
        textDosage = (TextView) findViewById(R.id.dosage);
        textStartTime = (TextView) findViewById(R.id.startTime);
        textStartDate = (TextView) findViewById(R.id.startDate);
        radioGroupDuration = (RadioGroup) findViewById(R.id.radioGroupDuration);
        repeatTime = (EditText) findViewById(R.id.edit_repeat);
        spinnerRepeatUnit = (Spinner) findViewById(R.id.spinnerRepeatUnit);
        dosageUnit = getResources().getStringArray(R.array.dosage_options)[0];
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
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
        });

        radioGroupDuration = (RadioGroup) findViewById(R.id.radioGroupDuration);

        radioGroupDays = (RadioGroup) findViewById(R.id.radioGroupDays);
        textStartTime.setText(getCurrentTime());
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
                        textStartTime.setText( selectedHour + ":" + selectedMinute);
                        startHour = selectedHour;
                        startMinute = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePicker.setTitle(R.string.select_start_time);
                timePicker.show();
            }
        });
        textStartDate.setText(getTodayDate());
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                datePicker.setTitle("Select Start Date");
                datePicker.show();
            }
        });
        textDosage.setText(R.string.none);

        /* Displays dialog for selecting the dosage amount.
           ( Quantities : mg,teaspoons,tablespoons)    */

        dosagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Get the layout inflater
                // Pass null as the parent view because its going in the
                // dialog layout
                builder.setTitle(R.string.select_dosage);
                builder.setCancelable(true);

                LayoutInflater inflater = (activity).getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_dialog_dosage,null);
                NumberPicker numberPicker = (NumberPicker) layout.findViewById(R.id.numberPicker);
                loadNumberPicker(numberPicker,0,500,500);

                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        dosageValue = newVal;
                    }
                });
                Spinner s = (Spinner) layout.findViewById(R.id.spinnerDosage);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddMedicationWithManualActivity.this,
                        R.array.dosage_options, android.R.layout.simple_spinner_item);
                dosage_options = getResources().getStringArray(R.array.dosage_options);
                s.setAdapter(adapter);
                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dosage = dosageValue+" "+dosageUnit;
                        textDosage.setText(dosage);
                        dialog.dismiss();
                        //  You can write the code  to save the selected item here
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

            }
        });
        radioGroupInstructions = (RadioGroup) findViewById(R.id.radioGroupInstructions);

        saveMedication = (Button) findViewById(R.id.buttonSaveMedication);
        saveMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedication();
                finish();
                //save all data
            }
        });


    }

    // Saves the medicine information to the database and adds medication navigation drawer activity.
    public void saveMedication(){
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
    }

    // Adds details to the Reminder Mddel and stores in the model.
    public long addReminder(){
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
    }

    // Sends pending intent after repeat time set by the user with the help of Alarm Manager.
    private void addAlarm(int hr,int min,long id) {
        String[] dividedDate = startDate.split("/");
        int repeatAfterMins = Integer.parseInt(repeatTime.getText().toString());
        Log.d("Message","Alarm to repeat after "+repeatAfterMins+" Mins");

        Intent myIntent = new Intent(this , AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this,(int)id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int startYear=Integer.parseInt(dividedDate[2]);
        int startDay=Integer.parseInt(dividedDate[1]);
        int startMonth=Integer.parseInt(dividedDate[0]);
        Calendar calendar = Calendar.getInstance();
        Log.e("Details :","Year:"+startYear);
        Log.e("Details :","Month:"+startMonth);
        Log.e("Details :","Day:"+startDay);
        Log.e("Details :","Hour:"+hr);
        Log.e("Details :","Mins:"+min);

        calendar.set(startYear,startMonth,startDay,hr,min);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*repeatAfterMins,pendingIntent);
    }

    // Adds medicine information to the Medication Model and save the information to the database.
    public long addMedication(long reminder_id){
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
    }

    // Dialog to select the number of days to set the reminder.
    public void onClickDuration(View view){
        switch(view.getId())
        {
            case R.id.radioContinuous:
                RadioButton button = (RadioButton) findViewById(R.id.radioContinuous);
                duration_no_of_days = 0;
                break;
            case R.id.radioDays:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Get the layout inflater
                LayoutInflater inflater = (activity).getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the
                // dialog layout
                builder.setTitle(R.string.number_of_days);
                builder.setCancelable(true);
                final View layout = inflater.inflate(R.layout.custom_dialog_days, null);
                NumberPicker numberPicker = (NumberPicker) layout.findViewById(R.id.numberPicker);
                loadNumberPicker(numberPicker,0,30,30);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        duration_no_of_days = newVal;
                    }
                });
                builder.setView(layout)
                        // Add action buttons
                        .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                            //@TargetApi(23)
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
                break;
        }
    }

    // Dialog to select number of days to repeat the reminder
    public void onClickDays(View view){
        switch(view.getId())
        {
            case R.id.radioEveryDay:
                RadioButton button = (RadioButton) findViewById(R.id.radioContinuous);
                days = button.getText().toString();
                break;
            case R.id.radioSpecificDay:
                final String[] daysofweek = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
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
                        } else if (selectedDays.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            selectedDays.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                        // Add action buttons
                        .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
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
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

    // OnClick Listener for Instructions section.

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

    // Used to load number picker for Dosage and Number of Days
    public void loadNumberPicker(NumberPicker numberPicker,int min,int max,int size){
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
