package edu.csulb.android.medicare.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import edu.csulb.android.medicare.Database.PillBox;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;
/*
* Description: Activity to show medicines as a weekly view
* */
public class WeeklyViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Week at a Glance");

        TableLayout stk = (TableLayout) findViewById(R.id.table_calendar);

        PillBox pillBox = new PillBox();

        List<Reminder> alarms = null;

        List<String> days = Arrays.asList("Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday");


        for (int i = 1; i < 8; i++) {

            String day = days.get(i-1);
            TableRow headerRow = new TableRow(this);
            TextView headerText = new TextView(this);

            headerText.setText(day);
            headerText.setTextColor(Color.WHITE);
            headerText.setBackgroundColor(getResources().getColor(R.color.card_title));
            int paddingPixel = 10;
            float density = getResources().getDisplayMetrics().density;
            int paddingDp = (int)(paddingPixel * density);
            headerText.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            headerText.setTypeface(null, Typeface.BOLD);
            headerText.setGravity(Gravity.CENTER);

            headerRow.addView(headerText);
            stk.addView(headerRow);

            //Let headerText span two columns
            TableRow.LayoutParams params = (TableRow.LayoutParams)headerText.getLayoutParams();
            params.span = 3;
            headerText.setLayoutParams(params);

            try {
                alarms = pillBox.getAlarms(this, i);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            if(alarms.size() != 0) {
                for(Reminder alarm: alarms) {
                    TableRow tbrow = new TableRow(this);
                    paddingPixel = 5;
                    density = getResources().getDisplayMetrics().density;
                    paddingDp = (int)(paddingPixel * density);
                    tbrow.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);

                    TextView t1v = new TextView(this);
                    t1v.setText(alarm.getMedicineName());
                    t1v.setMaxEms(6);
                    t1v.setGravity(Gravity.CENTER);
                    tbrow.addView(t1v);

                    TextView t2v = new TextView(this);
                    String time = alarm.getStringTime();
                    t2v.setText(time);
                    t2v.setGravity(Gravity.CENTER);
                    tbrow.addView(t2v);


                    TextView t3v = new TextView(this);
                    String dosage = alarm.getDosageQuantity()+" "+alarm.getDosageUnit()+" "+alarm.getInstructions();
                    t3v.setText(dosage);
                    t3v.setGravity(Gravity.CENTER);
                    tbrow.addView(t3v);

                    stk.addView(tbrow);
                }
            } else {
                TableRow tbrow = new TableRow(this);
                TextView tv = new TextView(this);
                //tv.setGravity(Gravity.CENTER);

                tv.setText("You don't have any reminders for " + day + ".");
                paddingPixel = 5;
                density = getResources().getDisplayMetrics().density;
                paddingDp = (int)(paddingPixel * density);
                tbrow.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                tbrow.addView(tv);
                stk.addView(tbrow);

                //Let tv span one columns
                TableRow.LayoutParams params2 = (TableRow.LayoutParams)tv.getLayoutParams();
                params2.span = 1;
                tv.setLayoutParams(params2);
            }
        }
    }


    @Override
    /** Inflate the menu; this adds items to the action bar if it is present */
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnHome = new Intent(getBaseContext(), NavigationDrawerActivity.class);
        startActivity(returnHome);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent returnHome = new Intent(getBaseContext(), NavigationDrawerActivity.class);
        startActivity(returnHome);
        finish();
    }
}