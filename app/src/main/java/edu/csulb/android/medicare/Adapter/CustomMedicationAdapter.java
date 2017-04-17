package edu.csulb.android.medicare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Model.Medication;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;

/**
 * Created by Samruddhi on 4/3/2017.
 */

public class CustomMedicationAdapter extends ArrayAdapter<MedicationInformation> {
    private List<MedicationInformation> medicationList;
    public CustomMedicationAdapter(Context context, int resource, List<MedicationInformation> medicationList) {
        super(context, resource, medicationList);
        this.medicationList = medicationList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MedicationInformation medicationInformation = medicationList.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row_medication, null);
        TextView textMedName = (TextView) row.findViewById(R.id.rowMedName);
        TextView textDosage = (TextView) row.findViewById(R.id.rowDosage);
        TextView textReminderTime = (TextView) row.findViewById(R.id.rowRepeatTime);
        textMedName.setText(medicationInformation.getMedicationName());
        textDosage.setText(medicationInformation.getDosageQuantity()+" "+medicationInformation.getDosageUnit());
        textReminderTime.setText(medicationInformation.getRepeatTime());
        return row;
    }
}
