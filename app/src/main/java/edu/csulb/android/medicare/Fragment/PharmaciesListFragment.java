package edu.csulb.android.medicare.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import edu.csulb.android.medicare.Adapter.RecyclerPharmacyAdapter;
import edu.csulb.android.medicare.Model.Pharmacy;
import edu.csulb.android.medicare.R;

/*
* Description: Fragment to update Pharmacies List
* */
public class PharmaciesListFragment extends Fragment {
    ProgressBar progressBar;
    private View mProgressView;
    private View mPharmaciesListView;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerPharmacyAdapter recyclerPharmacyAdapter;
    private List<Pharmacy> pharmacyList = new ArrayList<>();
    private String URL ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.7908020,-118.1354820&radius=1000&type=pharmacy&key=AIzaSyB7DLv3Kgy-5ISaXKj_ljxI2IgpfFYBmvo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pharmacies_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerPharmacy_view);
        mPharmaciesListView = view.findViewById(R.id.pharmacies_form);
        mProgressView = view.findViewById(R.id.pharmacies_progress);

        layoutManager = new LinearLayoutManager(getContext());
        showProgress(true);
        GetPharmaciesTask pharmaciesTask = new GetPharmaciesTask();
        pharmaciesTask.execute("test");
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerPharmacyAdapter = new RecyclerPharmacyAdapter(pharmacyList,getContext());
        recyclerView.setAdapter(recyclerPharmacyAdapter);
        return view;
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mPharmaciesListView.setVisibility(show ? View.GONE : View.VISIBLE);
            mPharmaciesListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPharmaciesListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mPharmaciesListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    class GetPharmaciesTask extends AsyncTask<String,Void,Boolean> {
        private RequestQueue requestQueue;
        private JsonObjectRequest jsonObjectRequest;
        @Override
        protected Boolean doInBackground(String... params) {
            requestQueue = Volley.newRequestQueue(getContext());
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        int size = jsonArray.length();
                        for (int i = 0; i < size; i++)
                        {
                            JSONObject objectInArray = jsonArray.getJSONObject(i);
                            //JSONObject openObject = objectInArray.getJSONObject("opening_hours");
                            // "...and get their component and thier value."
                            pharmacyList.add(
                                    new Pharmacy(
                                            objectInArray.getString("name"),
                                            objectInArray.getString("vicinity"),
                                            objectInArray.getString("icon")
                                    ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
            while (!jsonObjectRequest.hasHadResponseDelivered())
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            recyclerPharmacyAdapter = new RecyclerPharmacyAdapter(pharmacyList,getContext());
            recyclerView.setAdapter(recyclerPharmacyAdapter);
        }
    }

}
