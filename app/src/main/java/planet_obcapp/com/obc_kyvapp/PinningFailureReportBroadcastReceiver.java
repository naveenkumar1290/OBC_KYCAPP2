package planet_obcapp.com.obc_kyvapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.datatheorem.android.trustkit.reporting.BackgroundReporter;

import java.io.Serializable;

/**
 * Class that provides an example broadcast receiver
 *
 * <p>
 *     Applications using TrustKit can listen for local broadcasts and receive the same report that
 *     would be sent to the report_url.
 * </p>
 **/
class PinningFailureReportBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
     //   Serializable result = intent.getSerializableExtra(BackgroundReporter.EXTRA_REPORT);
    //    Log.i(Splash_activity.DEBUG_TAG, result.toString());
        Log.i(Splash_activity.DEBUG_TAG, "");
    }
}
