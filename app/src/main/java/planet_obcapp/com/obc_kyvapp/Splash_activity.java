package planet_obcapp.com.obc_kyvapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.scottyab.rootbeer.RootBeer;

import planet_obcapp.com.obc_kyvapp.App_utils.MyUtils;
import planet_obcapp.com.obc_kyvapp.App_utils.RootUtil;

import static planet_obcapp.com.obc_kyvapp.AlarmReceiver.EXTRA_REMINDER;


/**
 * Created by planet on 3/15/17.
 */

public class Splash_activity extends Activity {
    protected static final String DEBUG_TAG = "TrustKit-Demo";
    //private static final PinningFailureReportBroadcastReceiver pinningFailureReportBroadcastReceiver = new PinningFailureReportBroadcastReceiver();
    private String from = "NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_activity);
        CheckDeviceRootStatus();



        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getStringExtra("from");
        }


        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Splash_activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                alertBox();

            } else {

                splash();
            }

        } else {

            splash();
        }
    }

    private void CheckDeviceRootStatus() {
        //Check device is rooted or not!
        RootBeer rootBeer = new RootBeer(Splash_activity.this);
        if (rootBeer.isRooted()) {
            //we found indication of root
            Toast.makeText(getApplicationContext(), "This device is rooted!\nThis application can't be used on this device.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (rootBeer.isRootedWithoutBusyBoxCheck()) {
            //we found indication of root
            Toast.makeText(getApplicationContext(), "This device is rooted!\nThis application can't be used on this device.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

     /*   if (RootUtil.isDeviceRooted()) {
            //we found indication of root
            Toast.makeText(getApplicationContext(), "This device is rooted!\nThis application can't be used on this device.", Toast.LENGTH_SHORT).show();
            finish();
            return;

        }*/


//Check device is rooted or not!
    }

    private void splash() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences("new", MODE_PRIVATE);
                boolean isDeactivated = pref.getBoolean("Deactivate", false);

                SharedPreferences pref1 = getSharedPreferences("new1", MODE_PRIVATE);
                boolean isDeactivated1 = pref1.getBoolean("Deactivate1", false);
                if (isDeactivated1 && from != null && from.equalsIgnoreCase(EXTRA_REMINDER)) {
                    Intent mainIntent = new Intent(Splash_activity.this, Visit_Record_Histry_CRG.class);
                    startActivity(mainIntent);
                    finish();

                } else if (isDeactivated1) {
                    Intent mainIntent = new Intent(Splash_activity.this, CRG_Visit_MAin.class);
                    startActivity(mainIntent);
                    finish();
                } else if (isDeactivated) {
                    Intent mainIntent = new Intent(Splash_activity.this, Sol_ID_Activity.class);
                    startActivity(mainIntent);
                    finish();

                } else {
                    Intent mainIntent = new Intent(Splash_activity.this, Login_Activity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }

        }, 2000);
    }

    private void alertBox() {
        AlertDialog.Builder alert_box = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        alert_box.setMessage("Please Allow Location Persmission");

        alert_box.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                requestLocationPermission();
            }
        });

        alert_box.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        alert_box.show();

    }

    private void requestLocationPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(Splash_activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            splash();
        } else {
            Toast.makeText(this, "LOCATION permission was NOT granted.", Toast.LENGTH_SHORT).show();
            alertBox();
        }
    }


}