package planet_obcapp.com.obc_kyvapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;

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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;
import nl.changer.polypicker.model.Image;
import nl.changer.polypicker.utils.ImageInternalFetcher;
import planet_obcapp.com.obc_kyvapp.Adapter.FirmAdapter;
import planet_obcapp.com.obc_kyvapp.Adapter.ProprietorAdapter;
import planet_obcapp.com.obc_kyvapp.App_utils.CameraUtils;
import planet_obcapp.com.obc_kyvapp.App_utils.GPSTracker;
import planet_obcapp.com.obc_kyvapp.App_utils.UniqueID;
import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.App_utils.util;
import planet_obcapp.com.obc_kyvapp.Helper.ListAndGirdShow;
import planet_obcapp.com.obc_kyvapp.Sqlite.Databaseutill;
import planet_obcapp.com.obc_kyvapp.Sqlite.GetData;


/**
 * Created by planet on 3/22/17.
 */

public class VisitRecord_sec_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 3;
    public static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 1;
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;
    private static final int REQUEST_CODE_PERMISSION = 1;
    private static String imageStoragePath;
    final android.os.Handler mHandler = new android.os.Handler();
    ArrayList<String> listhh;
    ArrayList<String> listyy;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private Context context;
    private String s_curr_date, s_month_date, sname, spf_no, spro_name, saadh_no, sfirm_name, sac_no, saddress, sbuss_act, scon_no, sremarks, ssap_spn, receivedString, responsestatus;
    private Context mContext;
    private Button img_btn;
    private ProprietorAdapter proadb;
    private FirmAdapter firmadapter;
    private EditText edt_lat, edt_lang, other_propert;
    private ViewGroup mSelectedImagesContainer;
    private HashSet<Uri> mMedia = new HashSet<Uri>();
    private HashSet<Image> mMediaImages = new HashSet<Image>();
    private ListView propri_list, firm_list;
    private ArrayList<HashMap<String, String>> list1;
    private ArrayList<HashMap<String, String>> list2;
    private String latti, longgi, pro_location, encode_image = "", p_location, pro, firm;
    private ArrayList<String> encodedString = new ArrayList<>();
    private String save_prolocation, save_propreitor, save_firm, uniq_visit_no, subMitdataLimit, img1, img2, l_lat, L_longi;
    private Spinner pro_loca;
    private int catchhandl;
    private View imageHolder;
    private ImageView thumbnail;

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

    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_record_sec);


        mContext = VisitRecord_sec_Activity.this;
        GPSTracker gps = new GPSTracker(VisitRecord_sec_Activity.this);
        edt_lat = (EditText) findViewById(R.id.lat);
        edt_lang = (EditText) findViewById(R.id.lang);
        pro_loca = (Spinner) findViewById(R.id.pro_spn);
        pro_loca.setOnItemSelectedListener(this);

        other_propert = (EditText) findViewById(R.id.visit_locati_edt);
        TextView sol_id = (TextView) findViewById(R.id.sol_idss);
        TextView visit_no = (TextView) findViewById(R.id.visit_no);
        img_btn = (Button) findViewById(R.id.img_capture);
        Button submit = (Button) findViewById(R.id.Submit);
        Button preview = (Button) findViewById(R.id.pre);
        sol_id.setText("Welcome to SOL ID " + util.getSOLID_value(this));
        propri_list = (ListView) findViewById(R.id.proprietor_list);
        firm_list = (ListView) findViewById(R.id.firm_list);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);


        p_location = util.getPLocation(this);
        savePLocation_pref();
        pro = util.getProprietor(this);
        firm = util.getFirm(this);
        String other_edt = util.getOtherLocation(this);
        if (other_edt != null) {
            other_propert.setText(other_edt);
        }

        if (!haveNetworkConnection()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection Found", Toast.LENGTH_SHORT).show();
        } else {
            new Get_Data().execute();
        }
        if (!haveNetworkConnection()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection Found", Toast.LENGTH_SHORT).show();
        } else {
            new Get_DataPropriter().execute();
        }

        if (mSelectedImagesContainer != null) {
            String image_pref = util.getImages(this);

            mSelectedImagesContainer.destroyDrawingCache();

            if (image_pref != null && (!image_pref.equalsIgnoreCase(""))) {

                String images[] = image_pref.split(",");
                if (images.length > 0) {


                    for (int i = 0; i < images.length; i++) {
                        encodedString.add(images[i]);
                        imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);
                        thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
                        byte[] decodedString = Base64.decode(images[i], Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        thumbnail.setImageBitmap(decodedByte);
                        mSelectedImagesContainer.addView(imageHolder);

                    }
                }
            }
        }

        Intent intent = getIntent();
        if (intent != null) {

            sname = intent.getStringExtra("name");
            ssap_spn = intent.getStringExtra("sap_spn");
            spro_name = intent.getStringExtra("pro_name");
            sfirm_name = intent.getStringExtra("firm_name");
            spf_no = intent.getStringExtra("Pf_No");
            s_curr_date = intent.getStringExtra("curr_date");
            l_lat = intent.getStringExtra("l_lat");
            L_longi = intent.getStringExtra("L_longi");

            String date = String.valueOf(s_curr_date);
            SimpleDateFormat spf = new SimpleDateFormat("dd-MMM-yyyy");
            Date newDate = null;
            try {
                newDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf = new SimpleDateFormat("ddMMyyyy");
            try {
                date = spf.format(newDate);
            } catch (Exception e) {
            }
            int s = 0;
            try {
                Databaseutill db;
                GetData get;
                db = Databaseutill.getDBAdapterInstance(VisitRecord_sec_Activity.this);
                get = new GetData(db, VisitRecord_sec_Activity.this);
                s = get.GetSize();
            } catch (Exception e) {
            }
            String recordCount = "";
            s = s + 1;
            if (s < 9) {
                recordCount = "0" + String.valueOf(s);
            } else {
                recordCount = "" + String.valueOf(s + 1);
            }
            uniq_visit_no = util.getusername(this) + date;
            visit_no.setText("Visit Record Number :  " + uniq_visit_no);
            sac_no = intent.getStringExtra("ac_no");
            saddress = intent.getStringExtra("address");
            sbuss_act = intent.getStringExtra("buss_act");
            saadh_no = intent.getStringExtra("aadh_no");
            s_month_date = intent.getStringExtra("month_date");
            scon_no = intent.getStringExtra("con_no");
            sremarks = intent.getStringExtra("remarks");

            String save_images = util.getFirstImage(this);
            //  save_images = intent.getStringExtra("images");
            // save_images_sec = intent.getStringExtra("images_sec");
            String save_images_sec = util.getSECImage(this);


            String add_img = save_images + "," + save_images_sec;


            String save_latti = intent.getStringExtra("latti");
            String save_longi = intent.getStringExtra("longi");
            save_prolocation = intent.getStringExtra("pro_location");
            save_propreitor = intent.getStringExtra("propreitor");
            save_firm = intent.getStringExtra("firm");
            String save_visit_no = intent.getStringExtra("visit_no");
            if (save_visit_no != null) {
                visit_no.setText("Visit Record Number :  " + save_visit_no);
            }


            if (save_latti != null) {
                img_btn.setClickable(false);
                img_btn.setEnabled(false);
                pro_loca.setEnabled(false);
                submit.setVisibility(View.GONE);

                edt_lat.setText(save_latti);
            }
            if (save_longi != null) {
                edt_lang.setText(save_longi);
            }
            try {
                if (save_prolocation.equalsIgnoreCase("Shop")) {

                    pro_loca.post(new Runnable() {
                        @Override
                        public void run() {
                            pro_loca.setSelection(1);
                        }
                    });

                } else if (save_prolocation.equalsIgnoreCase("Factory")) {
                    pro_loca.post(new Runnable() {
                        @Override
                        public void run() {
                            pro_loca.setSelection(2);

                        }
                    });
                } else if (save_prolocation.equalsIgnoreCase("Godown")) {
                    pro_loca.post(new Runnable() {
                        @Override
                        public void run() {
                            pro_loca.setSelection(3);
                        }
                    });
                } else {
                    pro_loca.post(new Runnable() {
                        @Override
                        public void run() {
                            pro_loca.setSelection(4);
                            other_propert.setText(save_prolocation);
                            other_propert.setFocusable(false);
                            other_propert.setFocusableInTouchMode(false);
                            other_propert.setClickable(false);

                        }
                    });
                }
            } catch (Exception e) {
            }

            if (save_images != null && (!save_images.equalsIgnoreCase("")) || save_images_sec != null && (!save_images_sec.equalsIgnoreCase(""))) {
                String images[] = add_img.split(",");

                for (int i = 0; i < images.length; i++) {
                    imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);
                    thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
                    try {
                        byte[] decodedString = Base64.decode(images[i], Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        thumbnail.setImageBitmap(decodedByte);
                        mSelectedImagesContainer.addView(imageHolder);
                    } catch (Exception e) {

                    }

                }
            }
        }
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hit_Insert_api();
            }
        });
        Button logout_btn = (Button) findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });


        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.setImages(VisitRecord_sec_Activity.this, "");
                //   getImages();
                CaptureImageDialog();


            }
        });

        edt_lat.setText(l_lat);
        edt_lang.setText(L_longi);


