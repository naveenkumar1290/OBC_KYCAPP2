package planet_obcapp.com.obc_kyvapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import planet_obcapp.com.obc_kyvapp.App_utils.*;
import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.Sqlite.Databaseutill;
import planet_obcapp.com.obc_kyvapp.Sqlite.GetData;


/**
 * Created by Admin on 7/3/2017.
 */

public class CRG_Visit_MAin extends AppCompatActivity implements Animation.AnimationListener,GoogleApiClient.OnConnectionFailedListener{
    Context context;
    private String from,CMO,FName,Cluster;
    private String old_p,new_p,con_p,result;
    private EditText old_pass,new_pass,con_pass;
    private int  catchhandl;
    private Dialog dialog;
    private  String New_record="";
    private Databaseutill db;


    private GoogleApiClient mGoogleApiClient;

    double latitude;
    double longitude;
    private Location mLastLocation;
    static final Integer LOCATION = 0x1;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result1;
    static final Integer GPS_SETTINGS = 0x7;
    private LocationManager locationManager;
    private String provider;


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                // .addApi(Places.GEO_DATA_API)       //for geo data adapter
                .addApi(LocationServices.API)      //for current loc(gps)
                .build();

        setContentView(R.layout.main_crg);

        TextView name=(TextView)findViewById(R.id.namee);
        TextView cluster_name=(TextView)findViewById(R.id.cluster_name);
        TextView cmo_name=(TextView)findViewById(R.id.cmo_name);


        Button btn = (Button) findViewById(R.id.visit_roc);
        Button btn1 = (Button) findViewById(R.id.visit_hist);
        Button change_password = (Button)findViewById(R.id.change_pass) ;
        Button attendance = (Button)findViewById(R.id.attendance) ;


        if (isConnectingToInternet() == true) {
            new checkVersionUpdate().execute();
        } else {
            Toast.makeText(getApplicationContext(),
                    "No Internet Connection....", Toast.LENGTH_LONG).show();
        }





        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

       LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //  Toast.makeText(getApplicationContext(), "onLocationChanged", Toast.LENGTH_SHORT).show();
            }

          /*  @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Toast.makeText(getApplicationContext(), "onStatusChanged", Toast.LENGTH_SHORT).show();
            }
*/
           /* @Override
            public void onProviderEnabled(String provider) {
                // Toast.makeText(getApplicationContext(), "onProviderEnabled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Toast.makeText(getApplicationContext(), "onProviderDisabled", Toast.LENGTH_SHORT).show();
            }*/
        };
//        LocationListener locationListener=new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//            }
//



//        try {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, (android.location.LocationListener) locationListener);
//            //   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        } catch (SecurityException e) {
//        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);

        } else {
          //  new AsyncGetLocation().execute();

        }





        Animation_image();
