package edu.csulb.android.medicare.Utility;

import java.util.Comparator;

import edu.csulb.android.medicare.Model.Medication;

/**
 * Created by Samruddhi on 5/6/2017.
 */

public class MedicineCompare implements Comparator<Medication> {

    @Override
    public int compare(Medication pill1, Medication pill2){

        String firstName = pill1.getMedicationName();
        String secondName = pill2.getMedicationName();
        return firstName.compareTo(secondName);
    }
}
