package planet_obcapp.com.obc_kyvapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;
import nl.changer.polypicker.model.Image;
import nl.changer.polypicker.utils.ImageInternalFetcher;
import planet_obcapp.com.obc_kyvapp.Adapter.View_Remarks_Adapter;
import planet_obcapp.com.obc_kyvapp.App_utils.*;
import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.Sqlite.Databaseutill;
import planet_obcapp.com.obc_kyvapp.Sqlite.GetData;

import static android.R.attr.bitmap;
import static planet_obcapp.com.obc_kyvapp.Adapter.Visit_Histry_CRG_Adapter.FROM_EDIT;

/**
 * Created by Admin on 7/3/2017.
 */

public class Visit_CRG extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText address, close_date, conversion_date, sol_id, account_no, revisit_date, remarks, no_account, lat,longi,branch_name;
    private EditText time_date, ins_name, cont_person, contact_details, lacation_name,time_date_re;
    private TextView close_date_txt, conversion_date_txt, sol_id_txt, account_no_txt, revisit_date_txt, remarks_txt,ac_no,nametxtre,uniqe_visit_no;
    private Calendar mCalendar;
    private Context Context;
    //private Button img_capture;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;
   // private ViewGroup mSelectedImagesContainer;
    private HashSet<Uri> mMedia = new HashSet<Uri>();
    private HashSet<Image> mMediaImages = new HashSet<Image>();
    private ArrayList<String> encodedString = new ArrayList<>();
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    static final int DATE_PICKER_ID = 1111;
    private PendingIntent pendingIntent;
    private String date_con, encode_image = "", img1, img2,UNIID;

    private String ID, date_time, customer_name, contact_person, con_detail, type_lead, status_lead, date_revisit, remark, result_CRG;
    private String date_close, date_conversion, id_sol, acc_no, no_of_acc, lati, longii, location, save_images, save_images_sec,first_time;
    private Spinner spn_lead, spn_status,spn_lead_two;
    String from = "NA";
    Bitmap bb;
    private String mRepeat,Unique_no,type_lead_two,enteredValue;
    private String mActive,date_time_re,item,Branch_name,sol_brach,TotalCount;
    private boolean mWasEdited = false;
    private TextView branch;
    private int  catchhandl;
    private Databaseutill db;
    //  private String sdate_time,scustomer_name,scon_person,scon_detail,slead_type,sstatus_lead,sdate_revist,sremark,sdate_close;
    //  private  String sdate_conversion,ssol_id,sno_of_account,saccount,slatti,slongi,slocation;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_crg);
        Context = Visit_CRG.this;
        static_text();
        mActive = "true";
        mRepeat = "true";
        close_date = (EditText) findViewById(R.id.close_date);
        conversion_date = (EditText) findViewById(R.id.date_converted);
        sol_id = (EditText) findViewById(R.id.sol_id);
        account_no = (EditText) findViewById(R.id.account_close);
        revisit_date = (EditText) findViewById(R.id.date_revisit);
        remarks = (EditText) findViewById(R.id.remarks_edt);
        no_account = (EditText) findViewById(R.id.no_account);
        time_date = (EditText) findViewById(R.id.date_time);
        time_date_re = (EditText) findViewById(R.id.date_time_re);
        //img_capture = (Button) findViewById(R.id.img_capture);
        ins_name = (EditText) findViewById(R.id.ins_name);
        cont_person = (EditText) findViewById(R.id.contact_person);
        contact_details = (EditText) findViewById(R.id.contact_details);
        lacation_name = (EditText) findViewById(R.id.lacation_name);

        lat = (EditText) findViewById(R.id.lat);
        longi = (EditText) findViewById(R.id.lang);
        address = (EditText) findViewById(R.id.lacation_name);

      //  mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        close_date_txt = (TextView) findViewById(R.id.close);
        conversion_date_txt = (TextView) findViewById(R.id.converted);
        sol_id_txt = (TextView) findViewById(R.id.sol);
        account_no_txt = (TextView) findViewById(R.id.acco);
        revisit_date_txt = (TextView) findViewById(R.id.revisit);
        remarks_txt = (TextView) findViewById(R.id.remarks);
        ac_no = (TextView) findViewById(R.id.account);
        nametxtre = (TextView) findViewById(R.id.datetime_re);
        Button submit = (Button) findViewById(R.id.submit);
        Button update = (Button) findViewById(R.id.update);
        Button logout_btn = (Button) findViewById(R.id.logout_btn);
        uniqe_visit_no = (TextView) findViewById(R.id.uniqe_visit_no);

        branch_name = (EditText) findViewById(R.id.branch_name);
        branch = (TextView) findViewById(R.id.branch);

