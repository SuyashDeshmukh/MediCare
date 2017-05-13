package edu.csulb.android.medicare.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.csulb.android.medicare.Adapter.RecyclerAlarmAdapter;
import edu.csulb.android.medicare.Adapter.RecyclerHistoryAdapter;
import edu.csulb.android.medicare.Database.PillBox;
import edu.csulb.android.medicare.Model.History;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;

/*
* Description: Fragment to updte history
* */
public class HistoryFragment extends Fragment {
    List<History> histories;
    PillBox pillBox =  new PillBox();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    //private RecyclerView.Adapter adapter;
    private RecyclerHistoryAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        //TableLayout stk = (TableLayout) view.findViewById(R.id.table_today);
        histories = pillBox.getHistory(container.getContext());
        recyclerView =
                (RecyclerView) view.findViewById(R.id.recycler_history_view);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerHistoryAdapter(histories, getActivity());
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

}
