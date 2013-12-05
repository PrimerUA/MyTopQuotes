package top.quotes.pkg.util;

import top.quotes.pkg.messages.DailyNotification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DailyNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		DailyNotification.show(context);
	}

}
