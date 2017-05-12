package edu.csulb.android.medicare.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.csulb.android.medicare.Activity.EmergencyContactActivity;
import edu.csulb.android.medicare.Adapter.CustomContactAdapter;
import edu.csulb.android.medicare.Adapter.RecyclerPharmacyAdapter;
import edu.csulb.android.medicare.Database.Contacts;
import edu.csulb.android.medicare.Model.Contact;
import edu.csulb.android.medicare.Model.Pharmacy;
import edu.csulb.android.medicare.R;


public class ContactListFragment extends Fragment {
    ProgressBar progressBar;
    private View mProgressView;
    private View mPharmaciesListView;
    private LinearLayoutManager layoutManager;
    public static ListView listViewContacts;
    public static CustomContactAdapter customContactAdapter;
    private List<Contact> contactList = new ArrayList<>();
    Contacts contacts = new Contacts();
    Button btnAddContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact_list_list, container, false);
        /*btnAddContact = (Button) view.findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EmergencyContactActivity.class);
                startActivity(intent);
            }
        });*/
        listViewContacts = (ListView) view.findViewById(R.id.listViewContacts);
        contactList = contacts.getAllContactNames(getContext());
        customContactAdapter = new CustomContactAdapter(getContext(), R.layout.custom_row_contact, contactList);
        listViewContacts.setAdapter(customContactAdapter);
        listViewContacts.invalidate();
        return view;
    }



}
