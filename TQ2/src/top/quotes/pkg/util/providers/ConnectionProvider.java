package top.quotes.pkg.util.providers;

import top.quotes.pkg.R;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionProvider {

	public static boolean isConnectionAvailable(Context context) {
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}

	public void showConnectivityMessage(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.registration_failed_title);
		builder.setMessage(R.string.connection_check_text);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setCancelable(true);
		builder.show();
	}

}