//
//        Calendar c2 = Calendar.getInstance();
//        System.out.println("Current time =&gt; " + c2.getTime());
//        SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyyHHmm");
//        String formattedDate2 = df2.format(c2.getTime());


        sol_id.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

                enteredValue  = s.toString();

                if(s.toString().length()==4)
                {

                    if (isConnectingToInternet() == true) {
                            new CRG_BranchName().execute();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No Internet Connection....", Toast.LENGTH_LONG).show();
                        }


                }

                String newValue = enteredValue + "fdhhf";



            }
        });



        int s=0;
        try
        {
            Databaseutill db;
            GetData get;
            db = Databaseutill.getDBAdapterInstance(Visit_CRG.this);
            get = new GetData(db, Visit_CRG.this);
            s= get.GetSize_CRG();
        }
        catch (Exception e)
        {
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        //EncodeImage();




        Intent intent = getIntent();
        if (intent != null) {
            first_time= intent.getStringExtra("New");
            UNIID= intent.getStringExtra("UNIID");
            ID = intent.getStringExtra("ID");
            date_time = intent.getStringExtra("date_time");
            customer_name = intent.getStringExtra("customer_name");
            contact_person = intent.getStringExtra("con_person");
            con_detail = intent.getStringExtra("con_detail");

            type_lead = intent.getStringExtra("lead_type");
            status_lead = intent.getStringExtra("status_lead");
            date_revisit = intent.getStringExtra("date_revist");
            remark = intent.getStringExtra("remark");
            from = intent.getStringExtra("from");
            date_close = intent.getStringExtra("date_close");
            date_conversion = intent.getStringExtra("date_conversion");
            sol_brach = intent.getStringExtra("sol_id");
            no_of_acc = intent.getStringExtra("no_of_account");

            acc_no = intent.getStringExtra("account");
            lati = intent.getStringExtra("latti");
            longii = intent.getStringExtra("longi");
            location = intent.getStringExtra("location");

            Unique_no = intent.getStringExtra("UniqNO");
            type_lead_two = intent.getStringExtra("Lead_type_two");


            save_images = util.getFirstIma(this);
            save_images_sec = util.getSECIma(this);





            String add_img = save_images + "," + save_images_sec;


            if (from != null && from.equalsIgnoreCase(FROM_EDIT)) {
                update.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                time_date_re.setVisibility(View.VISIBLE);
                nametxtre.setVisibility(View.VISIBLE);
            }


           if(Unique_no!=null) {
               uniqe_visit_no.setText(Unique_no);
           }




            if (date_time != null) {
                time_date.setText(date_time);
            }

//            if (lati != null) {
//                lat.setText(lati);
//            }
//
//            if (longii != null) {
//                longi.setText(longii);
//            }
//
//
//            if (location != null) {
//                lacation_name.setText(location);
//            }

            if (customer_name != null) {
                ins_name.setText(customer_name);
            }

            if (contact_person != null) {
                cont_person.setText(contact_person);
            }

            if (con_detail != null) {
                contact_details.setText(con_detail);
            }

            if (date_revisit != null) {
                revisit_date.setText(date_revisit);
            }

            if (remark != null) {
                remarks.setText(remark);
            }
//            if (date_close != null) {
//                close_date.setText(date_close);
//            }
//            if (date_conversion != null) {
//                conversion_date.setText(date_conversion);
//            }

   if(sol_brach!=null) {
    try {
        String[] str = sol_brach.split("_");
        String Sol = str[0];
        String  branch_str = str[1];
        sol_id.setText(Sol);
        branch_name.setText(branch_str);

    } catch (Exception e) {
    }
   }
    if (id_sol != null) {
                sol_id.setText(id_sol);
            }
            if (no_of_acc != null) {
                no_account.setText(no_of_acc);
            }
            if (acc_no != null) {
                account_no.setText(acc_no);
            }
//            if (save_images != null && (!save_images.equalsIgnoreCase("")) && (!save_images.equalsIgnoreCase("null")) || save_images_sec != null && (!save_images_sec.equalsIgnoreCase("")) && (!save_images_sec.equalsIgnoreCase(""))) {
//                String images[] = add_img.split(",");
//
//                for (int i = 0; i < images.length; i++) {
//                    View imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);
//                    ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
//                    try {
//                        byte[] decodedString = Base64.decode(images[i], Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        thumbnail.setImageBitmap(decodedByte);
//                        mSelectedImagesContainer.addView(imageHolder);
//                    } catch (Exception e) {
//
//                    }
//
//                }
//            }
        }
        if (Branch_name != null) {
            sol_id.setText(Branch_name);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validateAllFields()) {
                    if (isConnectingToInternet() == true) {
                        new Submit_CRG_data().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No Internet Connection....", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAllFields()) {
                    if (isConnectingToInternet() == true) {
                        new update_CRG().execute();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No Internet Connection....", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        revisit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCurrentFocus()!=null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                Select_ReVisit_Date();

            }
        });


        String acco_no_count = ac_no.getText().toString();

//        for(int i=0;i<5;i++){
//        LinearLayout mRlayout = (LinearLayout) findViewById(R.id.lin);
//
//        EditText myEditText = new EditText(this);
//      //  myEditText.setLayoutParams(mRparams);
//        mRlayout.addView(myEditText);
//        }


        GPSTracker gps = new GPSTracker(Visit_CRG.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String lattt = String.valueOf(latitude);
            String langg = String.valueOf(longitude);
            lat.setText(lattt);
            longi.setText(langg);
        }

        if (gps != null) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
        } else {

        }
