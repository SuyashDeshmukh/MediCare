package edu.csulb.android.medicare.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.csulb.android.medicare.Model.Contact;
import edu.csulb.android.medicare.Model.TimeQuantity;
import edu.csulb.android.medicare.R;

/*
* Description: Custom Adapter to display contacts
* */

public class CustomContactAdapter extends ArrayAdapter<Contact> {
    private List<Contact> contacts;
    public CustomContactAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource, contacts);
        this.contacts = contacts;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Contact contact = contacts.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row_contact, null);
        TextView rowContactName = (TextView) row.findViewById(R.id.rowContactName);
        TextView rowContactNumber = (TextView) row.findViewById(R.id.rowContactNumber);
        TextView rowContactEmail = (TextView) row.findViewById(R.id.rowContactEmail);
        Button btnCallContact = (Button) row.findViewById(R.id.btnCallContact);
        rowContactName.setText(contact.getName());
        rowContactNumber.setText(contact.getNumber());
        rowContactEmail.setText(contact.getEmail());
        btnCallContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myPhoneNumberUri = "tel:"+contact.getNumber();
                Intent callIntentActivity = new Intent(Intent.ACTION_DIAL,
                        Uri.parse(myPhoneNumberUri));
                getContext().startActivity(callIntentActivity);
            }
        });
        return row;
    }
}
