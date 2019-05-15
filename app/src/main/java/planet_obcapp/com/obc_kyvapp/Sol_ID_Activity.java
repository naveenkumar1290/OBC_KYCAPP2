package planet_obcapp.com.obc_kyvapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import cn.pedant.SweetAlert.SweetAlertDialog;
import planet_obcapp.com.obc_kyvapp.App_utils.util;


public class Sol_ID_Activity extends AppCompatActivity implements Animation.AnimationListener {


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_sol_id);
        Image_Animation();
        TextView sol_id = (TextView) findViewById(R.id.sol);
        sol_id.setText("Welcome to SOL ID " + util.getSOLID_value(this));
        Button logout_btn = (Button) findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        Button edt_visit = (Button) findViewById(R.id.visit_roc);

        if (isConnectingToInternet() == true) {
            new checkVersionUpdate().execute();
        } else {
            Toast.makeText(getApplicationContext(),
                    "No Internet Connection....", Toast.LENGTH_LONG).show();
        }

        edt_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Sol_ID_Activity.this, VisitRecordFirst_Activity.class);
                util.setFirm(getApplication(), "");
                util.setProprietor(getApplication(), "");
                util.setPLocation(getApplication(), "");
                util.setOtherLocation(getApplication(), "");
                util.setImages(getApplication(), "");
                util.setSECImage(getApplication(), "");
                util.setFirstImage(getApplication(), "");
                startActivity(mainIntent);
            }
        });

        Button edt_visit_history = (Button) findViewById(R.id.visit_hist);
        edt_visit_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.setFirm(getApplication(), "");
                util.setProprietor(getApplication(), "");
                util.setPLocation(getApplication(), "");
                util.setOtherLocation(getApplication(), "");
                util.setImages(getApplication(), "");
                util.setSECImage(getApplication(), "");
                util.setFirstImage(getApplication(), "");
                Intent mainIntent = new Intent(Sol_ID_Activity.this, VisitRecord_Histary.class);
                startActivity(mainIntent);
            }
        });




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
                                // util.setPassword(context,"");
                                util.setusername(context, "");

                                Intent i = new Intent(Sol_ID_Activity.this, Login_Activity.class);
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


    private void Image_Animation() {

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
                        finish();
                        //close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) Sol_ID_Activity.this
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



    private class checkVersionUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Sol_ID_Activity.this);
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
            final String SOAP_ACTION = "https://tempuri.org/GetVersion";
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
                PackageManager manager = Sol_ID_Activity.this.getPackageManager();
                PackageInfo info = manager.getPackageInfo(Sol_ID_Activity.this.getPackageName(), 0);
                String versionName = info.versionName;
                String nversionName = ver;
                if (nversionName == null) {
                    nversionName = "0";
                }
                Double retuenvl = Double.parseDouble(nversionName);
                if (Double.parseDouble(versionName) < retuenvl) {
                    // if (true) {
                    try {
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Sol_ID_Activity.this, SweetAlertDialog.WARNING_TYPE);
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
}