//        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//        }
//        img_capture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getImages();
//            }
//        });

        ImageView back = (ImageView) findViewById(R.id.back);
        spn_lead = (Spinner) findViewById(R.id.type_lead);
        spn_lead.setOnItemSelectedListener(this);
        List categories = new ArrayList();
        categories.add("-Select-");
        categories.add("Institutional CASA");
        categories.add("Non Institutional CASA");
        categories.add("TPP");
        categories.add("RAG");
        categories.add("MSME");
        categories.add("MID Corporate");
        categories.add("Large Corporate");

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_lead.setAdapter(dataAdapter);

        if (type_lead != null) {
            int spinnerPosition = dataAdapter.getPosition(type_lead);
            spn_lead.setSelection(spinnerPosition);
        }

        spn_status = (Spinner) findViewById(R.id.status_lead);
        spn_status.setOnItemSelectedListener(this);


        if(first_time.equalsIgnoreCase("record_first")){

                   if (isConnectingToInternet() == true) {
                new Total_no_dta_count().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection...", Toast.LENGTH_LONG).show();
            }



            List categorie = new ArrayList();
        categorie.add("-Select-");
        categorie.add("Open");
            ArrayAdapter dataAdapt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorie);
            dataAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_status.setAdapter(dataAdapt);

            if (status_lead != null) {
                int spinnerPosition = dataAdapt.getPosition(status_lead);
                spn_status.setSelection(spinnerPosition);
            }
        }else{
            List categorie = new ArrayList();
            categorie.add("-Select-");
            categorie.add("Open");
            categorie.add("Closed");
            categorie.add("Converted");
            ArrayAdapter dataAdapt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorie);
            dataAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_status.setAdapter(dataAdapt);

            if (status_lead != null) {
                int spinnerPosition = dataAdapt.getPosition(status_lead);
                spn_status.setSelection(spinnerPosition);
            }

        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        if (date_time == null) {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy,HH:mm");
            String formattedDate = df.format(c.getTime());
            time_date.setText(formattedDate);
        }


        Calendar c1 = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c1.getTime());
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy,HH:mm");
        String formattedDate1 = df1.format(c1.getTime());
        time_date_re.setText(formattedDate1);


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void EncodeImage() {

    }

    private void Select_ReVisit_Date() {

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        showDialog(DATE_PICKER_ID);


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // create a new DatePickerDialog with values you want to show

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
                Calendar calendar = Calendar.getInstance();

                calendar.add(Calendar.DATE, 0); // Add 0 days to Calendar
                Date newDate = calendar.getTime();
                datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));
                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            date_con = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

            String dateto_convert = String.valueOf(date_con);
            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
            Date newDate1 = null;
            try {
                newDate1 = spf.parse(dateto_convert);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                dateto_convert = spf.format(newDate1);
            } catch (Exception e) {
            }
            revisit_date.setText(dateto_convert);
        }
    };

    private void static_text() {
        TextView nametxt = (TextView) findViewById(R.id.datetime);
        String text = "<font color=#000>First Date of Visit and Time</font> <font color=#cc0029>*</font>";
        nametxt.setText(Html.fromHtml(text));
        nametxtre = (TextView) findViewById(R.id.datetime_re);
        String textre = "<font color=#000>Actual Date of ReVisit and Time</font> <font color=#cc0029>*</font>";
        nametxtre.setText(Html.fromHtml(textre));

        TextView pf = (TextView) findViewById(R.id.institude);
        String text1 = "<font color=#000>Name of Insitution/Customer</font> <font color=#cc0029>*</font>";
        pf.setText(Html.fromHtml(text1));

        TextView name_pro = (TextView) findViewById(R.id.person);
        String text2 = "<font color=#000>Contact Person</font> <font color=#cc0029>*</font>";
        name_pro.setText(Html.fromHtml(text2));


        TextView aa_na = (TextView) findViewById(R.id.detail);
        String text3 = "<font color=#000>Contact Number</font> <font color=#cc0029>*</font>";
        aa_na.setText(Html.fromHtml(text3));


        TextView fr_name = (TextView) findViewById(R.id.lead);
        String text4 = "<font color=#000>Type Of Lead</font> <font color=#cc0029>*</font>";
        fr_name.setText(Html.fromHtml(text4));


        TextView ac_name = (TextView) findViewById(R.id.status);
        String text5 = "<font color=#000>Status Of Lead</font> <font color=#cc0029>*</font>";
        ac_name.setText(Html.fromHtml(text5));


        TextView ac_no = (TextView)findViewById(R.id.led);
        String text6 = "<font color=#000>Sub Lead</font> <font color=#cc0029>*</font>";
        ac_no.setText(Html.fromHtml(text6));


        TextView aad = (TextView) findViewById(R.id.lat_txt);
        String text7 = "<font color=#000>Latitude</font> <font color=#cc0029>*</font>";
        aad.setText(Html.fromHtml(text7));


        TextView busss = (TextView) findViewById(R.id.lang_txt);
        String text8 = "<font color=#000>Longitude</font> <font color=#cc0029>*</font>";
        busss.setText(Html.fromHtml(text8));


//        TextView runnn = (TextView)findViewById(R.id.revisit);
//        String text9 = "<font color=#000>Date Of Revisit</font> <font color=#cc0029>*</font>";
//        runnn.setText(Html.fromHtml(text9));
//
//
//        TextView con_no = (TextView)findViewById(R.id.remarks);
//        String text10 = "<font color=#000>Remarks</font> <font color=#cc0029>*</font>";
//        con_no.setText(Html.fromHtml(text10));


        TextView loca_txt = (TextView) findViewById(R.id.location_txt);
        String text11 = "<font color=#000>Name of Location</font> <font color=#cc0029>*</font>";
        loca_txt.setText(Html.fromHtml(text11));


    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
      item = parent.getItemAtPosition(position).toString();


        close_date_txt = (TextView) findViewById(R.id.close);
        conversion_date_txt = (TextView) findViewById(R.id.converted);
        sol_id_txt = (TextView) findViewById(R.id.sol);
        account_no_txt = (TextView) findViewById(R.id.acco);
        revisit_date_txt = (TextView) findViewById(R.id.revisit);
        remarks_txt = (TextView) findViewById(R.id.remarks);


        if (item.equals("Open")) {
            revisit_date.setVisibility(view.VISIBLE);
            remarks.setVisibility(view.VISIBLE);
            revisit_date_txt.setVisibility(view.VISIBLE);
            remarks_txt.setVisibility(view.VISIBLE);

            close_date.setVisibility(view.GONE);
            conversion_date.setVisibility(View.GONE);
            sol_id.setVisibility(View.GONE);
            account_no.setVisibility(View.GONE);
            close_date_txt.setVisibility(View.GONE);
            conversion_date_txt.setVisibility(View.GONE);
            sol_id_txt.setVisibility(View.GONE);
            account_no_txt.setVisibility(View.GONE);
            no_account.setVisibility(View.GONE);
            ac_no.setVisibility(View.GONE);


            branch_name.setVisibility(View.GONE);
            branch.setVisibility(View.GONE);
            branch_name.setText("");
            sol_id.setText("");
            no_account.setText("");
            account_no.setText("");
        }
        if (item.equals("Closed")) {

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy,HH:mm");
            String formattedDate = df.format(c.getTime());
            close_date.setText(formattedDate);

            close_date.setVisibility(view.VISIBLE);
            close_date_txt.setVisibility(View.VISIBLE);

            conversion_date_txt.setVisibility(View.GONE);
            sol_id_txt.setVisibility(View.GONE);
            account_no_txt.setVisibility(View.GONE);
            revisit_date_txt.setVisibility(View.GONE);
            remarks_txt.setVisibility(View.GONE);
            no_account.setVisibility(View.GONE);
            conversion_date.setVisibility(View.GONE);
            sol_id.setVisibility(View.GONE);
            account_no.setVisibility(View.GONE);
            revisit_date.setVisibility(view.GONE);
            remarks.setVisibility(view.GONE);
            ac_no.setVisibility(View.GONE);


            branch_name.setVisibility(View.GONE);
            branch.setVisibility(View.GONE);

            sol_id.setText("");
            no_account.setText("");
            account_no.setText("");
            branch_name.setText("");
            revisit_date.setText("");
            remarks.setText("");

        }
        if (item.equals("Converted")) {

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy,HH:mm");
            String formattedDate = df.format(c.getTime());
            conversion_date.setText(formattedDate);
            conversion_date.setVisibility(View.VISIBLE);
            sol_id.setVisibility(View.VISIBLE);
            account_no.setVisibility(View.VISIBLE);
            conversion_date_txt.setVisibility(View.VISIBLE);
            sol_id_txt.setVisibility(View.VISIBLE);
            account_no_txt.setVisibility(View.VISIBLE);

            branch_name.setVisibility(View.VISIBLE);
            branch.setVisibility(View.VISIBLE);

            no_account.setVisibility(View.VISIBLE);


            close_date_txt.setVisibility(view.GONE);
            revisit_date_txt.setVisibility(view.GONE);
            remarks_txt.setVisibility(view.GONE);
            close_date.setVisibility(view.GONE);
            revisit_date.setVisibility(view.GONE);
            remarks.setVisibility(view.GONE);
            ac_no.setVisibility(View.VISIBLE);
            revisit_date.setText("");
            remarks.setText("");
        }
        if (item.equals("Institutional CASA")) {
            spn_lead_two = (Spinner) findViewById(R.id.type_lead_child);
            spn_lead_two.setOnItemSelectedListener(this);
            List categories = new ArrayList();
            categories.add("-Select-");
            categories.add("Private Limited Company");
            categories.add("Public Limited Company (unlisted)");
            categories.add("Cooperative Society");
            categories.add("Societies");
            categories.add("Trusts");
            categories.add("Clubs");
            categories.add("Association");
            categories.add("Association of Persons");

            categories.add("Central Government Department");
            categories.add("State Government Department");
            categories.add("Bodies – Agencies – Corp - by statute of State Govt.");
            categories.add("Bodies – Agencies – Corp - by statute of Central Govt.");
            categories.add("Banking Companies");
            categories.add("Overseas Corporate Bodies");
            categories.add("Local Bodies (Municipality etc)");

            categories.add("Local Bodies (Municipality etc)");
            categories.add("Microfinance Inst.");
            categories.add("NGO");
            categories.add("Multi Level Marketing Companies");
            categories.add("Public Limited Companies (listed)");
            categories.add("Limited Liability Partnership");
            categories.add("Insurance company");


            ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_lead_two.setAdapter(dataAdapter);

            if (type_lead_two != null) {
                int spinnerPosition = dataAdapter.getPosition(type_lead_two);
                spn_lead_two.setSelection(spinnerPosition);
            }
        }

        if (item.equals("Non Institutional CASA")) {

            spn_lead_two = (Spinner) findViewById(R.id.type_lead_child);
            spn_lead_two.setOnItemSelectedListener(this);
            List categories = new ArrayList();
            categories.add("-Select-");
            categories.add("Current Account");
            categories.add("Salary Premium Account");
            categories.add("Salary Plus Account");
            categories.add("Salute Account");
            categories.add("Saparivaar Account");
            categories.add("Yuva Account");
            categories.add("NRE/ NRO SB Account");
            categories.add("Pranaam SB Account");
            categories.add("Other Savings Account");

            categories.add("Oriental Pratham Account");
            categories.add("Oriental Stree Shakti Account");

            ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_lead_two.setAdapter(dataAdapter);
            if (type_lead_two != null) {
                int spinnerPosition = dataAdapter.getPosition(type_lead_two);
                spn_lead_two.setSelection(spinnerPosition);
            }
        }



        if (item.equals("TPP")) {

            spn_lead_two = (Spinner) findViewById(R.id.type_lead_child);
            spn_lead_two.setOnItemSelectedListener(this);
            List categories = new ArrayList();
            categories.add("-Select-");
            categories.add("Life Insurance");
            categories.add("Mediclaim");
            categories.add("Credit Card");
            categories.add("Demat Account");
            categories.add("Point of Sale(POS)");
            categories.add("Payment Gateway(PG)");
            categories.add(" Mutual Fund");
            categories.add("General Insurance");
            categories.add("Online Trading");

            ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_lead_two.setAdapter(dataAdapter);
            if (type_lead_two != null) {
                int spinnerPosition = dataAdapter.getPosition(type_lead_two);
                spn_lead_two.setSelection(spinnerPosition);
            }
        }

        if (item.equals("RAG")) {
            spn_lead_two = (Spinner) findViewById(R.id.type_lead_child);
            spn_lead_two.setOnItemSelectedListener(this);
            List categories = new ArrayList();
            categories.add("-Select-");
            categories.add("Housing Loan");
            categories.add("Vehicle Loan");
            categories.add("Education Loan");
            categories.add("OMLS");
            categories.add("Personal Loan");

            ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_lead_two.setAdapter(dataAdapter);
            if (type_lead_two != null) {
                int spinnerPosition = dataAdapter.getPosition(type_lead_two);
                spn_lead_two.setSelection(spinnerPosition);
            }
        }



        if (item.equals("MSME")) {
            spn_lead_two = (Spinner) findViewById(R.id.type_lead_child);
            spn_lead_two.setOnItemSelectedListener(this);
            List categories = new ArrayList();
            categories.add("-Select-");
            categories.add("Mudra Loan");
            categories.add("OBLS");
            categories.add("Cash Credit");
            categories.add("Term Loan");
            categories.add("Others");

            ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_lead_two.setAdapter(dataAdapter);
            if (type_lead_two != null) {
                int spinnerPosition = dataAdapter.getPosition(type_lead_two);
                spn_lead_two.setSelection(spinnerPosition);
            }
        }


        if (item.equals("MID Corporate")) {
            spn_lead_two= (Spinner) findViewById(R.id.type_lead_child);
            spn_lead_two.setOnItemSelectedListener(this);
            List categories = new ArrayList();
            categories.add("No Data");
            ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_lead_two.setAdapter(dataAdapter);
            if (type_lead_two != null) {
                int spinnerPosition = dataAdapter.getPosition(type_lead_two);
                spn_lead_two.setSelection(spinnerPosition);
            }
        }

        if (item.equals("Large Corporate")) {
            spn_lead_two = (Spinner) findViewById(R.id.type_lead_child);
            spn_lead_two.setOnItemSelectedListener(this);
            List categories = new ArrayList();
            categories.add("No Data");
            ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_lead_two.setAdapter(dataAdapter);

            if (type_lead_two != null) {
                int spinnerPosition = dataAdapter.getPosition(type_lead_two);
                spn_lead_two.setSelection(spinnerPosition);
            }
        }

    }
    public void onNothingSelected(AdapterView adapterView) {

    }

    private void getImages() {
        Intent intent = new Intent(Context, ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.blue)
                .setCameraButtonColor(R.color.orange)
                .setSelectionLimit(2)    // set photo selection limit. Default unlimited selection.
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, INTENT_REQUEST_GET_N_IMAGES);
    }


    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (resuleCode == Activity.RESULT_OK) {

            if (requestCode == INTENT_REQUEST_GET_IMAGES || requestCode == INTENT_REQUEST_GET_N_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                int[] parcelableOrientations = intent.getIntArrayExtra((ImagePickerActivity.EXTRA_IMAGE_ORIENTATIONS));
                if (parcelableUris == null) {
                    return;
                }

                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                int[] orientations = new int[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                System.arraycopy(parcelableOrientations, 0, orientations, 0, parcelableOrientations.length);

                if (uris != null) {
                    /*for (Uri uri : uris) {
                        Log.i(TAG, " uri: " + uri);
                        mMedia.add(uri);

                    }*/

                    mMediaImages.clear();
                    encodedString.clear();
                    for (int i = 0; i < orientations.length; i++) {
                        mMediaImages.add(new Image(uris[i], orientations[i],""));

                    }

                    //showMedia();
                }
            }
        }
    }

    private void showMedia() {

      //  mSelectedImagesContainer.removeAllViews();

        Iterator<Image> iterator = mMediaImages.iterator();
        String media_size = mMediaImages.size() + "";

        util.setMEdiacount(getApplication(), media_size);
        ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
        while (iterator.hasNext()) {
            Image image = iterator.next();


            try {
                if (mMedia.size() >= 1) {
                 //   mSelectedImagesContainer.setVisibility(View.VISIBLE);
                }

            } catch (NullPointerException ne) {

            }

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            String path = image.mUri.toString();


            bb = decodeFile(new File(path), 0, 0);
            if (bb != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy,HH:mm");
                String currentDateandTime = sdf.format(new Date());

               BitmapDrawable bitmapDrawable= SetDATE_Time(bb, currentDateandTime);
                Bitmap bitmap =bitmapDrawable.getBitmap();


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                String encode_String = Base64.encodeToString(b, Base64.DEFAULT);

                encodedString.add(encode_String);
            }


            if (!image.mUri.toString().contains("content://")) {
                // probably a relative uri
                image.mUri = Uri.fromFile(new File(image.mUri.toString()));


            }

            imageFetcher.loadImage(image.mUri, thumbnail, image.mOrientation);

          //  mSelectedImagesContainer.addView(imageHolder);

            // set the dimension to correctly
            // show the image thumbnail.
            int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }
    }


    private static Bitmap decodeFile(File f, int WIDTH, int HIGHT) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            if (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT) {
                scale *= 2;

                if (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT) {
                    scale *= 2;

                }
                if (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT) {
                    scale *= 2;

                }


            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
               // img_capture.setEnabled(true);
            }
        }
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
            address.setText(locationAddress);
        }
    }

    private boolean validateAllFields() {

        date_revisit = revisit_date.getText().toString();
        remark = remarks.getText().toString();
        date_close = close_date.getText().toString();
        date_conversion = conversion_date.getText().toString();

        id_sol = sol_id.getText().toString();
        acc_no = account_no.getText().toString();
        no_of_acc = no_account.getText().toString();
        lati = lat.getText().toString();
        longii = longi.getText().toString();
        location = lacation_name.getText().toString();

        Unique_no=uniqe_visit_no.getText().toString();

        String branch_str = branch_name.getText().toString();
        sol_brach = id_sol+"_"+branch_str;


//        for(int i=0;i<encodedString.size();i++)
//        {
//            encode_image=encode_image+encodedString.get(i);
//            encode_image=encode_image+",";
//        }
//        encode_image=encode_image.substring(0,encode_image.lastIndexOf(","));
//
//        if(encodedString.size()==1){
//            img1=encodedString.get(0);
//        }else{
//            img1=encodedString.get(0);
//            img2=encodedString.get(1);
//        }
        date_time_re = time_date_re.getText().toString();
        date_time = time_date.getText().toString();
        if (date_time.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Current Date and Time is Mandatry!", Toast.LENGTH_LONG).show();
            time_date.requestFocus(time_date.length());
            return false;

        }
        customer_name = ins_name.getText().toString();
        if (customer_name.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Name of Customer", Toast.LENGTH_LONG).show();
            ins_name.requestFocus(ins_name.length());
            return false;
        }
        contact_person = cont_person.getText().toString();
        if (contact_person.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Contact Person", Toast.LENGTH_LONG).show();
            cont_person.requestFocus(cont_person.length());
            return false;
        }
        con_detail = contact_details.getText().toString();
        if (con_detail.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Contact Number", Toast.LENGTH_LONG).show();
            contact_details.requestFocus(contact_details.length());
            return false;
        }
        type_lead = spn_lead.getSelectedItem().toString();
        if (type_lead.contains("-Select-")) {
            Toast.makeText(getApplicationContext(), "Please select Type of Lead!", Toast.LENGTH_LONG).show();
            spn_lead.requestFocus();
            return false;
        }
        type_lead_two = spn_lead_two.getSelectedItem().toString();
        if (type_lead_two.contains("-Select-")) {
            Toast.makeText(getApplicationContext(), "Please select Type of Sub Lead ", Toast.LENGTH_LONG).show();
            spn_lead_two.requestFocus();
            return false;
        }


        status_lead = spn_status.getSelectedItem().toString();
        if (status_lead.contains("-Select-")) {
            Toast.makeText(getApplicationContext(), "Please select Status of Lead!", Toast.LENGTH_LONG).show();
            spn_status.requestFocus();
            return false;
        }

        status_lead = spn_status.getSelectedItem().toString();
        if (status_lead.contains("Open") && date_revisit.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select Excepted  Date of Revisit!", Toast.LENGTH_LONG).show();
            spn_status.requestFocus();
            return false;
        }
        status_lead = spn_status.getSelectedItem().toString();
        if (status_lead.contains("Open") && remark.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Remark!", Toast.LENGTH_LONG).show();
            spn_status.requestFocus();
            return false;
        }

        status_lead = spn_status.getSelectedItem().toString();
        if (status_lead.contains("Converted") && ((id_sol.trim().equalsIgnoreCase("")) || id_sol.length() != 4)) {
            Toast.makeText(getApplicationContext(), "Please Enter 4 digit Sol ID!", Toast.LENGTH_LONG).show();
            spn_status.requestFocus();
            return false;
        }

        status_lead = spn_status.getSelectedItem().toString();
        if (status_lead.contains("Converted") && no_of_acc.trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please Enter No. of account!", Toast.LENGTH_LONG).show();
            spn_status.requestFocus();
            return false;
        }

        status_lead = spn_status.getSelectedItem().toString();
        if (status_lead.contains("Converted") && ((acc_no.trim().equalsIgnoreCase("")) || acc_no.length() != 14)) {
            Toast.makeText(getApplicationContext(), "Please Enter 14 digit Account No.!", Toast.LENGTH_LONG).show();
            spn_status.requestFocus();
            return false;
        }


        ////
//        if (mSelectedImagesContainer == null || mSelectedImagesContainer.getChildCount() < 1) {
//            Toast.makeText(getApplicationContext(),
//                    "Please select image", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        ////

//        if ( encodedString==null || encodedString.isEmpty()) {
//            Toast.makeText(getApplicationContext(),
//                    "Please select image", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;
    }

    class Submit_CRG_data extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Visit_CRG.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            sand_crg_data();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            Intent myIntent = new Intent(Visit_CRG.this, MyAlarmService.class);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            pendingIntent = PendingIntent.getService(Visit_CRG.this, 0, myIntent, 0);
            Calendar calendar = Calendar.getInstance();
