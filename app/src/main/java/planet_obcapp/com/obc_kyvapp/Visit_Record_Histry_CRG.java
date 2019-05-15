package planet_obcapp.com.obc_kyvapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import planet_obcapp.com.obc_kyvapp.Adapter.Visit_Histry_CRG_Adapter;
import planet_obcapp.com.obc_kyvapp.App_utils.*;
import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.Sqlite.Databaseutill;
import planet_obcapp.com.obc_kyvapp.Sqlite.GetData;
import static planet_obcapp.com.obc_kyvapp.Login_Activity.FROM;

/**
 * Created by Admin on 7/19/2017.
 */

public class Visit_Record_Histry_CRG extends AppCompatActivity implements Animation.AnimationListener{

    ArrayList<HashMap<String,String>> listData;
    Visit_Histry_CRG_Adapter proadb;
    ListView data_list;
    private   TextView no_found;
    private Databaseutill db;
    private String from = "NA";
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";
    private int  catchhandl;
    private DatePickerDialog datePickerDialog;
    private String date,date_change,str_to_date,str_from_date;
    private EditText to_date,from_date;
    private String year1,mMonth1,mDay1,year2,mMonth2,mDay2;
    private   Date date_d,date_s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_history_crg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        no_found= (TextView)findViewById(R.id.no_data);
        data_list = (ListView) findViewById(R.id.lll);
        to_date = (EditText)findViewById(R.id.to_date);
        from_date = (EditText)findViewById(R.id.from_date);
        Button srch_date = (Button)findViewById(R.id.srch_date);



        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_to_Date();
            }
        });
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_From_Date();
            }
        });

        try{
            data_list = (ListView) findViewById(R.id.lll);
            Databaseutill db;
            GetData get;
            db = Databaseutill.getDBAdapterInstance(getApplication());
            get = new GetData(db, getApplication());
            listData=get.getUSERRecordCRG();
            proadb = new Visit_Histry_CRG_Adapter(Visit_Record_Histry_CRG.this,listData,Visit_Record_Histry_CRG.this);
            data_list.setAdapter(proadb);

        }catch ( Exception e){
        }



        srch_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    db = Databaseutill.getDBAdapterInstance(Visit_Record_Histry_CRG.this);
                    GetData gett= new  GetData(db, getApplication());
                    gett.Delete_record_CRG();

                }catch (Exception e){
                }

            try{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                date_d=sdf.parse(mDay1 +"/"+ mMonth1 +"/"+ year1);
                date_s=sdf.parse(mDay2 +"/"+ mMonth2 +"/"+ year2);



             }catch (Exception e){}



                str_to_date=to_date.getText().toString();
                str_from_date=from_date.getText().toString();


                if(str_to_date.equalsIgnoreCase("") || str_from_date.equalsIgnoreCase("") ){
                    Toast.makeText(getApplicationContext(), "Please Select Start date and End date" ,Toast.LENGTH_LONG).show();
                }
