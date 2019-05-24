package planet_obcapp.com.obc_kyvapp;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.App_utils.util;
import planet_obcapp.com.obc_kyvapp.Sqlite.Databaseutill;
import planet_obcapp.com.obc_kyvapp.Sqlite.GetData;


/**
 * Created by planet on 3/15/17.
 */

public class Login_Activity extends AppCompatActivity implements Animation.AnimationListener, View.OnClickListener {

    public static final String FROM = "EDIT";
    private Context context;
    private Dialog dialog3;
    private EditText user_id, pass, sol_edt, otp_edt, user_email;
    private String user_name, password, receivedString, otp_volue, OTP_volue, email;
    private int catchhandl;
    private JSONArray jArray;
    private String inValid_username = "", inValid_otp = "", inValid_Email = "";
    private Databaseutill db;
    private String FName, CMO, Cluster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //  AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Animation_image();
        AllView();
    }

    private void AllView() {
        user_id = (EditText) findViewById(R.id.user_id);
        pass = (EditText) findViewById(R.id.user_pass);
        Button btn = (Button) findViewById(R.id.login_button);
        TextView forgot_pass = (TextView) findViewById(R.id.forg_pass);
        TextView faq = (TextView) findViewById(R.id.faq);
        faq.setOnClickListener(this);
        btn.setOnClickListener(this);
        forgot_pass.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button:
                Login_click();
                break;
            case R.id.forg_pass:
                Forgot_passpard();
                break;
            case R.id.submit_btn:
                hitForgotPasswordApi();
                break;
            case R.id.submit:
                Submit_SOL();
                break;
            case R.id.re_otp:
                if (isConnectingToInternet() == true) {
                    new Create_OTP_Async().execute();
                }
                break;
            case R.id.submit_otp:
                Submit_OTP();
                util.onKeyBoardDown(Login_Activity.this);
                break;

            case R.id.faq:
                Intent mainIntent = new Intent(Login_Activity.this, FAQ_Activity.class);
                startActivity(mainIntent);
                break;
            default:
                break;
        }
    }

    private void hitForgotPasswordApi() {

        email = user_email.getText().toString();

        if (isValidEmail(email)) {
            if (isConnectingToInternet() == true) {
                new ForgotPassword().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection....", Toast.LENGTH_LONG).show();
            }

//          Toast.makeText(getApplicationContext(),
//                  "Please Enter valid Email ID", Toast.LENGTH_LONG).show();
        } else {
            if (isConnectingToInternet() == true) {
                new ForgotPassword_CRG().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection....", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void ForgotPassword_Api_CRG() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_ForgetPasswordApi";
        final String METHOD_NAME = "CRG_ForgetPasswordApi";

        SoapObject request4 = new SoapObject(NAMESPACE, METHOD_NAME);
        request4.addProperty("Email", email);
        //  request4.addProperty("Username",util.getusername(this));


        SoapSerializationEnvelope envelope5 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope5.dotNet = true;
        envelope5.setOutputSoapObject(request4);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope5);
            KvmSerializable ks = (KvmSerializable) envelope5.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            receivedString = ks.toString();
        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }
        try {
            String Jsonstring = receivedString;


            if (!Jsonstring.contains("Incorrect")) {
                inValid_Email = "";
                String ne = Jsonstring.substring(Jsonstring.indexOf("["));
                String n5 = ne;
                jArray = new JSONArray(n5);
                for (int k = 0; k < (jArray.length()); k++) {

                    JSONObject json_obj = jArray.getJSONObject(k);
                    //  String  responsestatus = json_obj.getString("Status");

                }
            } else {
                inValid_Email = "0";

            }

        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }

    }

    private void ForgotPassword_Api() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "http://tempuri.org/ForgetPasswordApi";
        final String METHOD_NAME = "ForgetPasswordApi";

        SoapObject request4 = new SoapObject(NAMESPACE, METHOD_NAME);
        request4.addProperty("Email", email);
        //  request4.addProperty("Username",util.getusername(this));


        SoapSerializationEnvelope envelope5 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope5.dotNet = true;
        envelope5.setOutputSoapObject(request4);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope5);
            KvmSerializable ks = (KvmSerializable) envelope5.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            receivedString = ks.toString();


        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }
        try {
            String Jsonstring = receivedString;

            if (!Jsonstring.contains("Incorrect")) {
                inValid_Email = "";
                String ne = Jsonstring.substring(Jsonstring.indexOf("["));
                String n5 = ne;
                jArray = new JSONArray(n5);
                for (int k = 0; k < (jArray.length()); k++) {

                    JSONObject json_obj = jArray.getJSONObject(k);
                    String responsestatus = json_obj.getString("Status");

                }
            } else {
                inValid_Email = "0";

            }
        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }

    }

    private void Submit_OTP() {

        OTP_volue = otp_edt.getText().toString().trim();

        if (OTP_volue.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter OTP", Toast.LENGTH_LONG).show();
        } else if (OTP_volue.length() != 0) {
            //  dialog1.dismiss();
            if (isConnectingToInternet() == true) {
                new OTP_idBackground().execute();
            }
        } else {
            // dialog1.dismiss();
            if (isConnectingToInternet() == true) {
                new OTP_idBackground().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection....", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void Submit_SOL() {
        hitsol_id_api();
    }

    private void hitsol_id_api() {
        String sol_volue = sol_edt.getText().toString();

        if (sol_edt.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter SOL ID!", Toast.LENGTH_LONG).show();

        } else if (sol_edt.length() != 0) {
            dialog3.dismiss();
            if (isConnectingToInternet() == true) {
                new Create_OTP_Async().execute();
            }
        } else {
            if (isConnectingToInternet() == true) {
                new Create_OTP_Async().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection....", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void Forgot_passpard() {

        Dialog dialog2 = new Dialog(Login_Activity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.forgot_password);
        dialog2.setCanceledOnTouchOutside(false);
        user_email = (EditText) dialog2.findViewById(R.id.user_email);
        Button sub_btn = (Button) dialog2.findViewById(R.id.submit_btn);
        sub_btn.setOnClickListener(this);
        dialog2.show();

    }

    private void Login_click() {
        user_name = user_id.getText().toString();
        password = pass.getText().toString();
        if (user_name.startsWith("bm") || user_name.startsWith("crg")) {
            hitLogin_api();
        } else if (validateAllFields()) {
            if (isConnectingToInternet() == true) {
                new LoginAPI_CRG().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection....", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateAllFields() {
        user_name = user_id.getText().toString();
        if (user_name.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter User ID!", Toast.LENGTH_LONG).show();
            return false;
        }
        password = pass.getText().toString();
        if (password.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter password!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void hitLogin_api() {

        user_name = user_id.getText().toString();
        password = pass.getText().toString();


        if (validateAllFields()) {
            if (isConnectingToInternet() == true) {
                new performBackgroundTask1().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection....", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void sand_OTP() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "http://tempuri.org/CheckOTP";
        final String METHOD_NAME = "CheckOTP";

        SoapObject request1 = new SoapObject(NAMESPACE, METHOD_NAME);
        request1.addProperty("UserID", util.getUserId(this));
        request1.addProperty("OTP", OTP_volue);


        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope1.dotNet = true;
        envelope1.setOutputSoapObject(request1);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        androidHttpTransport.debug = true;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope1);


            Log.d("HTTP REQUEST ", androidHttpTransport.requestDump);
            Log.d("HTTP RESPONSE", androidHttpTransport.responseDump);
            String a = androidHttpTransport.requestDump;
            Object results = (Object) envelope1.getResponse();
            String resultstring = results.toString();


            KvmSerializable ks = (KvmSerializable) envelope1.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            receivedString = ks.toString();


        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }
        try {
            String Jsonstring = receivedString;
            if (!Jsonstring.contains("Incorrect")) {
                inValid_otp = "";
                String ne = Jsonstring.substring(Jsonstring.indexOf("["));
                String n1 = ne;
                jArray = new JSONArray(n1);
                for (int k = 0; k < (jArray.length()); k++) {
                }
            } else {

                inValid_otp = "0";
            }
        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }

    }

    private void open_otp_dilog() {
        Dialog dialog1 = new Dialog(Login_Activity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.otp);
        dialog1.setCanceledOnTouchOutside(false);
        otp_edt = (EditText) dialog1.findViewById(R.id.otp_id);
        // otp_edt.setText(otp_volue);

        TextView resend_otp = (TextView) dialog1.findViewById(R.id.re_otp);
        resend_otp.setOnClickListener(this);
        TextView submit1 = (TextView) dialog1.findViewById(R.id.submit_otp);
        submit1.setOnClickListener(this);
        dialog1.show();
    }

    private void Create_OTP() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "http://tempuri.org/CreateOTP";
        final String METHOD_NAME = "CreateOTP";

        SoapObject request2 = new SoapObject(NAMESPACE, METHOD_NAME);
        request2.addProperty("UserName", util.getusername(this));
        request2.addProperty("UserId", util.getUserId(this));
        request2.addProperty("Email", util.getEmail_id(this));


        SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope2.dotNet = true;
        envelope2.setOutputSoapObject(request2);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 600000);

        androidHttpTransport.debug = true;
        Log.e("-->CreteOTP_input", request2.toString());
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope2);

            Log.d("HTTP REQUEST ", androidHttpTransport.requestDump);
            Log.d("HTTP RESPONSE", androidHttpTransport.responseDump);
            String a = androidHttpTransport.requestDump;
            Object results = (Object) envelope2.getResponse();
            String resultstring = results.toString();

            Log.e("-->CreteOTP_output", resultstring);

            KvmSerializable ks2 = (KvmSerializable) envelope2.bodyIn;
            for (int j = 0; j < ks2.getPropertyCount(); j++) {
                ks2.getProperty(j);
            }
            receivedString = ks2.toString();
            String Jsonstring = receivedString;
            String ne2 = Jsonstring.substring(Jsonstring.indexOf("["));
            String n1 = ne2;
            jArray = new JSONArray(n1);
            for (int k = 0; k < (jArray.length()); k++) {

            }

        } catch (Exception e) {
            Log.e("-->CreteOTP_Exception", e.getMessage());
            catchhandl++;
            e.printStackTrace();
        }

    }

    private void open_sol_dilog() {
        dialog3 = new Dialog(Login_Activity.this);
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setContentView(R.layout.sol_id);
        sol_edt = (EditText) dialog3.findViewById(R.id.sol_id);
        sol_edt.setFocusableInTouchMode(false);
        sol_edt.setFocusable(false);
        sol_edt.clearFocus();
        String SOL_ID_String = user_name.substring(user_name.length() - 4);
        util.setSOLID_value(this, SOL_ID_String);
        sol_edt.setText(SOL_ID_String);

        dialog3.setCanceledOnTouchOutside(false);
        TextView submit = (TextView) dialog3.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        dialog3.show();
    }

    private void sand_logindata() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "http://tempuri.org/LoginOBC";
        final String METHOD_NAME = "LoginOBC";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("UserName", user_name);
        request.addProperty("Password", password);
        util.setusername(this, user_name);
        // util.setPassword(this, password);
      //  Log.e("-->LoginOBC input", request.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            Databaseutill db;
            GetData get;
            db = Databaseutill.getDBAdapterInstance(Login_Activity.this);
            get = new GetData(db, Login_Activity.this);
            // get.setUsernameAndPassword(user_name, password);


        } catch (Exception e) {

        }
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.d("HTTP REQUEST ", androidHttpTransport.requestDump);
            Log.d("HTTP RESPONSE", androidHttpTransport.responseDump);
            String a = androidHttpTransport.requestDump;
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();
            Log.e("-->LoginOBC output", resultstring);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            receivedString = ks.toString();

            String Jsonstring = receivedString;
            if (!Jsonstring.contains("valid_user")) {
                inValid_username = "";
                String ne = Jsonstring.substring(Jsonstring.indexOf("["));
                String n1 = ne;
                jArray = new JSONArray(n1);
                for (int k = 0; k < (jArray.length()); k++) {
                    JSONObject json_obj = jArray.getJSONObject(k);
                    String login_id = json_obj.getString("NU_LOGINID");
                    String email_id = json_obj.getString("VR_EMAIL");
                    String return_username = json_obj.getString("VR_USERNAME");
                    util.setUserId(this, login_id);
                    util.setEmail_id(this, email_id);
                    util.setReturnUser_name(this, return_username);
                }

            } else {
                inValid_username = "0";
                //wrong user
            }
        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }


    }

    public void internetAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login_Activity.this);
        alertDialogBuilder.setTitle("No Internet Connection");
        alertDialogBuilder
                .setMessage("Check Internet Connection!")
                .setCancelable(true)

                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //   Complain.this.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) Login_Activity.this
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

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        //close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    private void login() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_Login";
        final String METHOD_NAME = "CRG_Login";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        request.addProperty("PFNo", user_name);
        request.addProperty("Password", password);
        util.setusername_CRG(getApplicationContext(), user_name);
        // util.setPassword_CRG(getApplicationContext(), password);

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
            if (recved.contains("PFNumber")) {
                inValid_username = "";
                String u = recved.substring(recved.indexOf("["));
                JSONArray jsonArray = new JSONArray(u);
                if (recved.contains("PFNumber")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String PFNumber = jsonObject.getString("PFNumber");
                        FName = jsonObject.getString("FName");
                        CMO = jsonObject.getString("CMO");
                        Cluster = jsonObject.getString("Cluster");

                        util.setCMO_CRG(this, CMO);
                        util.setClusterNmae_CRG(this, Cluster);
                        util.setname_CRG(this, FName);
                        util.setpfno(this, PFNumber);

                    }
                }
            } else {
                inValid_username = "0";
                //wrong user
            }
        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
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
                db = Databaseutill.getDBAdapterInstance(Login_Activity.this);
                GetData gett = new GetData(db, getApplication());
                gett.Delete_record_CRG();

            } catch (Exception e) {
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String ID_unique = jsonObject.getString("ID_unique");

                util.setunid_CRG(this, ID_unique);

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
                    db = Databaseutill.getDBAdapterInstance(Login_Activity.this);
                    get = new GetData(db, Login_Activity.this);
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

    private void showPopUpAfterDelay() {

        final ProgressDialog progressDialog = new ProgressDialog(Login_Activity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.getMessage();
                }

                open_otp_dilog();
                sol_edt.setText("");
                util.showAlert(Login_Activity.this, "OTP is send to branch Email ID");

            }
        }, 10000);


    }

    public class ForgotPassword_CRG extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Login_Activity.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }
                if (catchhandl == 0) {
                    if (inValid_Email.equals("0")) {

                        user_email.setText("");
                        util.showAlert(Login_Activity.this, "Please Enter correct branch ID!");
                    } else {
                        user_email.setText("");
                        util.showAlert(Login_Activity.this, "Password sent succesfully to branch  ID");

                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Network Error...Please Try Again", Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Network Error....", Toast.LENGTH_LONG).show();
                Dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ForgotPassword_Api_CRG();
            return null;
        }
    }

    public class ForgotPassword extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Login_Activity.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }

                if (inValid_Email.equals("0")) {

                    user_email.setText("");
                    util.showAlert(Login_Activity.this, "Please Enter correct branch email ID!");
                } else {
                    user_email.setText("");
                    util.showAlert(Login_Activity.this, "Password sent succesfully to branch email ID");

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Network Error....", Toast.LENGTH_LONG).show();
                Dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            ForgotPassword_Api();
            return null;
        }
    }

    public class OTP_idBackground extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Login_Activity.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }


        @Override
        protected void onPostExecute(Void unused) {
            try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }

                if (inValid_otp.equals("0")) {
                    otp_edt.setText("");
                    util.showAlert(Login_Activity.this, "Please enter correct OTP");
                } else {
                    otp_edt.setText("");
                    SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("Deactivate", true);
                    ed.apply();
                    util.onKeyBoardDown(Login_Activity.this);
                    Intent Intent = new Intent(Login_Activity.this, Sol_ID_Activity.class);
                    startActivity(Intent);
                    finish();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Network Error....", Toast.LENGTH_LONG).show();
                Dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            sand_OTP();
            return null;
        }
    }

    public class Create_OTP_Async extends AsyncTask<Void, Void, Void> {
      //  private ProgressDialog Dialog = new ProgressDialog(Login_Activity.this);

        @Override
        protected void onPreExecute() {
           /* Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();*/
           showPopUpAfterDelay();
        }


        @Override
        protected void onPostExecute(Void unused) {
           /* try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }
            } catch (Exception e) {
            }
          if(catchhandl>0){
                util.showAlert(Login_Activity.this, getResources().getString(R.string.NetworkNoResponse));
            }else {
            open_otp_dilog();
            sol_edt.setText("");
            util.showAlert(Login_Activity.this, "OTP is send to branch Email ID");
             }
*/

        }

        @Override
        protected Void doInBackground(Void... params) {
            Create_OTP();
            return null;
        }
    }

    public class performBackgroundTask1 extends AsyncTask<Void, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(Login_Activity.this);


        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }


        @Override
        protected void onPostExecute(Void unused) {
            try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }
                if (catchhandl > 0) {
                    util.showAlert(Login_Activity.this, getResources().getString(R.string.NetworkNoResponse));
                    return;
                }

                if (inValid_username.equals("0")) {

                    util.showAlert(Login_Activity.this, "Please Enter valid userID and password");

                } else {
                    open_sol_dilog();
                }


                //  SuccessAlertDialog();
                user_id.setText("");
                pass.setText("");
             /*
                else
                {
                    internetAlertDialog();
                }*/
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Network Error....", Toast.LENGTH_LONG).show();
                Dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            sand_logindata();
            return null;
        }

    }

    class LoginAPI_CRG extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Login_Activity.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            login();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            if (isConnectingToInternet() == true) {
//                new Fetch_record_API().execute();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "No Internet Connection....", Toast.LENGTH_LONG).show();
//            }

            if (catchhandl == 0) {
                if (inValid_username.equals("0")) {

                    util.showAlert(Login_Activity.this, "Please Enter valid userID and password");
//                                          Toast.makeText(getApplicationContext(),
//                            "Please Enter valid userID and password", Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences pref = getSharedPreferences("new1", MODE_PRIVATE);
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("Deactivate1", true);
                    ed.apply();
                    Intent mainIntent = new Intent(Login_Activity.this, CRG_Visit_MAin.class);
                    mainIntent.putExtra("from", FROM);
//                    mainIntent.putExtra("cmo",CMO);
//                    mainIntent.putExtra("name", FName);
//                    mainIntent.putExtra("closter", Cluster);
                    startActivity(mainIntent);
                    finish();
                }

            } else {
                Toast.makeText(getApplicationContext(),
                        "Network Error...Please Try Again", Toast.LENGTH_LONG).show();

            }

            try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }
            } catch (Exception e) {
            }

        }
    }

    class Fetch_record_API extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Login_Activity.this);

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

            try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();

                }

            } catch (Exception e) {
            }

        }
    }

}