//if(!date_con.equalsIgnoreCase("")){
            String date_con = revisit_date.getText().toString();

            String dateto_convert = String.valueOf(date_con);
            SimpleDateFormat spf = new SimpleDateFormat("dd-MMM-yyyy");
            Date newDate1 = null;
            try {
                newDate1 = spf.parse(dateto_convert);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                dateto_convert = spf.format(newDate1);
            } catch (Exception e) {
            }
            //  revisit_date.setText(dateto_convert);


         try {
                String[] date_s = dateto_convert.split("/");
                String d = date_s[0];
                String m = date_s[1];
                String y = date_s[2];

                ReminderDatabase rb = new ReminderDatabase(Context);

                // Creating Reminder
                int ID = rb.addReminder(new Reminder("",dateto_convert,"9:00", "", "", "", ""));


                // Set up calender for creating the notification
                mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.MONTH, Integer.parseInt(m)-1);
                mCalendar.set(Calendar.YEAR, Integer.parseInt(y));
                mCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d));
                mCalendar.set(Calendar.HOUR_OF_DAY, 9);
                mCalendar.set(Calendar.MINUTE, 00);
                mCalendar.set(Calendar.SECOND, 0);

                if (mActive.equals("true")) {
                    if (mRepeat.equals("true")) {
                        new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);
//                new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalendar, ID, mRepeatTime);
//            } else if (mRepeat.equals("false")) {
                    }
                }


             //   new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);
