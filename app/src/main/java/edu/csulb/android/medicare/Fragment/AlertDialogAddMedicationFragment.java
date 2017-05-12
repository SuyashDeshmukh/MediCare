package edu.csulb.android.medicare.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import edu.csulb.android.medicare.Activity.AddMedicationWithBarcodeActivity;
import edu.csulb.android.medicare.Activity.AddMedicationWithManualActivity;
import edu.csulb.android.medicare.R;

public class AlertDialogAddMedicationFragment extends DialogFragment {
    final int BARCODE=0;
    final int MANUAL=1;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alert_choose_method)
                .setItems(R.array.method_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which){
                            case BARCODE:
                                Intent intentBarcode = new Intent(getActivity(),AddMedicationWithBarcodeActivity.class);
                                startActivity(intentBarcode);
                               // getActivity().finish();
                                break;
                            case MANUAL:
                                Intent intentManual = new Intent(getActivity(),AddMedicationWithManualActivity.class);
                                startActivity(intentManual);
                                //getActivity().finish();
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }
}
