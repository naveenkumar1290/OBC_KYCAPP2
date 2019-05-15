package planet_obcapp.com.obc_kyvapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

import planet_obcapp.com.obc_kyvapp.Adapter.View_Remarks_Adapter;
import planet_obcapp.com.obc_kyvapp.Adapter.Visit_Histry_CRG_Adapter;
import planet_obcapp.com.obc_kyvapp.App_utils.*;
import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.Sqlite.Databaseutill;
import planet_obcapp.com.obc_kyvapp.Sqlite.GetData;

/**
 * Created by Admin on 7/31/2017.
 */

public class View_Remark_CRG extends AppCompatActivity implements Animation.AnimationListener{
    ArrayList<HashMap<String,String>> listData;
    View_Remarks_Adapter proadb;
    ListView data_list;
    private TextView no_found;
    private Databaseutill db;
    String UNIID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_remark);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Animation_image();

        Intent intent = getIntent();
        if (intent != null) {

            UNIID = intent.getStringExtra("UNIID");
        }
        //    if (from != null && from.equalsIgnoreCase(FROM)) {

        try{
            data_list = (ListView) findViewById(R.id.lll);
            Databaseutill db;
            GetData get;
            db = Databaseutill.getDBAdapterInstance(getApplication());
            get = new GetData(db, getApplication());
            listData=get.getUSERCRGOPEN();
            proadb = new View_Remarks_Adapter(View_Remark_CRG.this,listData,View_Remark_CRG.this);
            data_list.setAdapter(proadb);


            if(listData.size()==0){

                Toast.makeText(getApplicationContext(), "No Data Found for this Record", Toast.LENGTH_LONG).show();

            }

        }catch ( Exception e){
        }



        if(isConnectingToInternet()==true) {
            new FetchUpdate_record_API().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "No Internet Connection....", Toast.LENGTH_LONG).show();
        }
        // }




    }

    class FetchUpdate_record_API extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(View_Remark_CRG.this);

        @Override
        protected void onPreExecute()   {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            fetchUpdate_record();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                data_list = (ListView) findViewById(R.id.lll);
                Databaseutill db;
                GetData get;
                db = Databaseutill.getDBAdapterInstance(getApplication());
                get = new GetData(db, getApplication());
                listData=get.getUSERCRGOPEN();
                proadb = new View_Remarks_Adapter(View_Remark_CRG.this,listData,View_Remark_CRG.this);
                data_list.setAdapter(proadb);

                if(listData.size()==0){

                    Toast.makeText(getApplicationContext(), "No Data Found for this Record", Toast.LENGTH_LONG).show();

                }

            }catch ( Exception e){
            }




            if (Dialog.isShowing()) {
                Dialog.dismiss();

            }
        }
    }

    private void fetchUpdate_record() {

        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_fetchUpdateRecord";
        final String METHOD_NAME = "CRG_fetchUpdateRecord";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("PFNo", util.getpfno(this));


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }

            String recved = ks.toString();
            String u = recved.substring(recved.indexOf("["));
            JSONArray jsonArray = new JSONArray(u);


            try{
                db = Databaseutill.getDBAdapterInstance(View_Remark_CRG.this);
                GetData gett= new  GetData(db, getApplication());
                gett.Delete_record_CRGOPEN();

            }catch (Exception e){
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String date_visit = jsonObject.getString("date_visit");
                String date_revisit = jsonObject.getString("date_revisit");

                String remarks = jsonObject.getString("remarks");
                String status_lead = jsonObject.getString("status_lead");

                String PF_Number = jsonObject.getString("PF_Number");
                String main_ref = jsonObject.getString("main_ref");


                try {

                    if(UNIID.equals(main_ref)) {
                        Databaseutill db;
                        GetData get;
                        db = Databaseutill.getDBAdapterInstance(View_Remark_CRG.this);
                        get = new GetData(db, View_Remark_CRG.this);
                        get.setUSERCRGOPEN(date_visit, date_revisit, remarks, status_lead, PF_Number);
                        ArrayList<HashMap<String, String>> listData = get.getUSERCRGOPEN();
                    }

                } catch (Exception e) {

                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;}
        return super.onOptionsItemSelected(item);}



    private void Animation_image() {
        Animation animation_blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        animation_blink.setAnimationListener(this);
        ImageButton button_anim = (ImageButton) findViewById(R.id.imageButton);
        button_anim.startAnimation(animation_blink);
        double r = 5 / 400.0;
        double a = 1 + r;
        a = Math.pow(a, 3);
        double l = 10000 * a - 1;
        double m = Math.pow(1 + r, -1 / 3);
        double n = l / m;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }
    @Override
    public void onAnimationEnd(Animation animation) {

    }
    @Override
    public void onAnimationRepeat(Animation animation) {
    }
    private boolean isConnectingToInternet()
    {
        ConnectivityManager connectivity = (ConnectivityManager)View_Remark_CRG.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