//
//                calendar.set(Calendar.MONTH, Integer.parseInt(m) - 1);
//                calendar.set(Calendar.YEAR, Integer.parseInt(y));
//                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d));
//
//
//                calendar.set(Calendar.HOUR_OF_DAY, 9);
//                calendar.set(Calendar.MINUTE, 00);
//                calendar.set(Calendar.SECOND, 00);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);  //set repeating every 24 hours


//
//		Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
//		pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent,0);
//
//		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
               // alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);


         } catch (Exception e) {
           }

           if(catchhandl==0){
            if (result_CRG.equalsIgnoreCase("1")) {
                Toast.makeText(Context,
                        "Record Submitted Successfully", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(Visit_CRG.this, CRG_Visit_MAin.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            } else {
                Toast.makeText(Context,
                        "Please try again!", Toast.LENGTH_LONG).show();
            }

//            try {
//                Databaseutill db;
//                GetData get;
//                db = Databaseutill.getDBAdapterInstance(Visit_CRG.this);
//                get = new GetData(db, Visit_CRG.this);
//                get.setUSERRecordCRG("",date_time, customer_name, contact_person, con_detail, type_lead, status_lead, date_revisit, remark, date_close, date_conversion, sol_brach, no_of_acc, acc_no, img1, img2, lati, longii,location,type_lead_two,Unique_no," ");
//                ArrayList<HashMap<String, String>> listData = get.getUSERRecordCRG();
//
//            } catch (Exception e) {
//
//            }
           }else{

               Toast.makeText(getApplicationContext(),
                       "Sorry, something went wrong there. Try again.", Toast.LENGTH_LONG).show();

           }
            if (Dialog.isShowing()) {
                Dialog.dismiss();

            }
        }
    }

    private void sand_crg_data() {
        catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_SaveRecord";
        final String METHOD_NAME = "CRG_SaveRecord";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        try {
            for (int i = 0; i < encodedString.size(); i++) {
                encode_image = encode_image + encodedString.get(i);
                encode_image = encode_image + ",";
            }
            encode_image = encode_image.substring(0, encode_image.lastIndexOf(","));

            if (encodedString.size() == 1) {
                img1 = encodedString.get(0);
            } else {
                img1 = encodedString.get(0);
                img2 = encodedString.get(1);
                if(img2==null){
                    img2="";
                }
            }
        } catch (Exception e) {
        }


        request.addProperty("calltype", "2");
        request.addProperty("PFNumber", util.getpfno(Context));

        request.addProperty("datevisit", date_time);
        request.addProperty("namecustomer", customer_name);

        request.addProperty("contperson", contact_person);
        request.addProperty("contdetail", con_detail);

        request.addProperty("typelead", type_lead);
        request.addProperty("statuslead", status_lead);

        request.addProperty("no_ofaccount", no_of_acc);
        request.addProperty("image1", "");

        request.addProperty("image2", "");
        request.addProperty("latitude", lati);

        request.addProperty("longitude", longii);
        request.addProperty("location_name", location);

        request.addProperty("sol_id", sol_brach);
        request.addProperty("accountno", acc_no);

        request.addProperty("remark", remark);
        request.addProperty("date_revisit", date_revisit);

        request.addProperty("date_close", date_close);
        request.addProperty("date_conversion", date_conversion);

        request.addProperty("pId", "");
        request.addProperty("type_lead_ddl",type_lead_two);
        request.addProperty("uniq_visit_no", Unique_no);
        request.addProperty("revisit_time", "");

//        try {
//            Databaseutill db;
//            GetData get;
//            db = Databaseutill.getDBAdapterInstance(Visit_CRG.this);
//            get = new GetData(db, Visit_CRG.this);
//            get.setUSERRecordCRG("",date_time, customer_name, contact_person, con_detail, type_lead, status_lead, date_revisit, remark, date_close, date_conversion, id_sol, no_of_acc, acc_no, img1, img2, lati,longii,location,type_lead_two,Unique_no,"");
//            ArrayList<HashMap<String, String>> listData = get.getUSERRecordCRG();
//
//        } catch (Exception e) {
//
//        }
        date_time = time_date.getText().toString();
        if (item.equals("Open")) {
            try {
                Databaseutill db;
                GetData get;
                db = Databaseutill.getDBAdapterInstance(Visit_CRG.this);
                get = new GetData(db, Visit_CRG.this);
                get.setUSERCRGOPEN(date_time,date_revisit,remark,"Open",util.getpfno(this));
                ArrayList<HashMap<String, String>> listData = get.getUSERCRGOPEN();
            } catch (Exception e) {

            }
        }

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
                result_CRG = jsonObject.getString("result");

            }

        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }
    }


    public String removeValue(HashSet<String> hashSet) {
        if (hashSet == null || hashSet.size() <= 0)
            return null;

        String fisrtItem = getFirstItem(hashSet);
        if (fisrtItem.contains(fisrtItem))
            hashSet.remove(fisrtItem);

        if (hashSet.size() <= 0)
            return null;

        Iterator<String> iterator2 = hashSet.iterator();
        while (iterator2.hasNext()) {
            return (String) iterator2.next();
        }
        return null;

    }

    private String getFirstItem(HashSet<String> hashSet) {
        Iterator<String> iterator = hashSet.iterator();
        while (iterator.hasNext()) {
            return (String) iterator.next();

        }
        return "";
    }


    class update_CRG extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Visit_CRG.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Update_crg_data();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//
