package edu.csulb.android.medicare.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.csulb.android.medicare.Model.History;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;

/**
 * Created by Samruddhi on 4/16/2017.
 */

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder>  {

    private List<History> histories;
    Context context;

    public RecyclerHistoryAdapter(List<History> histories, Context context){
        this.histories = histories;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMedicineName;
        public TextView itemTakenDate;
        public TextView itemTakenTime;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemMedicineName = (TextView)itemView.findViewById(R.id.textMedicineName);
            itemTakenDate = (TextView)itemView.findViewById(R.id.takenOn);
            itemTakenTime = (TextView)itemView.findViewById(R.id.textTime);
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
    public RecyclerHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_history_layout, viewGroup, false);
        RecyclerHistoryAdapter.ViewHolder viewHolder = new RecyclerHistoryAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerHistoryAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemMedicineName.setText(histories.get(i).getMedicineName());
        viewHolder.itemTakenDate.setText(histories.get(i).getDateTaken());
        viewHolder.itemTakenTime.setText(histories.get(i).getStringTime());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}
