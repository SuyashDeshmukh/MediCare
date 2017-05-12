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
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;
import edu.csulb.android.medicare.Activity.ViewMedicationActivity;

/**
 * Created by Samruddhi on 4/16/2017.
 */

public class RecyclerAlarmAdapter extends RecyclerView.Adapter<RecyclerAlarmAdapter.ViewHolder>  {

    private List<Reminder> reminders;
    Context context;

    public RecyclerAlarmAdapter(List<Reminder> reminders, Context context){
        this.reminders = reminders;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        String medicineName;
        public TextView itemMedicineName;
        public TextView itemScheduleTime;
        public TextView itemInstructionsA;
        public TextView itemTakeDosage;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemMedicineName = (TextView)itemView.findViewById(R.id.textMedicineName);
            itemScheduleTime = (TextView)itemView.findViewById(R.id.scheduleTime);
            itemTakeDosage = (TextView)itemView.findViewById(R.id.takeDosage);
            itemInstructionsA = (TextView)itemView.findViewById(R.id.textInstructionsA);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Reminder reminder = reminders.get(position);
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, ViewMedicationActivity.class);
                    intent.putExtra("reminder_id",reminder.getId());
                    context.startActivity(intent);

                }
            });*/
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
        viewHolder.itemMedicineName.setText(reminders.get(i).getMedicineName());
        viewHolder.itemScheduleTime.setText(reminders.get(i).getStringTime());
        viewHolder.itemInstructionsA.setText(reminders.get(i).getInstructions());
        viewHolder.itemTakeDosage.setText(reminders.get(i).getDosageQuantity()
                +" "+
                reminders.get(i).getDosageUnit());
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}