//            Intent myIntent = new Intent(Visit_CRG.this, MyAlarmService.class);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            pendingIntent = PendingIntent.getService(Visit_CRG.this, 0, myIntent, 0);
//            Calendar calendar = Calendar.getInstance();
//if(!date_con.equalsIgnoreCase("")){
           date_con = revisit_date.getText().toString();


            String dateto_convert = String.valueOf(date_con);
            SimpleDateFormat spf = new SimpleDateFormat("dd-MMM-yyyy");
            Date newDate1 = null;
            try {
                newDate1 = spf.parse(dateto_convert);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                dateto_convert = spf.format(newDate1);
            } catch (Exception e) {
            }
            //  revisit_date.setText(dateto_convert);


            try {
                String[] date_s = dateto_convert.split("/");
                String d = date_s[0];
                String m = date_s[1];
                String y = date_s[2];


                ReminderDatabase rb = new ReminderDatabase(Context);

                // Creating Reminder
                int ID = rb.addReminder(new Reminder("",dateto_convert,"9:00", "", "", "", ""));


                // Set up calender for creating the notification
                mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.MONTH, Integer.parseInt(m)-1);
                mCalendar.set(Calendar.YEAR, Integer.parseInt(y));
                mCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d));
                mCalendar.set(Calendar.HOUR_OF_DAY, 9);
                mCalendar.set(Calendar.MINUTE, 00);
                mCalendar.set(Calendar.SECOND, 0);

                if (mActive.equals("true")) {
                    if (mRepeat.equals("true")) {
                        new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);
//                new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalendar, ID, mRepeatTime);
//            } else if (mRepeat.equals("false")) {
                    }
                }

