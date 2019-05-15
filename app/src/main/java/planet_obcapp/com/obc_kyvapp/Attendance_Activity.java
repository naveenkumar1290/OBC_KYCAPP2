package planet_obcapp.com.obc_kyvapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import planet_obcapp.com.obc_kyvapp.App_utils.*;
import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.Sqlite.Databaseutill;
import planet_obcapp.com.obc_kyvapp.Sqlite.GetData;

/**
 * Created by Admin on 11/1/2017.
 */

public class Attendance_Activity extends AppCompatActivity  implements Animation.AnimationListener{
    private TextView location_name;
    private int  catchhandl;
    private String str_name,str_cluster_name,str_cmo_name,str_pf_no,str_lati,str_longi,str_location_name,str_plan_edt,result,formattedDate1;
    private Databaseutill db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);
        ImageView back = (ImageView) findViewById(R.id.back);
        Button logout_btn = (Button) findViewById(R.id.logout_btn);
        final TextView name=(TextView)findViewById(R.id.namee);
        final TextView cluster_name=(TextView)findViewById(R.id.cluster_name);
        final TextView cmo_name=(TextView)findViewById(R.id.cmo_name);
        final TextView pf_no=(TextView)findViewById(R.id.pf_no);
        final TextView lati=(TextView)findViewById(R.id.lati);
        final TextView longi=(TextView)findViewById(R.id.longi);
         location_name=(TextView)findViewById(R.id.location_name);
        TextView name_pro = (TextView) findViewById(R.id.plan);
        TextView date_time = (TextView) findViewById(R.id.date_time);

        final EditText plan_edt = (EditText) findViewById(R.id.plan_edt);
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_name= name.getText().toString();
                str_cluster_name= cluster_name.getText().toString();
                str_cmo_name= cmo_name.getText().toString();
                str_pf_no= pf_no.getText().toString();
                str_lati= lati.getText().toString();
                str_longi= longi.getText().toString();
                str_location_name= location_name.getText().toString();
                str_plan_edt= plan_edt.getText().toString();

//                if(str_name.equalsIgnoreCase("null")||str_name.equalsIgnoreCase("")){
//                    Toast.makeText(getApplicationContext(),
//                            "Name should not be null", Toast.LENGTH_LONG).show();
//                }
//                else if(str_cluster_name.equalsIgnoreCase("null")||str_cluster_name.equalsIgnoreCase("")) {
//                    Toast.makeText(getApplicationContext(),
//                            "Cluster Name should not be null", Toast.LENGTH_LONG).show();
//                }
//
//                else if(str_cmo_name.equalsIgnoreCase("null")||str_cmo_name.equalsIgnoreCase("")) {
//                    Toast.makeText(getApplicationContext(),
//                            "CMO Name should not be null", Toast.LENGTH_LONG).show();
//                }
//                else if(str_lati.equalsIgnoreCase("null")||str_lati.equalsIgnoreCase("")) {
//                    Toast.makeText(getApplicationContext(),
//                            "Latitude should not be null", Toast.LENGTH_LONG).show();
//                }
//                else if(str_longi.equalsIgnoreCase("null")||str_longi.equalsIgnoreCase("")) {
//                    Toast.makeText(getApplicationContext(),
//                            "Longitude should not be null", Toast.LENGTH_LONG).show();
//                }
//                else if(str_location_name.equalsIgnoreCase("null")||str_location_name.equalsIgnoreCase("")) {
//                    Toast.makeText(getApplicationContext(),
//                            "Location Name should not be null", Toast.LENGTH_LONG).show();
//                }
          if(str_plan_edt.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),
                            "Please Write plan for the Day", Toast.LENGTH_LONG).show();
                }else
                if (isConnectingToInternet() == true) {
                    new Attendace_APi().execute();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No Internet Connection...", Toast.LENGTH_LONG).show();
                }
            }
        });
        Animation_image();
        name.setText(util.getname_CRG(this));
        cluster_name.setText(util.getCluster_CRG(this));
        cmo_name.setText(util.getCMO_CRG(this));
        pf_no.setText(util.getusername_CRG(this));

        Calendar c1 = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c1.getTime());
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy,HH:mm");
        formattedDate1 = df1.format(c1.getTime());
        date_time.setText(formattedDate1);



        GPSTracker gps = new GPSTracker(Attendance_Activity.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String lattt = String.valueOf(latitude);
            String langg = String.valueOf(longitude);
            lati.setText(lattt);
            longi.setText(langg);
        }
        if (gps != null) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
        }

        String text2 = "<font color=#000>Plan For The Day</font> <font color=#cc0029>*</font>";
        name_pro.setText(Html.fromHtml(text2));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
//            Toast.makeText(getApplicationContext(),
//                    "" +locationAddress, Toast.LENGTH_LONG).show();
            location_name.setText(locationAddress);
        }
    }

    class Attendace_APi extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Attendance_Activity.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            attendance_Save();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(catchhandl==0){
                if (result.equalsIgnoreCase("Saved successfully")) {
                    Toast.makeText(Attendance_Activity.this,
                            "Attendance Submitted Successfully", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(Attendance_Activity.this, CRG_Visit_MAin.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Toast.makeText(Attendance_Activity.this,
                            "Please try again!", Toast.LENGTH_LONG).show();
                }
            }else{

                Toast.makeText(getApplicationContext(),
                        "Sorry, something went wrong there. Try again.", Toast.LENGTH_LONG).show();

            }
            if (Dialog.isShowing()) {
                Dialog.dismiss();
            }
        }
    }
    private void attendance_Save() {
        catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_SaveAttendance";
        final String METHOD_NAME = "CRG_SaveAttendance";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Name", str_name);
        request.addProperty("ClusterName",str_cluster_name);
        request.addProperty("CmoName",str_cmo_name);
        request.addProperty("Latitude", str_lati);
        request.addProperty("Longitude", str_longi);
        request.addProperty("Location", str_location_name);
        request.addProperty("Plan_for_the_day", str_plan_edt);
        request.addProperty("Date",formattedDate1);
        request.addProperty("PFNo", str_pf_no);




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);


        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        try {
            httpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;

//
//            Log.d("HTTP REQUEST ", httpTransport.requestDump);
//            Log.d("HTTP RESPONSE", httpTransport.responseDump);
//            String a = httpTransport.requestDump;
//            Object results = (Object) envelope.getResponse();
//            String   resultstring = results.toString();


            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            String recved = ks.toString();
            String u = recved.substring(recved.indexOf("["));
            JSONArray jsonArray = new JSONArray(u);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                result = jsonObject.getString("result");

            }

        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }
    }

    private void logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setMessage(getResources().getString(R.string.confirm_logout));
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences pref = getSharedPreferences("new1", MODE_PRIVATE);
                                SharedPreferences.Editor ed = pref.edit();
                                ed.putBoolean("Deactivate1", false);
                                ed.apply();
                            //    util.setPassword_CRG(Attendance_Activity.this, "");
                                util.setusername_CRG(Attendance_Activity.this, "");

                                Intent i = new Intent(Attendance_Activity.this, Login_Activity.class);
                                startActivity(i);
                                finish();
                                try{
                                    db = Databaseutill.getDBAdapterInstance(Attendance_Activity.this);
                                    GetData gett= new  GetData(db, getApplication());
                                    gett.Delete_record_CRG();

                                }catch (Exception e){
                                }

                            }
                        })
                .setNegativeButton(getResources().getString(R.string.nooo),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialogBuilder.create().show();
    }
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


    private boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) Attendance_Activity.this
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
