package edu.csulb.android.medicare.Database;

import android.content.Context;

import java.util.List;

import edu.csulb.android.medicare.Model.Contact;

/*
* Description: Contacts Model
* */

public class Contacts {
    private MedicationDatabaseHelper db;

    public void addContact(Contact contact, Context c){
        db= new MedicationDatabaseHelper(c);
        db.insertContact(contact);
        db.close();
    }

    public List<Contact> getAllContactNames(Context c)
    {
        db = new MedicationDatabaseHelper(c);
        List<Contact> contacts = db.getAllContacts();
        db.close();
        return  contacts;
    }
}
