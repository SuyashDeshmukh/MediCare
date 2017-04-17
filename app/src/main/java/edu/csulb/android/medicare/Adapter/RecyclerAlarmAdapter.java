package edu.csulb.android.medicare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.R;
import edu.csulb.android.medicare.Activity.ViewMedicationActivity;

/**
 * Created by Samruddhi on 4/16/2017.
 */

public class RecyclerAlarmAdapter extends RecyclerView.Adapter<RecyclerAlarmAdapter.ViewHolder>  {

    private List<MedicationInformation> medicationInformationsList;
    Context context;

    public RecyclerAlarmAdapter(List<MedicationInformation> medicationInformationsList, Context context){
        this.medicationInformationsList = medicationInformationsList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        String medicineName;
        public TextView itemMedicineName;
        public TextView itemScheduleTime;
        public TextView itemLastTaken;
        public TextView itemTakeDosage;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemMedicineName = (TextView)itemView.findViewById(R.id.textMedicineName);
            itemScheduleTime = (TextView)itemView.findViewById(R.id.scheduleTime);
            itemLastTaken = (TextView)itemView.findViewById(R.id.rowRepeatTime);
            itemTakeDosage = (TextView)itemView.findViewById(R.id.takeDosage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    MedicationInformation medicationInformation = medicationInformationsList.get(position);
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, ViewMedicationActivity.class);
                    intent.putExtra("medication_id",medicationInformation.getId());
                    context.startActivity(intent);

                }
            });
        }
    }

    @Override
    public RecyclerAlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_alarm_layout, viewGroup, false);
        RecyclerAlarmAdapter.ViewHolder viewHolder = new RecyclerAlarmAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAlarmAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemMedicineName.setText(medicationInformationsList.get(i).getMedicationName());
        viewHolder.itemScheduleTime.setText(medicationInformationsList.get(i).getDosageQuantity()
                +" "+
                medicationInformationsList.get(i).getDosageUnit());
        viewHolder.itemLastTaken.setText(medicationInformationsList.get(i).getRepeatTime());
        viewHolder.itemTakeDosage.setText(medicationInformationsList.get(i).getDosageQuantity()
                +" "+
                medicationInformationsList.get(i).getDosageUnit());
    }

    @Override
    public int getItemCount() {
        return medicationInformationsList.size();
    }
}
