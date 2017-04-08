package edu.csulb.android.medicare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Suyash on 06-Apr-17.
 */

public class AddMedicationWithBarcodeActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private Button scan;
    private Button btnNext;
    private TextView text;
    private TextView product;
    private EditText NDCvalue;
    private Button buttonNDC;
    final Activity activity = this;
    private String code;
    private String ndc1;
    private String ndc2;
    private String ndc3;
    private String code1;
    private boolean barCodeUsed=false;
    String medicineName;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication_with_barcode);
        NDCvalue=(EditText) findViewById(R.id.textNDC);
        buttonNDC=(Button)findViewById(R.id.buttonNDC);
        scan = (Button) findViewById(R.id.buttonScan);
        text = (TextView) findViewById(R.id.text);
        product=(TextView)findViewById(R.id.productName);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
            }
        });
        progressBar = new ProgressBar(getApplicationContext());
        btnNext= (Button)findViewById(R.id.btnNext);
        btnNext.setEnabled(false);
        buttonNDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=NDCvalue.getText().toString();
                task = new Task();
                task.execute("Values");
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMedicationWithBarcodeActivity.this,AddMedicationWithManualActivity.class);
                intent.putExtra("Medicine Name",product.getText().toString());
                startActivity(intent);
                finish();
            }
        });

    }


    /*
        Initiates the barcode scanning intent.

     */
    public void scanBarcode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        intentIntegrator.setPrompt("Scan");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        //intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    /*
        On result, converts received barcode value to desirable format to perform
        the API call.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(result != null){
            if(result != null){
                if(result.getContents() == null){
                    Toast.makeText(this, "You have cancelled the scanning", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                    code1=result.getContents();
                    code=code1.subSequence(1,code1.length()-1).toString();
                    Log.e("Code",code);
                    changeFormats(code);
                    task = new Task();
                    task.execute("Values");
                  /*while(task.getStatus()!= AsyncTask.Status.FINISHED) {
                        try {
                            progressBar.
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/


                }
            } else {
                super.onActivityResult(requestCode, resultCode, intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
       /* if(product.getText().toString().matches("Product Name"))
        {
            try {
                Thread.sleep(5000);
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }*/

    }

    /*
        Converts barcode value into NDC value in three different formats :
        5-4-1, 5-3-2, 4-4-2.

     */
    private void changeFormats(String code) {
        //5-4-1
        ndc1=code.substring(0, 5) + "-" + code.substring(5,9)+ "-"+code.substring(9);
        Log.e("ndc1",ndc1);
        //5-3-2
        ndc2=code.substring(0, 5) + "-" + code.substring(5,8)+ "-"+code.substring(8);
        Log.e("ndc2",ndc2);
        //4-4-2
        ndc3=code.substring(0, 4) + "-" + code.substring(4,8)+ "-"+code.substring(8);
        Log.e("ndc3",ndc3);
        barCodeUsed=true;
    }

    class Task extends AsyncTask<String,Void,Boolean> {


        protected void displayDialog() {

            if(product.getText().toString().matches("No Product Found"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Get the layout inflater
                // Pass null as the parent view because its going in the
                // dialog layout
                builder.setTitle("Add Medication");
                builder.setMessage("Medication cannot be scanned. Please Enter Manually.");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent newIntent = new Intent(activity,AddMedicationWithManualActivity.class);
                        startActivity(newIntent);
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
                alertDialog.show();

            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Get the layout inflater
                // Pass null as the parent view because its going in the
                // dialog layout
                medicineName = product.getText().toString();
                builder.setTitle("Add Medication");
                builder.setMessage("Do you want to add "+medicineName+" ?");
                builder.setCancelable(true);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent newIntent = new Intent(activity,AddMedicationWithManualActivity.class);
                        newIntent.putExtra("Medicine Name",medicineName);
                        startActivity(newIntent);
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
                alertDialog.show();
            }


        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(!product.getText().toString().matches("No Product Found"))
                btnNext.setEnabled(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            return displayName();
        }

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error","Error happened!");
            }
        };

        Response.Listener res = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    String item = null;
                    JSONObject response1 = (JSONObject)response;
                    JSONArray item1 = response1.getJSONArray("data");
                    if(item1.length()==0) {
                        Log.e("error","no match for this NDC");
                    }
                    else
                    {
                        item=item1.getJSONObject(0).getString("title");
                        product.setText(item);
                        barCodeUsed=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.Listener resUPC = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    String item = null;
                    JSONArray response1 = (JSONArray) response;
                    if(response1.length()==0)
                    {
                        Log.e("error","no match for this NDC");
                    }else {
                        item = response1.getJSONObject(0).getString("Name");
                        product.setText(item);
                        barCodeUsed=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        /*
            Calls various different APIs to fetch the medication name from NDC value
            or barcode value.
         */
        private Boolean displayName() {

            Boolean isProductEmpty = true;

            //"http://api.upcdatabase.org/json/032c32681d84525464c7600732f02b4d/" + code;
            //String URL =  "http://www.barcodefinder.com/search?q="+code+"&format=json";
            //String URL="https://api.fda.gov/drug/label.json?search=openfda.product_ndc:"+code;
            if(barCodeUsed==true)
                code=ndc1;

            String URL ="https://dailymed.nlm.nih.gov/dailymed/services/v2/spls.json?ndc="+code;
            String URL2 ="https://dailymed.nlm.nih.gov/dailymed/services/v2/spls.json?ndc="+ndc2;
            String URL3 ="https://dailymed.nlm.nih.gov/dailymed/services/v2/spls.json?ndc="+ndc3;
            String URL4="http://www.barcodefinder.com/search?q="+code1+"&format=json\n";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest arrayRequest = new JsonObjectRequest(Request.Method.GET, URL, null,res,error);
            JsonObjectRequest arrayRequest2 = new JsonObjectRequest(Request.Method.GET, URL2, null,res,error);
            JsonObjectRequest arrayRequest3 = new JsonObjectRequest(Request.Method.GET, URL3, null,res,error);
            JsonArrayRequest arrayRequest4 = new JsonArrayRequest(Request.Method.GET,URL4,null,resUPC,error);

            queue.add(arrayRequest);
            if(barCodeUsed==true) {
                code=ndc2;
                queue.add(arrayRequest2);
            }
            if (barCodeUsed==true){
                code=ndc3;
                queue.add(arrayRequest3);
            }
            if(barCodeUsed==true){
                code=code1;
                queue.add(arrayRequest4);
            }
            else
            {
                isProductEmpty = false;
               // product.setText("No Product Found");
                Log.e("Error","Item Not found!");
            }


            return isProductEmpty;
        }
    }
}
