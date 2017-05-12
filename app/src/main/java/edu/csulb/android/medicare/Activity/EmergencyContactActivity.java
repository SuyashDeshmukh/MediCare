package edu.csulb.android.medicare.Activity;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import edu.csulb.android.medicare.Database.Contacts;
import edu.csulb.android.medicare.Fragment.ContactListFragment;
import edu.csulb.android.medicare.Helper.ContactHelper;
import edu.csulb.android.medicare.Model.Contact;
import edu.csulb.android.medicare.R;

import static edu.csulb.android.medicare.Fragment.ContactListFragment.customContactAdapter;
import static edu.csulb.android.medicare.Fragment.ContactListFragment.listViewContacts;

public class EmergencyContactActivity extends AppCompatActivity {
    EditText edtContactName, edtContactNumber,edtContactEmail;
    Button btnAddContact, btnCancel;
    Contacts contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contacts = new Contacts();

        edtContactName = (EditText) findViewById(R.id.edtContactName);
        edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);
        edtContactEmail = (EditText)findViewById(R.id.edtContactEmail);

        btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertContact();
            }
        });

    }

    public void insertContact()
    {
        String name = edtContactName.getText().toString();
        String number = edtContactNumber.getText().toString();
        String email = edtContactEmail.getText().toString();
        Contact NewContact = new Contact(name,number,email);
        contacts.addContact(NewContact,getApplicationContext());
        customContactAdapter.notifyDataSetChanged();
        listViewContacts.invalidate();
        Toast.makeText(this, "Contact "+name+" added Successfully!", Toast.LENGTH_SHORT).show();

        // finish();
    }

    public void addContact() {
        Intent intent = new Intent(this, ContactListFragment.class);
        if (edtContactName.getText().toString().equals("")
                && edtContactNumber.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill both fields...",
                    Toast.LENGTH_SHORT).show();
        } else {
            ContactHelper.insertContact(getContentResolver(),
                    edtContactName.getText().toString(), edtContactNumber
                            .getText().toString());
            edtContactName.setText("");
            edtContactNumber.setText("");

            startActivity(intent);
        }

    }
}
