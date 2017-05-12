package edu.csulb.android.medicare.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.csulb.android.medicare.Adapter.RecyclerAlarmAdapter;
import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Database.PillBox;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;

public class TomorrowFragment extends Fragment {
    List<Reminder> reminders;
    PillBox pillBox =  new PillBox();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    //private RecyclerView.Adapter adapter;
    private RecyclerAlarmAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tomorrow, container, false);
        //TableLayout stk = (TableLayout) view.findViewById(R.id.table_today);

        //User user = new User("sam","kal","email","8978675645","pwd","cpwd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)+1;
        Log.e("Today",day+"");
        if(day == 8)
            day = 1;

        try {
            reminders = pillBox.getAlarms(getContext(), day);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        recyclerView =
                (RecyclerView) view.findViewById(R.id.recycler_tomo_alarm_view);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAlarmAdapter(reminders, getActivity());
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

}
