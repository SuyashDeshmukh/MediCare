package edu.csulb.android.medicare.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.csulb.android.medicare.Adapter.RecyclerAdapter;
import edu.csulb.android.medicare.Fragment.AlertDialogAddMedicationFragment;
import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Fragment.ContactListFragment;
import edu.csulb.android.medicare.Fragment.FindDoctorFragment;
import edu.csulb.android.medicare.Fragment.PharmaciesListFragment;
import edu.csulb.android.medicare.Fragment.RemindersFragment;
import edu.csulb.android.medicare.Fragment.ViewAllMedicationsFragment;
import edu.csulb.android.medicare.Helper.MedicineTouchHelper;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;
import edu.csulb.android.medicare.Utility.MedicineCompare;
import edu.csulb.android.medicare.Utility.SharedValues;

import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.TextView;
/*
* Description: Main Activiy which contains all the fragments
* */
public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //ListView listViewMedication;
    List<Medication> medications = new ArrayList<>();
    List<MedicationInformation> medicationInformations = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TextView userEmail;
    private TextView userName;
    private ImageView imgview;
    //private RecyclerView.Adapter adapter;
    private RecyclerAdapter recyclerAdapter;
    private FloatingActionButton fab;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar toolbar;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        String email = SharedValues.getValue("Email");
        String name = SharedValues.getValue("Name");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        userEmail= (TextView)hView.findViewById(R.id.userEmail);
        userName= (TextView)hView.findViewById(R.id.userName);
        imgview=(ImageView)hView.findViewById(R.id.imageView);
        //imgview.
        userEmail.setText(email);
        userName.setText(name);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                openDialogAddMedicine();
            }
        });
        fragmentManager.beginTransaction().replace(R.id.content_frame, new RemindersFragment()).commit();
        toolbar.setTitle("Reminders");
        fab.show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        //listViewMedication = (ListView) findViewById(R.id.listViewMedication);
        try {
            loadMedicationsList();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //registerForContextMenu(listViewMedication);
        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(medicationInformations,getApplicationContext());
        recyclerView.setAdapter(recyclerAdapter);
        ItemTouchHelper.Callback callback = new MedicineTouchHelper(recyclerAdapter, context);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
        if (id == R.id.action_weeklyview) {
            Intent intent =  new Intent(this,WeeklyViewActivity.class);
            startActivity(intent);
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
            // Handle the Reminders action
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RemindersFragment()).commit();
            toolbar.setTitle("Reminders");
            fab.show();
            /*Intent i = new Intent(this, ReminderActivity.class);
            startActivity(i);*/
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ViewAllMedicationsFragment()).commit();
            toolbar.setTitle("Medications");
            fab.hide();

        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PharmaciesListFragment()).commit();
            toolbar.setTitle("Nearby Pharmacies");
            fab.hide();


        } else if (id == R.id.nav_share) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FindDoctorFragment()).commit();
            toolbar.setTitle("Find Doctor");
            fab.hide();

        } else if (id == R.id.nav_send) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ContactListFragment()).commit();
            toolbar.setTitle("Emergency Contacts");
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), EmergencyContactActivity.class);
                    startActivity(i);
                }
            });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openDialogAddMedicine(){

        AlertDialogAddMedicationFragment alertDialogAddMedicationFragment = new AlertDialogAddMedicationFragment();
        alertDialogAddMedicationFragment.show(getSupportFragmentManager(),"question");

    }

    public void loadMedicationsList() throws URISyntaxException {
        MedicationDatabaseHelper db = new MedicationDatabaseHelper(getApplicationContext());
        MedicationInformation medicationInformation = new MedicationInformation();
        List<Medication> medications = db.getAllMedicines();
        Collections.sort(medications, new MedicineCompare());

        for (Medication medication: medications){
            String name = medication.getMedicationName();
            medicationInformation.setMedicationName(name);
            List<String> times = new ArrayList<String>();
            List<Reminder> reminders = db.getAllRemindersByMedication(name);
            List<List<Long>> ids = new ArrayList<List<Long>>();

            for (Reminder reminder :reminders){
                String time = reminder.getStringTime();// + daysList(reminder);
                times.add(time);
                ids.add(reminder.getMedicineIds());
            }

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

}
