package edu.csulb.android.medicare.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import edu.csulb.android.medicare.Adapter.RecyclerAlarmAdapter;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.R;

public class ViewAlarmActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerAlarmAdapter recyclerAlarmAdapter;
    private List<MedicationInformation> medicationInformations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alarm);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerAlarm_view);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAlarmAdapter = new RecyclerAlarmAdapter(medicationInformations,getApplicationContext());
        recyclerView.setAdapter(recyclerAlarmAdapter);
    }
}
