package edu.csulb.android.medicare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.csulb.android.medicare.Model.Doctor;
import edu.csulb.android.medicare.Model.Reminder;
import edu.csulb.android.medicare.R;

/*
* Description: Recycler Adapter to display doctors
* */

public class RecyclerDoctorAdapter extends RecyclerView.Adapter<RecyclerDoctorAdapter.ViewHolder>  {

    private List<Doctor> doctors;
    Context context;
    Intent directions;

    public RecyclerDoctorAdapter(List<Doctor> doctors, Context context){
        this.doctors = doctors;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        String medicineName;
        public TextView itemDoctorName;
        public TextView itemAddress;
        public TextView itemTitle;
        public TextView itemSpeciality;
        public Button getDirections;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemDoctorName = (TextView)itemView.findViewById(R.id.textDoctorName);
            itemAddress = (TextView)itemView.findViewById(R.id.textAddress);
            itemTitle = (TextView)itemView.findViewById(R.id.textTitle);
            itemSpeciality = (TextView)itemView.findViewById(R.id.textSpeciality);
            getDirections= (Button)itemView.findViewById(R.id.btnDirections);
            getDirections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(directions);

                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_doctor_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String address =doctors.get(i).getAddress();
        viewHolder.itemDoctorName.setText(doctors.get(i).getName());
        viewHolder.itemTitle.setText(doctors.get(i).getTitle());
        viewHolder.itemSpeciality.setText(doctors.get(i).getSpeciality());
        viewHolder.itemAddress.setText(doctors.get(i).getAddress());
        directions = new Intent(android.content.Intent.ACTION_VIEW,
        Uri.parse("google.navigation:q="+address));

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }
}