//                else if(date_d.before(date_s)){
//                    Toast.makeText(getApplicationContext(), "End date should be greater than  Start date" ,Toast.LENGTH_LONG).show();
//
//                }
//                else if(str_to_date.equals(str_from_date)){
//                    Toast.makeText(getApplicationContext(), "From date should not be equals to To date" ,Toast.LENGTH_LONG).show();
//
//
//                }
                else if(isConnectingToInternet()==true) {
                    new Fetch_record_API().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "No Internet Connection....", Toast.LENGTH_LONG).show();
                    //  }
                    //  }
                }

            }
        });


        Animation_image();
        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getStringExtra("from");
        }

       // data_list.setOnScrollListener(new EndlessScrollListener());{


    // if (from != null && from.equalsIgnoreCase(FROM)) {


      //    if (from != null && from.equalsIgnoreCase(FROM)) {

        if(isConnectingToInternet()==true) {
        //  new FetchUpdate_record_API().execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "No Internet Connection....", Toast.LENGTH_LONG).show();
            }
       // }



    }

    private void Select_From_Date() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        year2= String.valueOf(mYear);
        mMonth2= String.valueOf(mMonth);
        mDay2= String.valueOf(mDay);

        // date picker dialog
        datePickerDialog = new DatePickerDialog(Visit_Record_Histry_CRG.this,
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
                        spf= new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            date_change = spf.format(newDate);
                            from_date.setText(date_change);
                            //  Toast.makeText(getApplicationContext(), "Date "+ date_change ,Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    private void Select_to_Date() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        year1= String.valueOf(mYear);
       mMonth1= String.valueOf(mMonth);
       mDay1= String.valueOf(mDay);

        // date picker dialog
        datePickerDialog = new DatePickerDialog(Visit_Record_Histry_CRG.this,
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
                        spf= new SimpleDateFormat("dd/MM/yyyy");
                        try{
                            date_change = spf.format(newDate);
                            to_date.setText(date_change);
                            //  Toast.makeText(getApplicationContext(), "Date "+ date_change ,Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
//            Intent mainIntent = new Intent(Visit_Record_Histry_CRG.this, Visit_CRG.class);
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//          startActivity(mainIntent);
            onBackPressed();
            return true;}
        return super.onOptionsItemSelected(item);}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              if(newText.length()>0) {
                  search(newText);

              }
              else {
                  proadb = new Visit_Histry_CRG_Adapter(Visit_Record_Histry_CRG.this,listData,Visit_Record_Histry_CRG.this);
                  data_list.setAdapter(proadb);
              }
                  return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void search(String srchText)
    {

      //  showprogressdialog();


        ArrayList<HashMap<String,String>> new_list=new ArrayList<HashMap<String,String>>();
        String search_text=srchText.trim().toLowerCase();
        for(int i=0;i<listData.size();i++)
        {
            HashMap<String,String> hashMap=listData.get(i);

            String DateTime=  hashMap.get(Utility.KEY_DATE_TIME).toLowerCase();
            String CustName= hashMap.get(Utility.KEY_CUSTOMER_NAME).toLowerCase();

            if(DateTime.contains(search_text) || CustName.contains(search_text))
            {
                new_list.add(hashMap);
            }



          //  CreateDetails createDetails=createlist.get(i);
           // String uniqueCrateId=createDetails.getUnique_crate_id().toLowerCase();
          //  String crateDesc=createDetails.getDescription().toLowerCase();
            /*if(uniqueCrateId.contains(search_text) || crateDesc.contains(search_text))
            {
                new_list.add(createDetails);
            }*/
        }
        try {

           ///

            proadb = new Visit_Histry_CRG_Adapter(Visit_Record_Histry_CRG.this,new_list,Visit_Record_Histry_CRG.this);
            data_list.setAdapter(proadb);



            ///

           /* adapters = new HomeCreateAdapter(Homeact.this, new_list);
            listView.setAdapter(adapters);*/

            if (new_list.size() > 0) {


              //  no_found.setVisibility(View.GONE);
            } else {
               // no_found.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("xsas", e.toString());
            // Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_SHORT).show();
           // no_found.setVisibility(View.GONE);

        }

       /// hideprogressdialog();
    }



    class Fetch_record_API extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Visit_Record_Histry_CRG.this);

        @Override
        protected void onPreExecute()   {
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

            if(catchhandl==0){
            try{
                data_list = (ListView) findViewById(R.id.lll);
                Databaseutill db;
                GetData get;
                db = Databaseutill.getDBAdapterInstance(getApplication());
                get = new GetData(db, getApplication());
                listData=get.getUSERRecordCRG();
                proadb = new Visit_Histry_CRG_Adapter(Visit_Record_Histry_CRG.this,listData,Visit_Record_Histry_CRG.this);
                data_list.setAdapter(proadb);

            }catch ( Exception e){
            }
            }else {
                Toast.makeText(getApplicationContext(),
                        "Network Error...Please Try Again", Toast.LENGTH_LONG).show();
            }

            if(listData.size()==0){
                no_found.setVisibility(View.VISIBLE);
            }else{
                no_found.setVisibility(View.GONE);
            }


            if (Dialog.isShowing()) {
                Dialog.dismiss();

            }
        }
    }
    private void fetch_record() {
          catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_FetchRecord";
        final String METHOD_NAME = "CRG_FetchRecord";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("PFNo",util.getpfno(this));
        request.addProperty("FromDate",str_from_date);
        request.addProperty("Todate",str_to_date);

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
                db = Databaseutill.getDBAdapterInstance(Visit_Record_Histry_CRG.this);
                GetData gett= new  GetData(db, getApplication());
                gett.Delete_record_CRG();

            }catch (Exception e){
            }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String ID_unique = jsonObject.getString("ID_unique");
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
                            db = Databaseutill.getDBAdapterInstance(Visit_Record_Histry_CRG.this);
                            get = new GetData(db, Visit_Record_Histry_CRG.this);
                            get.setUSERRecordCRG(ID_unique,date_visit, name_inst, contact_person, contact_detail, lead_type, status_lead, date_Revisit, remark, date_close, date_conversion, SolID, No_of_Accounts, AccountNumber, image1, image2, latitude, longitude, location_name,type_lead_ddl,unique_visit_no,revisit_time);
                            ArrayList<HashMap<String, String>> listData = get.getUSERRecordCRG();

                        } catch (Exception e) {

                        }
                    }

        } catch(Exception e){
            catchhandl++;
            e.printStackTrace();
        }
    }

    private boolean isConnectingToInternet()
      {
        ConnectivityManager connectivity = (ConnectivityManager)Visit_Record_Histry_CRG.this
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
}



