package planet_obcapp.com.obc_kyvapp.App_utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


import planet_obcapp.com.obc_kyvapp.CRG_Visit_MAin;
import planet_obcapp.com.obc_kyvapp.R;
import planet_obcapp.com.obc_kyvapp.Visit_Record_Histry_CRG;

public class Utils {

	public static NotificationManager mManager;

	@SuppressWarnings("static-access")
	public static void generateNotification(Context context){

		Intent intent = new Intent(context, Visit_Record_Histry_CRG.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder b = new NotificationCompat.Builder(context);

		b.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_ALL)
				.setWhen(System.currentTimeMillis())
				.setSmallIcon(R.mipmap.app_logo)
				.setTicker("Oriental CRG")
				.setContentTitle("Oriental CRG")
				.setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
				.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
				.setContentIntent(contentIntent)
				.setContentInfo("Info");
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, b.build());


//
//		mManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//		Intent intent1 = new Intent(context,CRG_Visit_MAin.class);
//		Notification notification = new Notification(R.mipmap.app_logo,"This is a test message!", System.currentTimeMillis());
//		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context,0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//	//	notification.setLatestEventInfo(context, "Oriental CRG", "This is a test message!", pendingNotificationIntent);
//		mManager.notify(0, notification);
	}
}
