package edu.csulb.android.medicare.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.csulb.android.medicare.Adapter.ExpandableViewAdapter;
import edu.csulb.android.medicare.Database.PillBox;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;
import edu.csulb.android.medicare.Utility.MedicineCompare;
/*
* Description: Fragment to view all medications
*  Resource: Github
* */
public class ViewAllMedicationsFragment extends Fragment {
    ExpandableViewAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    // This data structure allows us to get the ids of the alarms we want to edit
    // and store them in the tempId in the pill box model. The structure is similar
    // to the struture of listDataChild.
    List<List<List<Long>>> alarmIDData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_all_medications, container, false);
        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        try {
            getAllMedications();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        listAdapter = new ExpandableViewAdapter(getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                /*Expand*/
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Collpase*/
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                PillBox pillBox = new PillBox();
                pillBox.setTempIds(alarmIDData.get(groupPosition).get(childPosition));

                return false;
            }
        });
        return view;
    }

    /** Preparing the list data */
    private void getAllMedications() throws URISyntaxException {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        alarmIDData = new ArrayList<List<List<Long>>>();

        PillBox pillbox = new PillBox();
        List<Medication> pills = pillbox.getPills(getContext());
        Collections.sort(pills, new MedicineCompare());

        for (Medication pill: pills){
            String name = pill.getMedicationName();
            listDataHeader.add(name);
            List<String> times = new ArrayList<String>();
            List<Reminder> alarms = pillbox.getAlarmByPill(getContext(), name);
            List<List<Long>> ids = new ArrayList<List<Long>>();

            for (Reminder alarm :alarms){
                String dosage = "#"+alarm.getDosageQuantity()+" "+alarm.getDosageUnit();
                String instructions = "#"+alarm.getInstructions();
                String time = alarm.getStringTime() + daysList(alarm) + dosage + instructions;
                times.add(time);
                ids.add(alarm.getMedicineIds());
            }
            alarmIDData.add(ids);
            listDataChild.put(name, times);
        }
    }

    /**
     * Helper function to obtain a string of the days of the week
     * that can be used as a boolean list
     */
    private String daysList(Reminder alarm){
        String days = "#";
        for(int i=0; i<7; i++){
            if (alarm.getDaysOfWeek()[i])
                days += "1";
            else
                days += "0";
        }
        return days;
    }



}
