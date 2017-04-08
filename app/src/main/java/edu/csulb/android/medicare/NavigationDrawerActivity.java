package edu.csulb.android.medicare;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;

/*
    Activity to display all the reminder scheduled along with details.
    User can add reminders from this activity with two different methods.
 */
public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listViewMedication;
    List<Medication> medications = new ArrayList<>();
    List<MedicationInformation> medicationInformations = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                openDialogAddMedicine();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listViewMedication = (ListView) findViewById(R.id.listViewMedication);
        loadMedicationsList();
        registerForContextMenu(listViewMedication);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openDialogAddMedicine(){

        AlertDialogAddMedicationFragment alertDialogAddMedicationFragment = new AlertDialogAddMedicationFragment();
        alertDialogAddMedicationFragment.show(getSupportFragmentManager(),"question");

    }


    public void loadMedicationsList(){
        MedicationDatabaseHelper db = new MedicationDatabaseHelper(getApplicationContext());
        medicationInformations = db.getAllMedicationInformationList();
        listViewMedication.setAdapter(new CustomMedicationAdapter(this, R.layout.custom_row_medication, medicationInformations));
        db.close();
        listViewMedication.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MedicationInformation medicationInformation = medicationInformations.get(position);
                Intent intent = new Intent(getApplicationContext(), ViewMedicationActivity.class);
                intent.putExtra("medication_id",medicationInformation.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteMedication(info.position);
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                loadMedicationsList();
                // TODO: Implement Delete
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /*
        Deletes medication from the database and cancels alarm
        by calling the cancelAlarm method.
     */
    public void deleteMedication(int position) {
        MedicationDatabaseHelper db = new MedicationDatabaseHelper(getApplicationContext());
        final List<MedicationInformation> notes = db.getAllMedicationInformationList();
        long medicatioID = notes.get(position).getId();
        cancelAlarm(medicatioID);
        db.deleteMedication(medicatioID);
    }

    /*
        Cancels the alarm by passing the unique medication id
        to the AlarmManager cancel method.
     */
    private void cancelAlarm(long medicatioID) {
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent myIntent = new Intent(this , AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),(int)medicatioID ,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }
}
