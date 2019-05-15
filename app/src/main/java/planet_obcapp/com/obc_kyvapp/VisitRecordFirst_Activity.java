package planet_obcapp.com.obc_kyvapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import planet_obcapp.com.obc_kyvapp.App_utils.GPSTracker;
import planet_obcapp.com.obc_kyvapp.App_utils.MonthYearPicker;
import planet_obcapp.com.obc_kyvapp.App_utils.util;


/**
 * Created by planet on 3/22/17.
 */

public class VisitRecordFirst_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String mLastUpdateTime,lat,longi;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    private Context context;
    private   Spinner sap_spn;
    private   JSONArray jArray;
    private   EditText curr_date,month_date,name,pf_no,pro_name,aadh_no,firm_name,ac_no,address,buss_act,con_no,remarks;
    private    String s_curr_date,s_month_date,sname,spf_no,spro_name,saadh_no,sfirm_name,sac_no,saddress,sbuss_act,scon_no,sremarks,ssap_spn;
    private MonthYearPicker myp;
    private String save_images,save_latti,save_longi,save_prolocation,save_propreitor,save_firm,con_zero,save_Img_sec;
    private String save_name,save_sap_spn,save_pro_name,save_firm_name,save_pf_no,save_curr_date,save_ac_no,save_address,save_buss_act,save_aadh_no,save_month_date,save_con_no,save_remarks,save_visit_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_record);
        static_text();
        All_view();
        init();
        dxter();
        getSaveData();
