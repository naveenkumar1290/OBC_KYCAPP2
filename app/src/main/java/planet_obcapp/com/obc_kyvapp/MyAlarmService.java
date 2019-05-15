package planet_obcapp.com.obc_kyvapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import planet_obcapp.com.obc_kyvapp.App_utils.Utils;

public class MyAlarmService extends Service

{

	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() 
	{
//		try{
//			Utils.generateNotification(getApplicationContext());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}