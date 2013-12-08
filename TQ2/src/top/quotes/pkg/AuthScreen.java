package top.quotes.pkg;

import top.quotes.pkg.entity.User;
import top.quotes.pkg.register.Executor;
import android.app.Activity;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;

public class AuthScreen extends SherlockActivity implements OnClickListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private SignInButton loginButton;
	private PlusClient plusClient;

	private ImageView authImage;
	private LinearLayout authLayout;

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_screen);

		initScreen();
	}

	private void initScreen() {
		authImage = (ImageView) findViewById(R.id.auth_image);
		authLayout = (LinearLayout) findViewById(R.id.auth_layout);
		
		prefs = getSharedPreferences("topquotes", 0);

		plusClient = new PlusClient.Builder(this, this, this)
				.setScopes(Scopes.PLUS_LOGIN)
				.setVisibleActivities("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/BuyActivity").build();

		loginButton = (SignInButton) findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);

		plusClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult((Activity) this,
						REQUEST_CODE_RESOLVE_ERR);
				authImage.setVisibility(View.GONE);
				authLayout.setBackgroundResource(R.drawable.w1);
			} catch (IntentSender.SendIntentException e) {
				plusClient.connect();
			}
		} else {
			Toast.makeText(this,
					"Connection failed. Error code: " + result.getErrorCode(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, getString(R.string.google_connected) + "\n" + plusClient.getCurrentPerson().getDisplayName(),
				Toast.LENGTH_SHORT).show();

		User user = new User();
		user.setName(plusClient.getCurrentPerson().getDisplayName());
		user.setEmail(plusClient.getAccountName());

		new Executor().sendUSerInfo(user);

		User.setLoggedIn(true);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("loggedIn", true);
		editor.commit();

		finish();
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, getString(R.string.google_disconnected),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		if (!plusClient.isConnected()) {
			plusClient.connect();
		}
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.login_text), Toast.LENGTH_SHORT)
				.show();
	}

}
