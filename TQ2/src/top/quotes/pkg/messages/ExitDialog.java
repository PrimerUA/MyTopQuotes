package top.quotes.pkg.messages;

import top.quotes.pkg.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;

public class ExitDialog {

    public static AlertDialog showExitDialog(final Activity activity) {
	AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	builder.setTitle(R.string.exit_title);
	builder.setMessage(R.string.exit_text);
	builder.setIcon(R.drawable.ic_launcher);
	builder.setCancelable(true);
	builder.setNeutralButton(R.string.rate_app, new OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(activity.getString(R.string.app_market_url)));
		activity.startActivity(openUrlIntent);
	    }
	});
	builder.setPositiveButton(android.R.string.yes, new OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		activity.finish();
	    }
	});
	builder.setNegativeButton(android.R.string.no, new OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	    }
	});
	return builder.show();
    }
}