//                calendar.set(Calendar.MONTH, Integer.parseInt(m) - 1);
//                calendar.set(Calendar.YEAR, Integer.parseInt(y));
//                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d));
//
//
//                calendar.set(Calendar.HOUR_OF_DAY, 9);
//                calendar.set(Calendar.MINUTE, 00);
//                calendar.set(Calendar.SECOND, 00);
//                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);  //set repeating every 24 hours
//
//
////
////		Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
////		pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent,0);
////
////		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);


            } catch (Exception e) {
            }
if(catchhandl==0){
            if (result_CRG.equalsIgnoreCase("1")) {
                Toast.makeText(Context,
                        "Record Updated Successfully", Toast.LENGTH_LONG).show();

                Intent mainIntent = new Intent(Visit_CRG.this, CRG_Visit_MAin.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            } else {
                Toast.makeText(Context,
                        "Please try again", Toast.LENGTH_LONG).show();

            }
      }else{
        Toast.makeText(getApplicationContext(),
            "Network Error...Please Try Again", Toast.LENGTH_LONG).show();

           }

            if (Dialog.isShowing()) {
                Dialog.dismiss();

            }
        }
    }

    private void Update_crg_data() {
        catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL =Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_SaveRecord";
        final String METHOD_NAME = "CRG_SaveRecord";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        try {
            for (int i = 0; i < encodedString.size(); i++) {
                encode_image = encode_image + encodedString.get(i);
                encode_image = encode_image + ",";
            }
            encode_image = encode_image.substring(0, encode_image.lastIndexOf(","));

            if (encodedString.size() == 1) {
                img1 = encodedString.get(0);
            } else {
                img1 = encodedString.get(0);
                img2 = encodedString.get(1);
                if(img2==null){
                    img2="";
                }

            }
        } catch (Exception e) {
        }
//        try {
//            if (encodedString.size() == 0 && mSelectedImagesContainer.getChildCount() > 0) {
//                img1 = save_images;
//                img2 = save_images_sec;
//            }
//        } catch (Exception e) {
//        }


        request.addProperty("calltype", "3");
        request.addProperty("PFNumber", util.getpfno(Context));

        request.addProperty("datevisit", date_time);
        request.addProperty("namecustomer", customer_name);

        request.addProperty("contperson", contact_person);
        request.addProperty("contdetail", con_detail);

        request.addProperty("typelead", type_lead);
        request.addProperty("statuslead", status_lead);

        request.addProperty("no_ofaccount", no_of_acc);
        request.addProperty("image1", "");

        request.addProperty("image2", "");
        request.addProperty("latitude", lati);

        request.addProperty("longitude", longii);
        request.addProperty("location_name", location);

        request.addProperty("sol_id", sol_brach);
        request.addProperty("accountno", acc_no);

        request.addProperty("remark", remark);
        request.addProperty("date_revisit", date_revisit);

        request.addProperty("date_close", date_close);
        request.addProperty("date_conversion", date_conversion);
        request.addProperty("pId", UNIID);
        request.addProperty("type_lead_ddl",type_lead_two);
        request.addProperty("uniq_visit_no", Unique_no);
        request.addProperty("revisit_time",date_time_re);

        date_time = time_date.getText().toString();
        if (item.equals("Open")) {
            try {
                Databaseutill db1;
                GetData get1;
                db1 = Databaseutill.getDBAdapterInstance(Visit_CRG.this);
                get1 = new GetData(db1, Visit_CRG.this);
                get1.setUSERCRGOPEN(date_time,date_revisit,remark,"Open",util.getpfno(this));
                ArrayList<HashMap<String, String>> listData = get1.getUSERCRGOPEN();
            } catch (Exception e) {

            }
        }


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

//
//        ContentValues cv = new ContentValues();
//        cv.put("date_time", date_time); //These Fields should be your String values of actual column names
//        cv.put("customer_name", customer_name);
//        cv.put("con_person", contact_person);
//
//        cv.put("con_details", con_detail);
//        cv.put("lead_type", type_lead);
//        cv.put("status_lead", status_lead);
//        cv.put("date_revisit", date_revisit);
//        cv.put("remarks", remark);
//        cv.put("date_close", date_close);
//
//        cv.put("date_conversion", date_conversion);
//        cv.put("sol_id", sol_brach);
//        cv.put("no_of_acc", no_of_acc);
//        cv.put("account_no", acc_no);
//        cv.put("image_one", img1);
//        cv.put("image_two", img2);
//        cv.put("lati", lati);
//        cv.put("longi", longii);
//        cv.put("location_nmae", location);
//        cv.put("type_lead_two", type_lead_two);
//        cv.put("unique_no", Unique_no);
//        cv.put("date_time_re", date_time_re);
//
//
//        Databaseutill db = new Databaseutill(Context);
//        GetData get;
//        try {
//            db.createDatabase();
//        } catch (Exception e) {
//        }
//        int result = db.updateRecordsInDB("USERRecordCRG", cv, "ID=" + ID, null);






//
//        Toast.makeText(Context,
//                "Record Updated Successfully", Toast.LENGTH_LONG).show();
//
//
//        Intent mainIntent = new Intent(Visit_CRG.this, CRG_Visit_MAin.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(mainIntent);
//        finish();


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

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                result_CRG = jsonObject.getString("result");

            }

        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }
    }


    class CRG_BranchName extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Visit_CRG.this);

        @Override
        protected void onPreExecute()   {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Sol_ID_Brance_NAme();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


//            Toast.makeText(getApplicationContext(),
//                    "dghsdighsid" +Branch_name , Toast.LENGTH_LONG).show();

            if(catchhandl==0){
            if(Branch_name!=null){
                branch_name.setText(Branch_name);
           }else{
                branch_name.setText("");
                Toast.makeText(getApplicationContext(),
                    "Incorrect Sol Id" , Toast.LENGTH_LONG).show();
        }
        }else{
                Toast.makeText(getApplicationContext(),
                        "Network Error...Please Try Again", Toast.LENGTH_LONG).show();
            }

            if (Dialog.isShowing()) {
                Dialog.dismiss();

            }

        }
    }

    private void Sol_ID_Brance_NAme() {
      catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_BranchName";
        final String METHOD_NAME = "CRG_BranchName";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("SOLid",enteredValue);


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



            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Branch_name = jsonObject.getString("Branch name");




            }

        } catch(Exception e){
            catchhandl++;
            e.printStackTrace();
        }
    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) Visit_CRG.this
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
                             //   util.setPassword_CRG(Context, "");
                                util.setusername_CRG(Context, "");

                                Intent i = new Intent(Visit_CRG.this, Login_Activity.class);
                                startActivity(i);
                                finish();

                                try{
                                    db = Databaseutill.getDBAdapterInstance(Visit_CRG.this);
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


    private BitmapDrawable SetDATE_Time(Bitmap bitmap, String text) {

      /*  Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.imgpsh_fullsiz)
                .copy(Bitmap.Config.ARGB_8888, true);*/
        BitmapDrawable newBitmap=null;

        try {


            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);


            Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setTypeface(tf);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(convertToPixels(Visit_CRG.this, 14));

            Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);

            Canvas canvas = new Canvas(mutableBitmap);

            //If the text is bigger than the canvas , reduce the font size
            if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
                paint.setTextSize(convertToPixels(Visit_CRG.this, 7));        //Scaling needs to be used for different dpi's

            //Calculate the positions
            int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

            //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

            canvas.drawText(text, xPos, yPos, paint);

            newBitmap= new BitmapDrawable(getResources(), mutableBitmap);
        } catch (Exception e) {
            e.getMessage();
        }
        return newBitmap;
    }


    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    class Total_no_dta_count extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(Visit_CRG.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            product();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(catchhandl==0){
                try{

                    String recordCount="";
                    int total1= Integer.parseInt(TotalCount);
                    int total=total1+1;
                    if(total<9)
                    {
                        recordCount="0000"+total;
                    }else{
                        recordCount="000"+total;
                    }

                    Unique_no= util.getpfno(getApplicationContext())+ recordCount;
                    uniqe_visit_no.setText(Unique_no);


                }catch (Exception e){}
            }else{

                Toast.makeText(getApplicationContext(),
                        "Sorry, something went wrong there. Try again.", Toast.LENGTH_LONG).show();

            }
            if (Dialog.isShowing()) {
                Dialog.dismiss();
            }
        }
    }
    private void product() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url_crg;
        final String SOAP_ACTION = "http://tempuri.org/CRG_GetTotalUserCount";
        final String METHOD_NAME = "CRG_GetTotalUserCount";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("pfnumber",util.getpfno(this));


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

                TotalCount = jsonObject.getString("TotalCount");


            }

        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }


    }
}
