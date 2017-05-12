package edu.csulb.android.medicare.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import edu.csulb.android.medicare.R;

/**
 * Customized Expandable List Adapter
 * http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */

public class ExpandableViewAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableViewAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_medication_details, null);
        }

        // childText should be a string that we can split into two parts
        String[] parts = childText.split("#");
        // The first part is the time
        String time = parts[0];
        // The second part is a string of 0 and 1 that works as a boolean list
        String daysOfWeek = parts[1];

        TextView timeListChild = (TextView) convertView
                .findViewById(R.id.pill_box_time);
        timeListChild.setText(time);

        // Get all the textview objects from the xml file
        TextView monday = (TextView) convertView
                .findViewById(R.id.pill_box_monday);
        TextView tuesday = (TextView) convertView
                .findViewById(R.id.pill_box_tuesday);
        TextView wednesday = (TextView) convertView
                .findViewById(R.id.pill_box_wednesday);
        TextView thursday = (TextView) convertView
                .findViewById(R.id.pill_box_thursday);
        TextView friday = (TextView) convertView
                .findViewById(R.id.pill_box_friday);
        TextView saturday = (TextView) convertView
                .findViewById(R.id.pill_box_saturday);
        TextView sunday = (TextView) convertView
                .findViewById(R.id.pill_box_sunday);

        // The color indicates the days of week when the alarm goes off
        int colorSelected = _context.getResources().getColor(R.color.card_title);
        // The colors indicates the days of week when the alarm doesn't go off
        int colorNotSelected = Color.parseColor("#dddddd");

        // Use dayOfWeek as a boolean list to change the colors of the textviews
        for (int i = 0; i < 7; i++){
            if (i==0) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    sunday.setTextColor(colorSelected);
                } else {
                    sunday.setTextColor(colorNotSelected);
                }
            } else if (i==1) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    monday.setTextColor(colorSelected);
                } else {
                    monday.setTextColor(colorNotSelected);
                }
            } else if (i==2) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    tuesday.setTextColor(colorSelected);
                } else {
                    tuesday.setTextColor(colorNotSelected);
                }
            } else if (i==3) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    wednesday.setTextColor(colorSelected);
                } else {
                    wednesday.setTextColor(colorNotSelected);
                }
            } else if (i==4) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    thursday.setTextColor(colorSelected);
                } else {
                    thursday.setTextColor(colorNotSelected);
                }
            } else if (i==5) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    friday.setTextColor(colorSelected);
                } else {
                    friday.setTextColor(colorNotSelected);
                }
            } else if (i==6) {
                if (daysOfWeek.substring(i, i+1).equals("1")) {
                    saturday.setTextColor(colorSelected);
                } else {
                    saturday.setTextColor(colorNotSelected);
                }
            }
        }

        TextView medicineTakeDosage = (TextView) convertView.findViewById(R.id.medicineTakeDosage);
        medicineTakeDosage.setText("Take "+ parts[2]+" "+parts[3]);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_medication_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