//        GPSTracker  gps = new GPSTracker(CRG_Visit_MAin.this);
//        if(gps.canGetLocation()){
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//        }else {
//            gps.showSettingsAlert();
//        }

        Intent intent = getIntent();
        if (intent != null) {
           from = intent.getStringExtra("from");
//            CMO = intent.getStringExtra("cmo");
//            FName = intent.getStringExtra("name");
//            Cluster = intent.getStringExtra("closter");

        }
        name.setText(util.getname_CRG(this));
        cluster_name.setText(util.getCluster_CRG(this));
        cmo_name.setText(util.getCMO_CRG(this));
        Button logout_btn = (Button)findViewById(R.id.logout_btn) ;
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                New_record="record_first";
                util.setSECIma(getApplication(),"");
                util.setFirstIma(getApplication(),"");
                Intent mainIntent = new Intent(CRG_Visit_MAin.this,Visit_CRG.class);
                mainIntent.putExtra("New",New_record);
                startActivity(mainIntent);

            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(CRG_Visit_MAin.this,Visit_Record_Histry_CRG.class);
                mainIntent.putExtra("from",from);
                startActivity(mainIntent);
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                change_Password();
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(CRG_Visit_MAin.this,Attendance_Activity.class);
                startActivity(mainIntent);
            }
        });

    }
    /******************************Methods for gps current location***********************************************/
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(CRG_Visit_MAin.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CRG_Visit_MAin.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(CRG_Visit_MAin.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(CRG_Visit_MAin.this, new String[]{permission}, requestCode);
            }
        } else {
            //  Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            askForGPS();
        }}


    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result1 = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result1.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result1) {
                final Status status = result1.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // NO need to show the dialog;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //  GPS turned off, Show the user a dialog
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        try {
                            status.startResolutionForResult(CRG_Visit_MAin.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are unavailable so not possible to show any dialog now
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_SETTINGS) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
                /*nks*/
               // new AsyncGetLocation().execute();
                /*nks*/
            } else {
                //close  all activity
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
   }


    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public void getAddress() {

        Address locationAddress = getAddress(latitude, longitude);

        if (locationAddress != null) {
            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            String currentLocation;

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;

                // tvEmpty.setVisibility(View.GONE);
             //   mAutocompleteView.setText(currentLocation);
                //tvAddress.setVisibility(View.VISIBLE);

                //if(!btnProceed.isEnabled())
                //  btnProceed.setEnabled(true);


            }

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class AsyncGetLocation extends AsyncTask<String, Void, String> {
        ProgressDialog progressDoalog;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDoalog = new ProgressDialog(CRG_Visit_MAin.this);
            progressDoalog.setMessage("Kindly Wait");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.setCancelable(true);
            progressDoalog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //Wait 10 seconds to see if we can get a location from either network or GPS, otherwise stop
            Long t = Calendar.getInstance().getTimeInMillis();
            try {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
            catch (SecurityException e){
                e.getMessage();
            }
            while (mLastLocation==null && Calendar.getInstance().getTimeInMillis() - t < 15000) {
                try {
                    // Thread.sleep(1000);
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            try {
                //    mLastLocation = locationManager.getLastKnownLocation(provider);
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                }
                else
                {
                    //Couldn't find location, do something like show an alert dialog
                }

            } catch (SecurityException e) {
                e.printStackTrace();
            }

            if(progressDoalog.isShowing()){
                progressDoalog.dismiss();
            }
        }


    }




    private void change_Password() {
        dialog = new Dialog(CRG_Visit_MAin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.crg_change_pass);
        ImageView close=(ImageView)dialog.findViewById(R.id.close);
         old_pass = (EditText)dialog.findViewById(R.id.old_pass);
         new_pass = (EditText)dialog.findViewById(R.id.new_pass);
         con_pass = (EditText)dialog.findViewById(R.id.con_pass);
        Button submit = (Button)dialog.findViewById(R.id.submit_btn);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAllFields()) {
                    if (isConnectingToInternet() == true) {
                        new Change_Password().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No Internet Connection....", Toast.LENGTH_LONG).show();
                    }
                }



            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        GPSTracker  gps = new GPSTracker(CRG_Visit_MAin.this);
//        if(gps.canGetLocation()){
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//        }else {
//            gps.showSettingsAlert();
//        }
//    }

//    @Override
//    public void onRestart() {
//        super.onRestart();
//        GPSTracker  gps = new GPSTracker(CRG_Visit_MAin.this);
//        if(gps.canGetLocation()){
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//        }else {
//            gps.showSettingsAlert();
//        }
//    }

    class Change_Password extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(CRG_Visit_MAin.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            change();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(catchhandl==0){
                if (result.equalsIgnoreCase("1")) {
                    dialog.dismiss();

                    SharedPreferences pref = getSharedPreferences("new1", MODE_PRIVATE);
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("Deactivate1", false);
                    ed.apply();
                    //util.setPassword_CRG(context,"");
                    util.setusername_CRG(context,"");

                    Intent i = new Intent(CRG_Visit_MAin.this, Login_Activity.class);
                    startActivity(i);
                    finish();

                    Toast.makeText(getApplicationContext(),
                            "Password Change Successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(),
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
    private void change() {
        catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_ChangePassword";
        final String METHOD_NAME = "CRG_ChangePassword";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("pfnumber",util.getpfno(this));
        request.addProperty("oldpwd",old_p);
        request.addProperty("new_pwd",new_p);

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


    private boolean validateAllFields() {

        old_p= old_pass.getText().toString();
        new_p = new_pass.getText().toString();
        con_p = con_pass.getText().toString();

        if (old_p.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Old Password", Toast.LENGTH_LONG).show();
            return false;
        }

        if (new_p.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Enter New Password", Toast.LENGTH_LONG).show();
            return false;
        }
        if (con_p.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Confirm Password", Toast.LENGTH_LONG).show();
            return false;
        }

       /* if (!old_p.equals(util.getPassword_CRG(this))) {
            Toast.makeText(getApplicationContext(), "Please Enter Old Password Correctly!", Toast.LENGTH_LONG).show();
            return false;
        }*/

        if (!new_p.equals(con_p)) {
            Toast.makeText(getApplicationContext(), "New Password and Confirm Password Do Not Match", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
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
                           //     util.setPassword_CRG(context,"");
                                util.setusername_CRG(context,"");

                                Intent i = new Intent(CRG_Visit_MAin.this, Login_Activity.class);
                                startActivity(i);
                                finish();

                                try{
                                    db = Databaseutill.getDBAdapterInstance(CRG_Visit_MAin.this);
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

    class Fetch_record_API extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(CRG_Visit_MAin.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            fetch_record();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try{
                if (Dialog.isShowing()) {
                    Dialog.dismiss();

                }

            }catch (Exception e){}

        }
    }

    private void fetch_record() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_FetchRecord";
        final String METHOD_NAME = "CRG_FetchRecord";
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


            try {
                db = Databaseutill.getDBAdapterInstance(CRG_Visit_MAin.this);
                GetData gett = new GetData(db, getApplication());
                gett.Delete_record_CRG();

            } catch (Exception e) {
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String ID_unique = jsonObject.getString("ID_unique");

                util.setunid_CRG(this,ID_unique);

                String date_visit = jsonObject.getString("date_visit");
                String name_inst = jsonObject.getString("name_inst");
                String contact_person = jsonObject.getString("contact_person");
                String contact_detail = jsonObject.getString("contact_detail");
                String lead_type = jsonObject.getString("lead_type");
                String status_lead = jsonObject.getString("status_lead");
                String No_of_Accounts = jsonObject.getString("No_of_Accounts");
                String image1 = jsonObject.getString("image1");
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");
                String location_name = jsonObject.getString("location_name");

                String SolID = jsonObject.getString("SolID");
                String AccountNumber = jsonObject.getString("AccountNumber");
                String RefNo = jsonObject.getString("RefNo");
                String createdate = jsonObject.getString("createdate");
                String image2 = jsonObject.getString("image2");
                String remark = jsonObject.getString("remark");
                String date_Revisit = jsonObject.getString("date_Revisit");
                String date_close = jsonObject.getString("date_close");
                String date_conversion = jsonObject.getString("date_conversion");
                String status = jsonObject.getString("status");
                String PFNumber = jsonObject.getString("PFNumber");

                String type_lead_ddl = jsonObject.getString("type_lead_ddl");
                String unique_visit_no = jsonObject.getString("unique_visit_no");
                String revisit_time = jsonObject.getString("revisit_time");


                try {
                    Databaseutill db;
                    GetData get;
                    db = Databaseutill.getDBAdapterInstance(CRG_Visit_MAin.this);
                    get = new GetData(db, CRG_Visit_MAin.this);
                    get.setUSERRecordCRG(ID_unique, date_visit, name_inst, contact_person, contact_detail, lead_type, status_lead, date_Revisit, remark, date_close, date_conversion, SolID, No_of_Accounts, AccountNumber, image1, image2, latitude, longitude, location_name, type_lead_ddl, unique_visit_no, revisit_time);
                    ArrayList<HashMap<String, String>> listData = get.getUSERRecordCRG();

                } catch (Exception e) {

                }
            }

        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }


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

    private class checkVersionUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(CRG_Visit_MAin.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            try {
                progressDialog.show();
            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String val = "0.0";
            final String NAMESPACE = "https://tempuri.org/" + "";
            final String URL = "https://www.exhibitpower2.com/WebService/techlogin_service.asmx";
            final String SOAP_ACTION ="https://tempuri.org/GetVersion";
            final String METHOD_NAME = "GetVersion";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            try {

                final String appPackageName = getPackageName();
                String address = "https://play.google.com/store/apps/details?id=" + appPackageName;
                // String address = "https://itunes.apple.com/in/app/reliance-mutualfund/id691879143?mt=8";


                request.addProperty("address", address);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE httpTransport = new HttpTransportSE(URL);
                httpTransport.call(SOAP_ACTION, envelope);
                KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
                for (int j = 0; j < ks.getPropertyCount(); j++) {
                    ks.getProperty(j);
                }
                String recved = ks.toString();

                if (recved.contains("GetVersionResult")) {
                    val = recved.substring(recved.indexOf("=") + 1, recved.indexOf(";"));
                }

                //  val = "1.1";//test
            } catch (Exception e) {
                e.getMessage();
            }
            return val;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            String ver = result;
            super.onPostExecute(result);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {

                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            } catch (Exception e) {
                e.getMessage();
            }

            try {
                PackageManager manager = CRG_Visit_MAin.this.getPackageManager();
                PackageInfo info = manager.getPackageInfo(CRG_Visit_MAin.this.getPackageName(), 0);
                String versionName = info.versionName;
                String nversionName = ver;
                if (nversionName == null) {
                    nversionName = "0";
                }
                Double retuenvl = Double.parseDouble(nversionName);
                if (Double.parseDouble(versionName) < retuenvl) {
                    // if (true) {
                    try {
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CRG_Visit_MAin.this, SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setTitleText("New Update Available");
                        sweetAlertDialog.setContentText("Please update to the latest version!");
                        sweetAlertDialog.setConfirmText("Update");
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setCanceledOnTouchOutside(false);
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                /**/
                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                                finish();
                                /**/
                            }
                        })
                                .show();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            } catch (Exception err) {
                err.getMessage();
            }

             /*   if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
                    new async_RunIncompleteTask().execute();
                }*/
            //  SavedTimeSheetDialog();

        }
    }





    private boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) CRG_Visit_MAin.this
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
