package com.example.qrcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ScanQR extends AppCompatActivity {


    String scannedData;
    EditText name,id;
    Button btn_scan;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        name=findViewById(R.id.Namescan);
        id=findViewById(R.id.idnummberscan);


        final Activity activity = this;
        btn_scan = (Button) findViewById(R.id.scan_btn);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setBeepEnabled(true);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();


            }
        });
    }

    public void addStudentData(){
        String sName=name.getText().toString();
        String sId=id.getText().toString();

        StringRequest stringRequest=new StringRequest
                (Request.Method.POST, "https://script.google.com/macros/s/AKfycbwIOsC4Km7k-XXrFgpTOx4iz3nlRA4szsXwCyGlcEnzX7HzZ_t8tSILc7hFoHGrrqY8Og/exec", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent=new Intent(getApplicationContext(),ScanQR.class);
                        startActivity(intent);

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params=new HashMap<>();
                params.put("action","sdata");
                params.put("Name",sName);
                params.put("Id",sId);

                return params;


            }
        };
        int socketTimeOut=50000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            scannedData = result.getContents();
            if (scannedData != null) {

                new SendRequest().execute();
                addStudentData();

            } else {


            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{

                //Enter script URL Here
                URL url = new URL("https://script.google.com/macros/s/AKfycbwIOsC4Km7k-XXrFgpTOx4iz3nlRA4szsXwCyGlcEnzX7HzZ_t8tSILc7hFoHGrrqY8Og/exec");

                JSONObject postDataParams = new JSONObject();

               // int i;
                //for(i=1;i<=70;i++)


                   // String usn = Integer.toString(i);

                //Passing scanned code as parameter

                postDataParams.put("hello",scannedData);


                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

        }

    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