//        GPSTracker  gps = new GPSTracker(VisitRecordFirst_Activity.this);
//        if(gps.canGetLocation()){
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//        }else {
//            gps.showSettingsAlert();
//        }

        myp = new MonthYearPicker(this);
        myp.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                month_date.setText(myp.getSelectedMonthName() + "-" + myp.getSelectedYear());
            }
        }, null);

        Button logout_btn = (Button)findViewById(R.id.logout_btn) ;

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });
        ImageView back = (ImageView)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finish();

            }
        });

    }


    private void getSaveData() {
        Intent intent = getIntent();
        if (intent != null) {
            save_name = intent.getStringExtra("name");
            save_sap_spn = intent.getStringExtra("sap_spn");
            save_pro_name = intent.getStringExtra("pro_name");
            save_firm_name = intent.getStringExtra("firm_name");
            save_pf_no = intent.getStringExtra("Pf_No");
            save_curr_date = intent.getStringExtra("curr_date");
            save_ac_no = intent.getStringExtra("ac_no");
            save_address = intent.getStringExtra("address");
            save_buss_act = intent.getStringExtra("buss_act");
            save_aadh_no = intent.getStringExtra("aadh_no");
            save_month_date = intent.getStringExtra("month_date");
            save_con_no = intent.getStringExtra("con_no");
            save_remarks = intent.getStringExtra("remarks");

           // save_images = intent.getStringExtra("images");
            save_latti = intent.getStringExtra("latti");
            save_longi = intent.getStringExtra("longi");
            save_prolocation = intent.getStringExtra("pro_location");
            save_propreitor = intent.getStringExtra("propreitor");
            save_firm = intent.getStringExtra("firm");
            save_visit_no = intent.getStringExtra("Visit_no");
            //save_Img_sec = intent.getStringExtra("IMg_sec");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
      setSave_data();

        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private void setSave_data() {

        try{
            if(save_sap_spn.equalsIgnoreCase("Sh.")){
                sap_spn.setSelection(1);
                sap_spn.setEnabled(false);
            }
            else
                sap_spn.setSelection(2);
            sap_spn.setEnabled(false);

        }catch (Exception e){
        }
        if(save_name!=null){
            name.setText(save_name);
            name.setFocusable(false);
            name.setFocusableInTouchMode(false);
            name.setClickable(false);

        }
        if(save_pro_name!=null){
          pro_name.setText(save_pro_name);
            pro_name.setFocusable(false);
            pro_name.setFocusableInTouchMode(false);
            pro_name.setClickable(false);

        }
       if(save_firm_name!=null){
           firm_name.setText(save_firm_name);
           firm_name.setFocusable(false);
           firm_name.setFocusableInTouchMode(false);
           firm_name.setClickable(false);


       }
       if(save_pf_no!=null){
         pf_no.setText(save_pf_no);
           pf_no.setFocusable(false);
           pf_no.setFocusableInTouchMode(false);
           pf_no.setClickable(false);

       }
        if(save_curr_date!=null) {
            curr_date.setText(save_curr_date);
            curr_date.setFocusable(false);
            curr_date.setFocusableInTouchMode(false);
            curr_date.setClickable(false);

        }
        if(save_ac_no!=null){
            ac_no.setText(save_ac_no);
            ac_no.setFocusable(false);
            ac_no.setFocusableInTouchMode(false);
            ac_no.setClickable(false);

        }
        if(save_address!=null){
           address.setText(save_address);
            address.setFocusable(false);
            address.setFocusableInTouchMode(false);
            address.setClickable(false);
        }
       if(save_buss_act!=null){
           buss_act.setText(save_buss_act);
           buss_act.setFocusable(false);
           buss_act.setFocusableInTouchMode(false);
           buss_act.setClickable(false);

       }
       if(save_aadh_no!=null){
           aadh_no.setText(save_aadh_no);
           aadh_no.setFocusable(false);
           aadh_no.setFocusableInTouchMode(false);
           aadh_no.setClickable(false);

       }
        if(save_month_date!=null){
            month_date.setText(save_month_date);
            month_date.setEnabled(false);
            month_date.setClickable(false);
        }
        if(save_con_no!=null){
          con_no.setText(save_con_no);
          con_no.setFocusable(false);
          con_no.setFocusableInTouchMode(false);
          con_no.setClickable(false);

      }
       if(save_remarks!=null){
        remarks.setText(save_remarks);
           remarks.setFocusable(false);
           remarks.setFocusableInTouchMode(false);
           remarks.setClickable(false);



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
                                SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                                SharedPreferences.Editor ed = pref.edit();
                                ed.putBoolean("Deactivate", false);
                                ed.apply();
                            //    util.setPassword(context,"");
                                util.setusername(context,"");

                                Intent i = new Intent(VisitRecordFirst_Activity.this, Login_Activity.class);
                                startActivity(i);
                                finish();
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


    private void All_view() {
        Button btn = (Button)findViewById(R.id.Submit);

        curr_date = (EditText)findViewById(R.id.visit_date);

        month_date = (EditText)findViewById(R.id.date_time);
        name = (EditText)findViewById(R.id.vist_name);
        pf_no = (EditText)findViewById(R.id.pf_number);
        pro_name = (EditText)findViewById(R.id.proprietor_name);
        aadh_no = (EditText)findViewById(R.id.aadhar_no);
        firm_name = (EditText)findViewById(R.id.firm_name);
        ac_no = (EditText)findViewById(R.id.acc_no);
        address = (EditText)findViewById(R.id.address);
        buss_act = (EditText)findViewById(R.id.buss_nature);
        con_no = (EditText)findViewById(R.id.con_num);
        con_no.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
               con_zero = s.toString();
                if(con_zero.startsWith("0"))
                {
                    con_no.setError("Zero not allowed before contact number");
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });
        remarks = (EditText)findViewById(R.id.remarks);

        cuurent_Date();
        TextView sol_id = (TextView)findViewById(R.id.sol_idd);
        sol_id.setText( "Welcome to SOL ID " + util.getSOLID_value(this));
        sap_spn = (Spinner) findViewById(R.id.salutation_Spinner);
        sap_spn.setOnItemSelectedListener(this);

        salutation_spinner();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call_next_visitrecord();

                 // hit_Insert_api();
            }
        });
    }
    private boolean validateAllFields() {

      //  sac_no=ac_no.getText().toString();
        sremarks= remarks.getText().toString();

        sname=name.getText().toString();
        if (sname.trim().equalsIgnoreCase("")||!name.getText().toString().matches("[a-zA-Z ]+")) {
            name.setError("Please Enter Name  in Alphabets only");
            name.requestFocus();
            return false;
        }
        spf_no=pf_no.getText().toString();
        if (spf_no.trim().equalsIgnoreCase("") ||spf_no.length()!=6) {
            pf_no.setError("Please Enter 6 digits valid PF no.");
            pf_no.requestFocus();
            return false;
        }
        ssap_spn=sap_spn.getSelectedItem().toString();

        if (ssap_spn.contains("-Salutation-")) {
            Toast.makeText(getApplicationContext(), "Please select Salutation!", Toast.LENGTH_LONG).show();
            sap_spn.requestFocus();
            return false;
        }
        spro_name=pro_name.getText().toString();
        if (spro_name.trim().equalsIgnoreCase("")||!pro_name.getText().toString().matches("[a-zA-Z ]+")) {

            pro_name.setError("Please Enter Proprietor name in Alphabets only ");
            pro_name.requestFocus();
            return false;
        }
        saadh_no=aadh_no.getText().toString();
        if (saadh_no.trim().equalsIgnoreCase("")||saadh_no.length()!=12) {
            aadh_no.requestFocus();
            aadh_no.setError("Please Enter 12 digit  Aadhar no.");
            return false;
        }

        sfirm_name=firm_name.getText().toString();
        if (sfirm_name.trim().equalsIgnoreCase("")) {
            firm_name.requestFocus();
            firm_name.setError("Please Enter Firm name");
            return false;
        }
        s_curr_date = curr_date.getText().toString();
        if (s_curr_date.trim().equalsIgnoreCase("")) {
            curr_date.requestFocus();
            curr_date.setError("Please Enter Current Date");
            return false;
        }

        sac_no = ac_no.getText().toString();
        if (!sac_no.equalsIgnoreCase("") && sac_no.length()!=14) {
            ac_no.requestFocus();
            ac_no.setError("Please Enter 14 digits Account No.");
            return false;
        }

        saddress=address.getText().toString();
        if (saddress.trim().equalsIgnoreCase("")) {
            address.requestFocus();
            address.setError("Please Enter address");
            return false;
        }
        sbuss_act=buss_act.getText().toString();
        if (sbuss_act.trim().equalsIgnoreCase("")) {
            buss_act.requestFocus();
            buss_act.setError("Please Enter business activity");
            return false;
        }
        s_month_date = month_date.getText().toString();
        if (s_month_date.trim().equalsIgnoreCase("")) {

            month_date.setError("Please select month and year");
            return false;
        }
        scon_no=con_no.getText().toString();
        if (scon_no.trim().equalsIgnoreCase("")||scon_no.length()!=10||con_zero.startsWith("0")) {
            con_no.requestFocus();
            con_no.setError("Please Enter correct contact no.");
            return false;
        }

        return true;
    }



    private void call_next_visitrecord() {


        //if (gps.canGetLocation()){

            if (validateAllFields()) {
                Intent mainIntent = new Intent(VisitRecordFirst_Activity.this, VisitRecord_sec_Activity.class);
                mainIntent.putExtra("name", sname);
                mainIntent.putExtra("Pf_No", spf_no);
                mainIntent.putExtra("sap_spn", ssap_spn);
                mainIntent.putExtra("pro_name", spro_name);
                mainIntent.putExtra("aadh_no", saadh_no);
                mainIntent.putExtra("firm_name", sfirm_name);
                mainIntent.putExtra("curr_date", s_curr_date);
                mainIntent.putExtra("ac_no", sac_no);
                mainIntent.putExtra("address", saddress);
                mainIntent.putExtra("buss_act", sbuss_act);
                mainIntent.putExtra("month_date", s_month_date);
                mainIntent.putExtra("con_no", scon_no);
                mainIntent.putExtra("remarks", sremarks);
                mainIntent.putExtra("l_lat", lat);
                mainIntent.putExtra("L_longi", longi);
                mainIntent.putExtra("latti", save_latti);
                mainIntent.putExtra("longi", save_longi);
                mainIntent.putExtra("pro_location", save_prolocation);
                mainIntent.putExtra("propreitor", save_propreitor);
                mainIntent.putExtra("firm", save_firm);
                mainIntent.putExtra("visit_no", save_visit_no);
                startActivity(mainIntent);
            }


    }


    private void cuurent_Date() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        curr_date.setText(formattedDate);
    }


    private void static_text() {
        TextView nametxt = (TextView)findViewById(R.id.name);
        String text = "<font color=#000>Name of the visiting official</font> <font color=#cc0029>*</font>";
        nametxt.setText(Html.fromHtml(text));

        TextView pf = (TextView)findViewById(R.id.pf_no);
        String text1 = "<font color=#000>PF Number</font> <font color=#cc0029>*</font>";
        pf.setText(Html.fromHtml(text1));

        TextView name_pro = (TextView)findViewById(R.id.name_prii);
        String text2 = "<font color=#000>Name of propriteor</font> <font color=#cc0029>*</font>";
        name_pro.setText(Html.fromHtml(text2));


        TextView aa_na = (TextView)findViewById(R.id.aad_no);
        String text3 = "<font color=#000>Aadhaar Number</font> <font color=#cc0029>*</font>";
        aa_na.setText(Html.fromHtml(text3));


        TextView fr_name = (TextView)findViewById(R.id.fi_name);
        String text4 = "<font color=#000>Firm Name</font> <font color=#cc0029>*</font>";
        fr_name.setText(Html.fromHtml(text4));


        TextView ac_name = (TextView)findViewById(R.id.visit_txt);
        String text5 = "<font color=#000>Date Of Visit</font> <font color=#cc0029>*</font>";
        ac_name.setText(Html.fromHtml(text5));


//        TextView ac_no = (TextView)findViewById(R.id.ac_txt);
//        String text6 = "<font color=#000>Account Number</font> <font color=#cc0029>*</font>";
//        ac_no.setText(Html.fromHtml(text6));


        TextView aad = (TextView)findViewById(R.id.aad_txt);
        String text7 = "<font color=#000>Address</font> <font color=#cc0029>*</font>";
        aad.setText(Html.fromHtml(text7));


        TextView busss = (TextView)findViewById(R.id.buss_txt);
        String text8 = "<font color=#000>Nature of business activity</font> <font color=#cc0029>*</font>";
        busss.setText(Html.fromHtml(text8));


        TextView runnn = (TextView)findViewById(R.id.running_txt);
        String text9 = "<font color=#000>Running Since</font> <font color=#cc0029>*</font>";
        runnn.setText(Html.fromHtml(text9));


        TextView con_no = (TextView)findViewById(R.id.con_txt);
        String text10 = "<font color=#000>Contact Number</font> <font color=#cc0029>*</font>";
        con_no.setText(Html.fromHtml(text10));
    }

    private void salutation_spinner() {
        List categories = new ArrayList();
        categories.add("-Salutation-");
        categories.add("Sh.");
        categories.add("Smt.");


        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sap_spn.setAdapter(dataAdapter);

    }

    public void show(View view) {
        myp.show();
    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();



        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }
    public void onNothingSelected(AdapterView adapterView) {

    }
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void dxter() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    //    Log.i(TAG, "All location settings are satisfied.");

                       // Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                              //  Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        //"location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(VisitRecordFirst_Activity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    //Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                               // Log.e(TAG, errorMessage);

                                Toast.makeText(VisitRecordFirst_Activity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }
    private void updateLocationUI() {
        if (mCurrentLocation != null) {

         lat= String.valueOf(mCurrentLocation.getLatitude());
         longi= String.valueOf(mCurrentLocation.getLongitude());


            }

        // toggleButtons();
    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      //  Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        //  toggleButtons();
                    }
                });
    }



}
