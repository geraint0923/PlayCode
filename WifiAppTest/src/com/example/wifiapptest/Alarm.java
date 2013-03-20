package com.example.wifiapptest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class Alarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//MainActivity activity = (MainActivity)context;
			
		System.out.println("come on alarm!"+MainActivity.instance);

		IBinder binder = this.peekService(context, new Intent("com.example.TestService"));
		ITestService service = ITestService.Stub.asInterface(binder);
		if(service == null) {
			System.out.println("null service");
			return;
		}
		try {
			service.tick();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}