//
//        Location location = appLocationService
//                .getLocation(LocationManager.GPS_PROVIDER);
//

        //double latitude = 37.422005;
        //double longitude = -122.084095

//        if (gps != null) {
//            double latitude = gps.getLatitude();
//            double longitude = gps.getLongitude();
//            LocationAddress locationAddress = new LocationAddress();
//            locationAddress.getAddressFromLocation(latitude, longitude,
//                    getApplicationContext(), new GeocoderHandler());
//        } else {
//
//        }


        text_view();
        KYC_spinner();
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//
//
//    }

    private void savePLocation_pref() {

        try {
            if (p_location.equalsIgnoreCase("Shop")) {

                pro_loca.post(new Runnable() {
                    @Override
                    public void run() {
                        pro_loca.setSelection(1);
                    }
                });

            } else if (p_location.equalsIgnoreCase("Factory")) {
                pro_loca.post(new Runnable() {
                    @Override
                    public void run() {
                        pro_loca.setSelection(2);
                    }
                });
            } else if (p_location.equalsIgnoreCase("Godown")) {
                pro_loca.post(new Runnable() {
                    @Override
                    public void run() {
                        pro_loca.setSelection(3);
                    }
                });
            } else if (p_location.equalsIgnoreCase("If other please specify")) {
                pro_loca.post(new Runnable() {
                    @Override
                    public void run() {
                        pro_loca.setSelection(4);


                    }
                });
            }
        } catch (Exception e) {
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            ArrayList<String> listhh = proadb.checkedIds;
            ArrayList<String> listyy = firmadapter.checkedIds;

            String Prolocation = pro_loca.getSelectedItem().toString();
            String other_location = other_propert.getText().toString();
            String pro = listhh.toString();

            String firm = listyy.toString();
            util.setProprietor(getApplication(), pro);
            util.setFirm(getApplication(), firm);
            util.setPLocation(getApplication(), Prolocation);
            util.setOtherLocation(getApplication(), other_location);
        } catch (Exception e) {

        }
//        for(int i=0;i<encodedString.size();i++)
//        {
//            encode_image=encode_image+encodedString.get(i);
//            encode_image=encode_image+",";
//        }
//        encode_image=encode_image.substring(0,encode_image.lastIndexOf(","));


        String encoded = "";

        if (encodedString.size() > 0) {

            for (int i = 0; i < encodedString.size(); i++) {
                encoded = encoded + encodedString.get(i);
                encoded = encoded + ",";
            }
            encoded = encoded.substring(0, encoded.lastIndexOf(","));


            util.setImages(getApplication(), encoded);

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        // util.setImages(getApplicationContext(),"");


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
                                // util.setPassword(context, "");
                                util.setusername(context, "");

                                Intent i = new Intent(VisitRecord_sec_Activity.this, Login_Activity.class);
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

    private void hit_Insert_api() {

        latti = edt_lat.getText().toString();
        longgi = edt_lang.getText().toString();

        try {
            listhh = proadb.checkedIds;
            listyy = firmadapter.checkedIds;
        } catch (Exception e) {
        }
        pro_location = pro_loca.getSelectedItem().toString();
        String other_pro_location = other_propert.getText().toString();
        if (latti.length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Please get latitude and longitude", Toast.LENGTH_SHORT).show();
        } else if (longgi.length() == 0) {

            Toast.makeText(getApplicationContext(),
                    "Please get latitude and longitude", Toast.LENGTH_SHORT).show();

        } else if (encodedString == null || encodedString.isEmpty()) {

            Toast.makeText(getApplicationContext(),
                    "Please select image", Toast.LENGTH_SHORT).show();


        } else if (encodedString.size() > 2) {

            Toast.makeText(getApplicationContext(),
                    "Please select 2 images only", Toast.LENGTH_SHORT).show();


        } else if (listyy == null || listyy.size() == 0) {

            Toast.makeText(getApplicationContext(),

                    "Please select firm", Toast.LENGTH_LONG).show();
        } else if (listhh == null || listhh.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Please select proprietor", Toast.LENGTH_LONG).show();
        } else if (pro_location.contains("--Select Visit Property Location --")) {
            Toast.makeText(getApplicationContext(), "Please select Property location", Toast.LENGTH_SHORT).show();
        } else if (pro_location.contains("If other please specify")) {
            if (other_pro_location.trim().equalsIgnoreCase("")) {

                Toast.makeText(getApplicationContext(),
                        "Please enter other Property ", Toast.LENGTH_SHORT).show();
            } else {
                pro_location = other_propert.getText().toString();

                if (isConnectingToInternet() == true) {
                    new performBackgroundTask1().execute();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No Internet Connection....", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            if (isConnectingToInternet() == true) {
                new performBackgroundTask1().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection....", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void sand_visitData() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "http://tempuri.org/InsertOBC";
        final String METHOD_NAME = "InsertOBC";

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
        }

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("NameVisitingOfficial", sname);
        request.addProperty("PFvisitingofficial", spf_no);
        request.addProperty("Salutation", ssap_spn);
        request.addProperty("NameProprietor", spro_name);
        request.addProperty("AadhaarNumber", saadh_no);
        request.addProperty("NameFirm", sfirm_name);
        request.addProperty("DateofVisit", s_curr_date);
        request.addProperty("AccountNumber", sac_no);
        request.addProperty("Address", saddress);
        request.addProperty("NatureofBusinessActivity", sbuss_act);
        request.addProperty("RunningSince", s_month_date);
        request.addProperty("ContactNo", scon_no);
        request.addProperty("Remarks", sremarks);

        String propri = util.getP_id(VisitRecord_sec_Activity.this);

        String pro_volve = "";

        try {
            JSONArray j = new JSONArray(propri);
            for (int i = 0; i < j.length(); i++) {
                JSONObject jsonObjecty = j.getJSONObject(i);
                String pro_str = jsonObjecty.getString("P_Id");
                pro_volve = pro_volve + pro_str + ",";
            }
            pro_volve = pro_volve.substring(0, pro_volve.lastIndexOf(","));
            request.addProperty("KYCProprietor_ID", pro_volve);

        } catch (Exception e) {
            e.getCause();
        }
        String firm = util.getKF_ID(VisitRecord_sec_Activity.this);
        String firm_volue = "";
        try {
            JSONArray j = new JSONArray(firm);
            for (int i = 0; i < j.length(); i++) {
                JSONObject jsonObjecty = j.getJSONObject(i);
                String firm_str = jsonObjecty.getString("Kf_ID");
                firm_volue = firm_volue + firm_str + ",";
            }
            firm_volue = firm_volue.substring(0, firm_volue.lastIndexOf(","));
            request.addProperty("KYCFirm_ID", firm_volue);

        } catch (Exception e) {
            e.getCause();
        }
        request.addProperty("UserId", util.getUserId(getApplication()));
        request.addProperty("Email", util.getEmail_id(getApplication()));
        request.addProperty("Images", img1);
        request.addProperty("Images1", img2);
        request.addProperty("Longitude", longgi);
        request.addProperty("Latitude", latti);
        request.addProperty("PLocation", pro_location);
        request.addProperty("SolidID", util.getSOLID_value(VisitRecord_sec_Activity.this));
        request.addProperty("UniqueNo", uniq_visit_no);
        request.addProperty("ChkPDF", "1");


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);//timeout 30 sec
        androidHttpTransport.debug = true;

      //  Log.e("InsertOBC input",request.toString());
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable) envelope.bodyIn;
           // Log.d("HTTP REQUEST ", androidHttpTransport.requestDump);
          //  Log.d("HTTP RESPONSE", androidHttpTransport.responseDump);
            String a = androidHttpTransport.requestDump;
            Object results = (Object) envelope.getResponse();
            String resultstring = results.toString();
          //  Log.e("InsertOBC_output",resultstring);
          //  Log.e("result", resultstring);
            for (int j = 0; j < ks.getPropertyCount(); j++) {
                ks.getProperty(j);
            }
            receivedString = ks.toString();
            String Jsonstring = receivedString;
            if (!Jsonstring.contains("ID")) {
                subMitdataLimit = "";
                String news = Jsonstring.substring(Jsonstring.indexOf("["));
                String n1 = news;
                JSONArray jArray = new JSONArray(n1);
                for (int k = 0; k < (jArray.length()); k++) {
                    JSONObject json_obj = jArray.getJSONObject(k);
                    responsestatus = json_obj.getString("Status");

                }
            } else {
                subMitdataLimit = "99";
            }
        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
           // Log.e("insertOBC_exception",e.getMessage());

        }


    }

    private void text_view() {
        TextView nametxt = (TextView) findViewById(R.id.lat_txt);
        String text = "<font color=#000>Latitude</font> <font color=#cc0029>*</font>";
        nametxt.setText(Html.fromHtml(text));
        TextView nametx = (TextView) findViewById(R.id.lang_txt);
        String tex = "<font color=#000>Longitude</font> <font color=#cc0029>*</font>";
        nametx.setText(Html.fromHtml(tex));

    }

    private void KYC_spinner() {

        List categories = new ArrayList();
        categories.add("--Select Visit Property Location --");
        categories.add("Shop");
        categories.add("Factory");
        categories.add("Godown");
        categories.add("If other please specify");

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pro_loca.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (item.equals("If other please specify")) {
            other_propert.setVisibility(view.VISIBLE);
        } else {
            other_propert.setVisibility(View.GONE);
        }
    }

    public void onNothingSelected(AdapterView adapterView) {
    }

    private void getImages() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.blue)
                .setCameraButtonColor(R.color.orange)
                .setSelectionLimit(2)    // set photo selection limit. Default unlimited selection.
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, INTENT_REQUEST_GET_N_IMAGES);
    }

    private void getNImages() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
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

            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                // mMediaImages.clear();


                String path = imageStoragePath;
                Uri uri = Uri.parse(path);
                int orientation = Utility.getExifOrientation(path);
                String id = Utility.getUniqueId();
                mMediaImages.add(new Image(uri, orientation, id));

                // showMedia();
                showMedia_New();

            } else if (requestCode == GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
                if (intent != null) {

                    //The array list has the image paths of the selected images
                    ArrayList<com.darsh.multipleimageselect.models.Image> images = intent.getParcelableArrayListExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_IMAGES);
                    //  mMediaImages.clear();

                    for (int i = 0; i < images.size(); i++) {
                        String path = images.get(i).path;
                        Uri uri = Uri.parse(path);
                        int orientation = Utility.getExifOrientation(path);
                        String id = String.valueOf(UniqueID.get());
                        mMediaImages.add(new Image(uri, orientation, id));


                    }

                    //  showMedia();
                    showMedia_New();

                }
            } else if (requestCode == INTENT_REQUEST_GET_IMAGES || requestCode == INTENT_REQUEST_GET_N_IMAGES) {
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
                        String id = Utility.getUniqueId();
                        mMediaImages.add(new Image(uris[i], orientations[i], id));

                    }

                    showMedia();
                }
            }
        }
    }

    private void showMedia() {

        mSelectedImagesContainer.removeAllViews();

        Iterator<Image> iterator = mMediaImages.iterator();
        String media_size = mMediaImages.size() + "";

        util.setMEdiacount(getApplication(), media_size);
        ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
        while (iterator.hasNext()) {
            Image image = iterator.next();


            try {
                if (mMedia.size() >= 1) {
                    mSelectedImagesContainer.setVisibility(View.VISIBLE);
                }

            } catch (NullPointerException ne) {

            }

            imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);
            thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            String path = image.mUri.toString();


            Bitmap bb = decodeFile(new File(path), 0, 0);
            if (bb != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy,HH:mm");
                String currentDateandTime = sdf.format(new Date());

                BitmapDrawable bitmapDrawable = SetDATE_Time(bb, currentDateandTime);
                Bitmap bitmap = bitmapDrawable.getBitmap();


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

            mSelectedImagesContainer.addView(imageHolder);

            // set the dimension to correctly
            // show the image thumbnail.
            int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }
    }

    private void showMedia_New() {
        ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
        encodedString.clear();
        mSelectedImagesContainer.removeAllViews();
        Iterator<Image> iterator = mMediaImages.iterator();
        String media_size = mMediaImages.size() + "";
        util.setMEdiacount(getApplication(), media_size);

        while (iterator.hasNext()) {
            final Image image = iterator.next();
            try {
                if (mMedia.size() >= 1) {
                    mSelectedImagesContainer.setVisibility(View.VISIBLE);
                }

            } catch (NullPointerException ne) {

            }


            imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout_1, null);
            thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            final ImageView imgvw_remove = (ImageView) imageHolder.findViewById(R.id.img_remove);
            imgvw_remove.setTag(image.id);
            imgvw_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = String.valueOf(imgvw_remove.getTag());
                    Iterator<Image> iterator = mMediaImages.iterator();
                    while (iterator.hasNext()) {
                        final Image image = iterator.next();
                        if (image.id.equals(id)) {
                            mMediaImages.remove(image);
                            // iterator.remove();

                            showMedia_New();
                            break;

                        }
                    }
                }
            });


            /*for bitmap*/
            String path = image.mUri.toString();
            Bitmap bb = decodeFile(new File(path), 0, 0);
            if (bb != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy,HH:mm");
                String currentDateandTime = sdf.format(new Date());

                BitmapDrawable bitmapDrawable = SetDATE_Time(bb, currentDateandTime);
                Bitmap bitmap = bitmapDrawable.getBitmap();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                String encode_String = Base64.encodeToString(b, Base64.DEFAULT);

                encodedString.add(encode_String);

                thumbnail.setImageBitmap(bitmap);
            }
            /**/


            //  imageFetcher.loadImage(image.mUri, thumbnail, image.mOrientation);
          /*  Glide
                    .with(VisitRecord_sec_Activity.this)
                    .load(new File(image.mUri.getPath()))     // Uri of the picture
                    .into(thumbnail);*/


            mSelectedImagesContainer.addView(imageHolder);


        /*    int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                img_btn.setEnabled(true);
            }
        }
    }

    private void FirmListdata() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "http://tempuri.org/FatchFirm";
        final String METHOD_NAME = "FatchFirm";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


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
            list2 = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String firm_id = jsonObject.getString("Kf_ID");
                String firm_name = jsonObject.getString("KF_Name");

                HashMap<String, String> hmap = new HashMap<>();
                hmap.put("Kf_ID", firm_id);
                hmap.put("KF_Name", firm_name);

                list2.add(hmap);
            }

        } catch (Exception e) {
            catchhandl++;
            e.printStackTrace();
        }


    }

    private void ProprietorListData() {
        catchhandl = 0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Utility.base_url;
        final String SOAP_ACTION = "  http://tempuri.org/FatchProprietor";
        final String METHOD_NAME = "FatchProprietor";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


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
            list1 = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String pro_id = jsonObject.getString("P_Id");
                String pro_name = jsonObject.getString("KYCProprietorName");


                HashMap<String, String> hmap = new HashMap<>();
                hmap.put("P_Id", pro_id);
                hmap.put("KYCProprietorName", pro_name);
                list1.add(hmap);

            }


        } catch (Exception e) {
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

    public void internetAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VisitRecord_sec_Activity.this);
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
        ConnectivityManager connectivity = (ConnectivityManager) VisitRecord_sec_Activity.this
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

    private BitmapDrawable SetDATE_Time(Bitmap bitmap, String text) {

      /*  Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.imgpsh_fullsiz)
                .copy(Bitmap.Config.ARGB_8888, true);*/
        BitmapDrawable newBitmap = null;

        try {


            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);


            Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setTypeface(tf);
            paint.setTextAlign(Paint.Align.CENTER);

            paint.setTextSize(convertToPixels(VisitRecord_sec_Activity.this, 14));

            Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);

            Canvas canvas = new Canvas(mutableBitmap);

            //If the text is bigger than the canvas , reduce the font size
            if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
                paint.setTextSize(convertToPixels(VisitRecord_sec_Activity.this, 7));        //Scaling needs to be used for different dpi's

            //Calculate the positions
            int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

            //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

            canvas.drawText(text, xPos, yPos, paint);

            newBitmap = new BitmapDrawable(getResources(), mutableBitmap);
        } catch (Exception e) {
            e.getMessage();
        }
        return newBitmap;
    }

