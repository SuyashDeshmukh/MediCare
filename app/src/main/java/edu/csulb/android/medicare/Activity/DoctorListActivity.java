package edu.csulb.android.medicare.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import edu.csulb.android.medicare.Adapter.RecyclerDoctorAdapter;
import edu.csulb.android.medicare.Fragment.FindDoctorFragment;
import edu.csulb.android.medicare.R;

public class DoctorListActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerDoctorAdapter recyclerDoctorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_doctor_view);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerDoctorAdapter = new RecyclerDoctorAdapter(FindDoctorFragment.doctors,getApplicationContext());
        recyclerView.setAdapter(recyclerDoctorAdapter);
    }
}