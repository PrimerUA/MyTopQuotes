package top.quotes.pkg.util;

import top.quotes.pkg.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class AppRater {
	// private final static String APP_TITLE = "Top Quotes";
	private final static String APP_PNAME = "top.quotes.pkg";

	private final static int DAYS_UNTIL_PROMPT = 1;
	private final static int LAUNCHES_UNTIL_PROMPT = 3;

	public static void app_launched(Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get date of first launch
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}

		// Wait at least n days before opening
		if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
				showRateDialog(mContext, editor);
			}
		}

		editor.commit();
	}

	public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		dialog.setTitle(R.string.do_rate_it);
		dialog.setCancelable(false);
		dialog.setIcon(R.drawable.ic_launcher);
		dialog.setMessage(mContext.getResources().getString(R.string.please_rate));

		dialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getResources().getString(R.string.do_rate_it), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
				dialog.dismiss();
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getResources().getString(R.string.remind), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getResources().getString(R.string.lazy), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
				dialog.dismiss();
			}
		});
		dialog.show();

	}
}