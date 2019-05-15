package planet_obcapp.com.obc_kyvapp;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.datatheorem.android.trustkit.TrustKit;

import io.fabric.sdk.android.Fabric;

/*
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;*/
/*
@ReportsCrashes(formKey = "",
        mailTo = "marif@planetecomsolutions.com", // my email here
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text
)*/
public class CrashReport extends Application {
    private static Context cont;

    public static Context getCont() {
        return cont;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SSLPinning();
        try {
            //   ACRA.init(this);
        } catch (Exception er) {
            er.getMessage();
        }

        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //      MultiDex.install(this);
    }

    private void SSLPinning() {
        try {
            // Initialize TrustKit with the default path for the Network Security Configuration which is
            // res/xml/network_security_config.xml
            TrustKit.initializeWithNetworkSecurityConfiguration(this);
            // Connect to the URL with valid pins - this connection will succeed
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
