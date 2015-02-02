package com.rod.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartServiceAtBoot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(!TimeService.ativo){
			Intent service = new Intent(context,TimeService.class);
			context.startService(service);
		}
	}

}