//    private class GeocoderHandler extends Handler {
//        @Override
//        public void handleMessage(Message message) {
//            String locationAddress;
//            switch (message.what) {
//                case 1:
//                    Bundle bundle = message.getData();
//                    locationAddress = bundle.getString("address");
//                    break;
//                default:
//                    locationAddress = null;
//            }
//            Toast.makeText(getApplicationContext(),
//                    "Name of Location" +locationAddress, Toast.LENGTH_LONG).show();
//
//           // tvAddress.setText(locationAddress);
//        }
//    }

    public void CaptureImageDialog() {
        final Dialog dialog = new Dialog(VisitRecord_sec_Activity.this);

        dialog.setContentView(R.layout.openattachmentdilog_new);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        LinearLayout cameralayout = (LinearLayout) dialog
                .findViewById(R.id.cameralayout);
        LinearLayout gallarylayout = (LinearLayout) dialog
                .findViewById(R.id.gallarylayout);

        ImageView crosse = (ImageView) dialog
                .findViewById(R.id.close);


        crosse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });

        cameralayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


                //  if (CameraUtils.checkPermissions(getApplicationContext())) {
                captureImage();
                ///  } else {
                //       requestCameraPermission(MEDIA_TYPE_IMAGE);
                //  }


            }
        });
        gallarylayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitRecord_sec_Activity.this, AlbumSelectActivity.class);
                intent.putExtra(com.darsh.multipleimageselect.helpers.Constants.INTENT_EXTRA_LIMIT, 2);
                startActivityForResult(intent, GALLERY_CAPTURE_IMAGE_REQUEST_CODE);

                dialog.dismiss();

            }
        });


        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
            }
        });

    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        try {
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }catch (Exception e){
            e.getMessage();
        }
        }


    class performBackgroundTask1 extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(VisitRecord_sec_Activity.this);

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
                    if (subMitdataLimit.equals("99")) {

                        util.showAlert(VisitRecord_sec_Activity.this, "Data can not submit more than 99 records per day");

                    } else {

                        Toast.makeText(getApplicationContext(),
                                "" + responsestatus, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(VisitRecord_sec_Activity.this, Sol_ID_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }
                } else {

                    Toast.makeText(getApplicationContext(),
                            "Network Error...Please try again", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Network Error...", Toast.LENGTH_LONG).show();
                Dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            sand_visitData();
            return null;
        }
    }

    class Get_DataPropriter extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(VisitRecord_sec_Activity.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            //  FirmListdata();
            ProprietorListData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (catchhandl == 0) {
                proadb = new ProprietorAdapter(VisitRecord_sec_Activity.this, list1, save_propreitor, pro);
                firm_list.setAdapter(proadb);
                new ListAndGirdShow().sethight(firm_list);
            } else {

                Toast.makeText(getApplicationContext(),
                        "Network Error...Please Try Again", Toast.LENGTH_LONG).show();
                Dialog.dismiss();

            }
            if (Dialog.isShowing()) {
                Dialog.dismiss();

            }
        }
    }

    class Get_Data extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(VisitRecord_sec_Activity.this);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            FirmListdata();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (catchhandl == 0) {
                firmadapter = new FirmAdapter(VisitRecord_sec_Activity.this, list2, save_firm, firm);
                propri_list.setAdapter(firmadapter);
                new ListAndGirdShow().sethight(propri_list);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Network Error...Please Try Again", Toast.LENGTH_LONG).show();
             try {
                 Dialog.dismiss();
             }catch (Exception e){
                 e.getMessage();
             }

            }
            try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }
            }catch (Exception e){
                e.getMessage();
            }


        }
    }

}



