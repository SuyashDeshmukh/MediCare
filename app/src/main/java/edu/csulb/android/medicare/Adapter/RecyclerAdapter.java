package edu.csulb.android.medicare.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import edu.csulb.android.medicare.AlarmReceiver;
import edu.csulb.android.medicare.Database.MedicationDatabaseHelper;
import edu.csulb.android.medicare.Model.MedicationInformation;
import edu.csulb.android.medicare.R;
import edu.csulb.android.medicare.Activity.ViewMedicationActivity;

/**
 * Created by Samruddhi on 4/13/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {

    private List<MedicationInformation> medicationInformationsList;
    Context context;

    public RecyclerAdapter(List<MedicationInformation> medicationInformationsList, Context context){
        this.medicationInformationsList = medicationInformationsList;
        this.context = context;
    }
    public void swap(int firstPosition, int secondPosition){
        Collections.swap(medicationInformationsList, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }

    public void remove(int position){
        medicationInformationsList.remove(position);
        //deleteMedication(position);
        notifyItemRemoved(position);
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        String medicineName;
        public TextView itemTitle;
        public TextView itemDosage;
        public TextView itemRepeatTime;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemTitle = (TextView)itemView.findViewById(R.id.rowMedName);
            itemDosage = (TextView)itemView.findViewById(R.id.rowDosage);
            itemRepeatTime = (TextView)itemView.findViewById(R.id.rowRepeatTime);
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

    /*public void deleteMedication(int position) {
        MedicationDatabaseHelper db = new MedicationDatabaseHelper(context);
        final List<MedicationInformation> notes = db.getAllMedicationInformationList();
        long medicatioID = notes.get(position).getId();
        cancelAlarm(medicatioID, context);
        db.deleteMedication(medicatioID);
    }*/

    private void cancelAlarm(long medicatioID, Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent myIntent = new Intent(context , AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,(int)medicatioID ,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_medication_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(medicationInformationsList.get(i).getMedicationName());
        viewHolder.itemDosage.setText(medicationInformationsList.get(i).getDosageQuantity()
                +" "+
                medicationInformationsList.get(i).getDosageUnit());
        viewHolder.itemRepeatTime.setText(medicationInformationsList.get(i).getRepeatTime());
    }

    @Override
    public int getItemCount() {
        return medicationInformationsList.size();
    }
}
