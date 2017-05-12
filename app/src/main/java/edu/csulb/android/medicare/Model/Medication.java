package edu.csulb.android.medicare.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Samruddhi on 4/2/2017.
 */

public class Medication {
    private Long id;
    private String medicationName;
    private List<Reminder> reminders = new LinkedList<Reminder>();

    public Medication(){

    }

    public Medication(Long id, String medicationName) {
        this.id = id;
        this.medicationName = medicationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
        Collections.sort(reminders);
    }

}
