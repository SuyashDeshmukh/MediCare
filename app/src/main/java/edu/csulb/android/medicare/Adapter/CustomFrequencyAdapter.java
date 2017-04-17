package edu.csulb.android.medicare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.csulb.android.medicare.R;
import edu.csulb.android.medicare.Model.TimeQuantity;

/**
 * Created by Samruddhi on 4/1/2017.
 */

public class CustomFrequencyAdapter extends ArrayAdapter<TimeQuantity> {
    private List<TimeQuantity> timeQuantityList;
    public CustomFrequencyAdapter(Context context, int resource, List<TimeQuantity> timeQuantityList) {
        super(context, resource, timeQuantityList);
        this.timeQuantityList = timeQuantityList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimeQuantity timeQuantity = timeQuantityList.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row_frequency, null);
        TextView rowTime = (TextView) row.findViewById(R.id.rowTime);
        TextView rowQuantity = (TextView) row.findViewById(R.id.rowQuantity);
        rowTime.setText(timeQuantity.getTime());
        rowQuantity.setText(timeQuantity.getQuantity());
        return row;
    }
}
