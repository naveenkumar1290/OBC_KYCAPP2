package planet_obcapp.com.obc_kyvapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import planet_obcapp.com.obc_kyvapp.Adapter.VisitHistoryAdapter;
import planet_obcapp.com.obc_kyvapp.App_utils.*;
import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.Sqlite.Databaseutill;
import planet_obcapp.com.obc_kyvapp.Sqlite.GetData;

/**
 * Created by planet on 4/6/17.
 */

public class VisitRecord_Histary  extends AppCompatActivity{

    Context context;
    private int catchhandl;
    private String receivedString,date,date_change,formattedDate;
    private Databaseutill db;
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_history);


try{
         ListView  data_list = (ListView) findViewById(R.id.lll);
         Databaseutill db;
         GetData get;
         db = Databaseutill.getDBAdapterInstance(getApplication());
         get = new GetData(db, getApplication());
         ArrayList<HashMap<String,String>> listData=get.getUSERRecord();
         VisitHistoryAdapter  proadb = new VisitHistoryAdapter(VisitRecord_Histary.this,listData);
         data_list.setAdapter(proadb);

}catch ( Exception e){
}

        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("ddMMMyyyy");
        formattedDate = df.format(c.getTime());


        if (!haveNetworkConnection()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection Found", Toast.LENGTH_SHORT).show();
        } else {
            new GetUser_Data().execute();
        }
        TextView show_date =  (TextView)findViewById(R.id.show_date);
        show_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(VisitRecord_Histary.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                date= dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year;



                                 date_change= String.valueOf(date);
                                SimpleDateFormat spf=new SimpleDateFormat("d/M/yyyy");
                                Date newDate= null;
                                try {
                                    newDate = spf.parse(date_change);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                spf= new SimpleDateFormat("ddMMMyyyy");
                                try{
                                    date_change = spf.format(newDate);
                                  //  Toast.makeText(getApplicationContext(), "Date "+ date_change ,Toast.LENGTH_SHORT).show();
                                }catch (Exception e){}


                                if (!haveNetworkConnection()) {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection Found", Toast.LENGTH_SHORT).show();
                                } else {
                                    new User_Data_from_Date().execute();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }




        });

        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    class GetUser_Data extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(VisitRecord_Histary.this);
        @Override
        protected void onPreExecute()
        {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {


            User_data();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }


                ListView   firm_list = (ListView) findViewById(R.id.lll);
                Databaseutill db;
                GetData get;
                db = Databaseutill.getDBAdapterInstance(getApplication());
                get = new GetData(db, getApplication());
                ArrayList<HashMap<String,String>> listData=get.getUSERRecord();
                VisitHistoryAdapter  proadb = new VisitHistoryAdapter(VisitRecord_Histary.this,listData);
                firm_list.setAdapter(proadb);

                if(listData.size()==0){

                    Toast.makeText(getApplicationContext(), "No Data Found for this Date", Toast.LENGTH_LONG).show();

                }

            }catch(Exception e){}
        }
    }

    private void User_data() {
        catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "http://tempuri.org/FatchUserDetails";
        final String METHOD_NAME = "FatchUserDetails";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("userID",util.getusername(this));
        request.addProperty("date",formattedDate);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug=true;
        try
        {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable)envelope.bodyIn;

            Log.d("HTTP REQUEST ", androidHttpTransport.requestDump);
            Log.d("HTTP RESPONSE", androidHttpTransport.responseDump);
            String a = androidHttpTransport.requestDump;
            Object results = (Object) envelope.getResponse();
            String   resultstring = results.toString();


            for(int j=0;j<ks.getPropertyCount();j++)
            {
                ks.getProperty(j);
            }
            receivedString = ks.toString();

        }
        catch (Exception e)
        {
            catchhandl++;
            e.printStackTrace();
        }
        try
        {

            String ne = receivedString.substring(receivedString.indexOf("=")+1);
            try{
            ne  =ne.replaceAll("\n","").replaceAll(" ","");
            }catch (Exception e){

            }
            JSONObject jObject = new JSONObject(ne);
            JSONArray table = jObject.getJSONArray("Table");


     try{
            db = Databaseutill.getDBAdapterInstance(VisitRecord_Histary.this);
            GetData gett= new  GetData(db, getApplication());
            gett.Delete_record();

    }catch (Exception e){
     }
     for(int i=0; i < table.length(); i++){
                JSONObject jsonObject = table.getJSONObject(i);

                String DateofVisit1  = jsonObject.getString("DateofVisit1");
                String id   = jsonObject.getString("ID");
                String  name_visiter  =jsonObject.getString("NameVisitingOfficial");
                String  pf_no  =jsonObject.getString("PFvisitingofficial");
                String  salutation  =jsonObject.getString("Salutation");
                String  name_pro  =jsonObject.getString("NameProprietor");
                String  addhar  =jsonObject.getString("AadhaarNumber");
                String  firm_name  =jsonObject.getString("NameFirm");
                String  visit_date  =jsonObject.getString("DateofVisit");
                String  acc_no  =jsonObject.getString("AccountNumber");
                String  address  =jsonObject.getString("Address");
                String  bussi_activity  =jsonObject.getString("NatureofBusinessActivity");
                String  runing_since  =jsonObject.getString("RunningSince");
                String  con_no  =jsonObject.getString("ContactNo");
                String  remark  =jsonObject.getString("Remarks");
                String  pro_id  =jsonObject.getString("KYCProprietor_ID");
                String  firm_id  =jsonObject.getString("KYCFirm_ID");
                String  user_id  =jsonObject.getString("UserID");
                String  longi  =jsonObject.getString("Longitude");
                String  latitude  =jsonObject.getString("Latitude");
                String  images  =jsonObject.getString("Images");
                String  pro_location  =jsonObject.getString("ProprietorLocation");
                String  unique_no  =jsonObject.getString("UniqueNo");
                String  CheckUnique  =jsonObject.getString("CheckUnique");
                String  createddate  =jsonObject.getString("createddate");
                String  Image_sec  =jsonObject.getString("Image2");

                try
                {
                    Databaseutill db;
                    GetData get;
                    db = Databaseutill.getDBAdapterInstance(VisitRecord_Histary.this);
                    get = new GetData(db, VisitRecord_Histary.this);
                    get.setUSERRecord(DateofVisit1,id,name_visiter,pf_no,salutation,name_pro,addhar,firm_name,visit_date,acc_no,address,bussi_activity,runing_since,con_no,remark,pro_id,firm_id,user_id,longi,latitude,images ,pro_location,unique_no,CheckUnique,createddate,Image_sec);
                    ArrayList<HashMap<String,String>> listData=get.getUSERRecord();
                }
                catch (Exception e)
                {

                }
            }
        }
        catch(Exception e)
        {
            catchhandl++;
            e.printStackTrace();
        }

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    class User_Data_from_Date extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(VisitRecord_Histary.this);
        @Override
        protected void onPreExecute()
        {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {


            User_data2();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }


                ListView   firm_list = (ListView) findViewById(R.id.lll);
                Databaseutill db;
                GetData get;
                db = Databaseutill.getDBAdapterInstance(getApplication());
                get = new GetData(db, getApplication());
                ArrayList<HashMap<String,String>> listData=get.getUSERRecord();
                VisitHistoryAdapter  proadb = new VisitHistoryAdapter(VisitRecord_Histary.this,listData);
                firm_list.setAdapter(proadb);

                if(listData.size()==0){

                    Toast.makeText(getApplicationContext(), "No Data Found for this Date", Toast.LENGTH_LONG).show();

                }

            }catch(Exception e){}
        }
    }

    private void User_data2() {
        catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "http://tempuri.org/FatchUserDetails";
        final String METHOD_NAME = "FatchUserDetails";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("userID",util.getusername(this));
        request.addProperty("date",date_change);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try
        {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable)envelope.bodyIn;
            for(int j=0;j<ks.getPropertyCount();j++)
            {
                ks.getProperty(j);
            }
            receivedString = ks.toString();

        }
        catch (Exception e)
        {
            catchhandl++;
            e.printStackTrace();
        }
        try
        {

            String ne = receivedString.substring(receivedString.indexOf("=")+1);
            try{
                ne  =ne.replaceAll("\n","").replaceAll(" ","");
            }catch (Exception e){

            }
            JSONObject jObject = new JSONObject(ne);
            JSONArray table = jObject.getJSONArray("Table");


            try{
                db = Databaseutill.getDBAdapterInstance(VisitRecord_Histary.this);
                GetData gett= new  GetData(db, getApplication());
                gett.Delete_record();

            }catch (Exception e){

            }



            for(int i=0; i < table.length(); i++){
                JSONObject jsonObject = table.getJSONObject(i);

                String DateofVisit1   = jsonObject.getString("DateofVisit1");
                String id   = jsonObject.getString("ID");
                String  name_visiter  =jsonObject.getString("NameVisitingOfficial");
                String  pf_no  =jsonObject.getString("PFvisitingofficial");
                String  salutation  =jsonObject.getString("Salutation");
                String  name_pro  =jsonObject.getString("NameProprietor");
                String  addhar  =jsonObject.getString("AadhaarNumber");
                String  firm_name  =jsonObject.getString("NameFirm");
                String  visit_date  =jsonObject.getString("DateofVisit");
                String  acc_no  =jsonObject.getString("AccountNumber");
                String  address  =jsonObject.getString("Address");
                String  bussi_activity  =jsonObject.getString("NatureofBusinessActivity");
                String  runing_since  =jsonObject.getString("RunningSince");
                String  con_no  =jsonObject.getString("ContactNo");
                String  remark  =jsonObject.getString("Remarks");
                String  pro_id  =jsonObject.getString("KYCProprietor_ID");
                String  firm_id  =jsonObject.getString("KYCFirm_ID");
                String  user_id  =jsonObject.getString("UserID");
                String  longi  =jsonObject.getString("Longitude");
                String  latitude  =jsonObject.getString("Latitude");
                String  images  =jsonObject.getString("Images");
                String  pro_location  =jsonObject.getString("ProprietorLocation");
                String  unique_no  =jsonObject.getString("UniqueNo");
                String  CheckUnique  =jsonObject.getString("CheckUnique");
                String  createddate  =jsonObject.getString("createddate");
                String  Image_sec  =jsonObject.getString("Image2");




                try
                {
                    Databaseutill db;
                    GetData get;
                    db = Databaseutill.getDBAdapterInstance(VisitRecord_Histary.this);
                    get = new GetData(db, VisitRecord_Histary.this);
                    get.setUSERRecord(DateofVisit1,id,name_visiter,pf_no,salutation,name_pro,addhar,firm_name,visit_date,acc_no,address,bussi_activity,runing_since,con_no,remark,pro_id,firm_id,user_id,longi,latitude,images ,pro_location,unique_no,CheckUnique,createddate,Image_sec);
                    ArrayList<HashMap<String,String>> listData=get.getUSERRecord();
                }
                catch (Exception e)
                {

                }
            }
        }
        catch(Exception e)
        {
            catchhandl++;
            e.printStackTrace();
        }

    }




}
