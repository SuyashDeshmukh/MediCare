package edu.csulb.android.medicare.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import edu.csulb.android.medicare.Activity.DoctorListActivity;
import edu.csulb.android.medicare.Adapter.RecyclerDoctorAdapter;
import edu.csulb.android.medicare.Adapter.RecyclerPharmacyAdapter;
import edu.csulb.android.medicare.Model.Doctor;
import edu.csulb.android.medicare.Model.Pharmacy;
import edu.csulb.android.medicare.R;

/*
* Description: Fragment to find doctors using better doctor API
* */
public class FindDoctorFragment extends Fragment {
    ProgressBar progressBar;
    private View mProgressView;
    private View mPharmaciesListView;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerDoctorAdapter recyclerDoctorAdapter;
    public static List<Doctor> doctors = new ArrayList<>();
    Location location;
    LocationManager locationManager;
    LocationListener mLocationListener;
    private EditText query;
    private Button search;
    private RadioButton radioMale, radioFemale;
    private RadioGroup genderRadioGroup;
    private String searchQuery;
    private String gender;
    private final String API_KEY = "e7799266601466e712570713130919da";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_find_doctor, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        //showProgress(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //doctors.clear();
        doctors = new ArrayList<>();
        query=(EditText)view.findViewById(R.id.queryvalue);
        genderRadioGroup = (RadioGroup)view.findViewById(R.id.radioGroupGender);
        radioMale = (RadioButton) view.findViewById(R.id.radioM);
        radioFemale = (RadioButton) view.findViewById(R.id.radioF);
        search = (Button)view.findViewById(R.id.btnSearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery=query.getText().toString();
                if(radioMale.isChecked())
                    gender = radioMale.getText().toString();
                else if(radioFemale.isChecked())
                    gender = radioFemale.getText().toString();
                FindDoctor findDoctor = new FindDoctor();
                findDoctor.execute("dsds");

            }
        });
        return view;
    }

    class FindDoctor extends AsyncTask<String,Void,Boolean> {
        RequestQueue requestQueue;
        JsonObjectRequest jsonObjectRequest;
        private String URL = "https://api.betterdoctor.com/2016-03-01/doctors?query=" + searchQuery +
                "&location=37.773%2C-122.413%2C100&user_location=37.773%2C-122.413&gender="+ gender.toLowerCase() +"&skip=0&limit=10" +
                "&user_key=" + API_KEY;

        Intent showdoctors = new Intent(getActivity(), DoctorListActivity.class);
        boolean isSuccessful;

        @Override
        protected void onPostExecute(Boolean success) {
            //showProgress(false);
            if (success){
                startActivity(showdoctors);
            }
            else {
                Toast.makeText(getActivity(), "Please Enter a valid criteria", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //showProgress(true);
            requestQueue = Volley.newRequestQueue(getContext());
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray data = response.getJSONArray("data");
                        for(int i=0;i<data.length();i++)
                        {
                            JSONObject jsonObject= (JSONObject)data.get(i);
                            String first_name = jsonObject.getJSONObject("profile").getString("first_name");
                            String last_name = jsonObject.getJSONObject("profile").getString("last_name");
                            String name = first_name+" "+last_name;
                            String title = jsonObject.getJSONObject("profile").getString("title");
                            String speciality = jsonObject.getJSONArray("specialties").getJSONObject(0).getString("name");
                            String street = jsonObject.getJSONArray("practices").getJSONObject(0).getJSONObject("visit_address").getString("street");
                            String city = jsonObject.getJSONArray("practices").getJSONObject(0).getJSONObject("visit_address").getString("city");
                            String state  = jsonObject.getJSONArray("practices").getJSONObject(0).getJSONObject("visit_address").getString("state");
                            String zip = jsonObject.getJSONArray("practices").getJSONObject(0).getJSONObject("visit_address").getString("zip");
                            String address= street+", "+city+", "+state+", "+zip;
                           // String address=" ";
                            //String speciality=" ";
                            Doctor doctor = new Doctor(name,address,title,speciality);
                            doctors.add(doctor);
                        }
                        isSuccessful=true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                    isSuccessful=false;

                }
            });
            requestQueue.add(jsonObjectRequest);

            while (!jsonObjectRequest.hasHadResponseDelivered())
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            return isSuccessful;
        }
    }
}
