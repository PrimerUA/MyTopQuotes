package top.quotes.pkg.util.controllers;

import java.util.Calendar;

import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.util.DailyNotificationReceiver;
import top.quotes.pkg.util.PreferencesLoader;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class DailyNotificationController {

	private static boolean isSet;

	private static AlarmManager alarmManager;
	private static PendingIntent pendingIntent;
	private static NotificationManager notificationManager;

	public static void initNotification(Context context) {
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		pendingIntent = PendingIntent.getBroadcast(context, ConstantsFacade.NOTIFICATION_ID, new Intent(context, DailyNotificationReceiver.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		if (PreferencesLoader.isDaily()) {
			if (!isSet) {
				isSet = startAlarmIntent();
			}
		} else {
			isSet = stopAlarmIntent();
		}
	}

	private static boolean startAlarmIntent() {
		Calendar calendar = Calendar.getInstance();
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
		return true;
	}

	private static boolean stopAlarmIntent() {
		alarmManager.cancel(pendingIntent);
		notificationManager.cancel(ConstantsFacade.NOTIFICATION_ID);
		return false;
	}

}
