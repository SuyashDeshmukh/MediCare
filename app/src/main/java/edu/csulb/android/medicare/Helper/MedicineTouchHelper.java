package edu.csulb.android.medicare.Helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import edu.csulb.android.medicare.Adapter.RecyclerAdapter;

/**
 * Created by Samruddhi on 4/16/2017.
 */

public class MedicineTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerAdapter recyclerAdapter;
    Context context;

    public MedicineTouchHelper(RecyclerAdapter recyclerAdapter, Context context){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.recyclerAdapter = recyclerAdapter;
        this.context = context;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        recyclerAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        recyclerAdapter.remove(position);
        /*MedicationDatabaseHelper db = new MedicationDatabaseHelper(context);
        List<MedicationInformation> medicationInformations = db.getAllMedicationInformationList();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        // Pass null as the parent view because its going in the
        // dialog layout
        MedicationInformation medicationInformation = medicationInformations.get(position);
        String medicineName = medicationInformation.getMedicationName();
        builder.setTitle("Delete Medication");
        builder.setMessage("Do you want to delete "+medicineName+" ?");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recyclerAdapter.remove(position);
                dialog.dismiss();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

    }
}
