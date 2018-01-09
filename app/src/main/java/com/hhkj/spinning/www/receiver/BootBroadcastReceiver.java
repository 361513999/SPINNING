package com.hhkj.spinning.www.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.service.SpinningService;

public class BootBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())||"com.spinning.destroy".equals(intent.getAction()))
		{
			P.c(intent.getAction()+"-------------");
			Intent startServiceIntent = new Intent(context, SpinningService.class);
			startServiceIntent.putExtra("watch","");
			startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(startServiceIntent);
			//
		}
	}
}
