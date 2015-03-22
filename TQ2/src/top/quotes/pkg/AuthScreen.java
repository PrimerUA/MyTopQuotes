package top.quotes.pkg;

import top.quotes.pkg.entity.User;
import top.quotes.pkg.util.PreferencesLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class AuthScreen extends Activity implements OnClickListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private SignInButton loginButton;
	private PlusClient plusClient;

	private ImageView authImage;
	private LinearLayout authLayout;
	
	private ProgressDialog myProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_screen);

		initScreen();
	}

	private void initScreen() {
		myProgressDialog = new ProgressDialog(this);
		authImage = (ImageView) findViewById(R.id.auth_image);
		authLayout = (LinearLayout) findViewById(R.id.auth_layout);

		plusClient = new PlusClient.Builder(this, this, this).setScopes(Scopes.PLUS_LOGIN)
				.setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity").build();

		loginButton = (SignInButton) findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);

		plusClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult((Activity) this, REQUEST_CODE_RESOLVE_ERR);
				authImage.setVisibility(View.GONE);
				authLayout.setBackgroundResource(R.drawable.w1);
				myProgressDialog.dismiss();
			} catch (IntentSender.SendIntentException e) {
				plusClient.connect();
			}
		} else {
			Toast.makeText(this, "Connection failed. Error code: " + result.getErrorCode(), Toast.LENGTH_SHORT).show();
			myProgressDialog.dismiss();
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, getString(R.string.google_connected) + "\n" + plusClient.getCurrentPerson().getDisplayName(), Toast.LENGTH_SHORT).show();

		User.getInstance().setName(plusClient.getCurrentPerson().getDisplayName());
		User.getInstance().setEmail(plusClient.getAccountName());

		ParseUser user = new ParseUser();
		user.setUsername(User.getInstance().getName());
		user.setEmail(User.getInstance().getEmail());
		user.setPassword("pass");
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				ParseAnonymousUtils.logIn(new LogInCallback() {
					@Override
					public void done(ParseUser user, ParseException e) {
						if (e == null) {
							User.getInstance().setLoggedIn(true);
							PreferencesLoader.getInstance().saveUserData();
							finish();
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(AuthScreen.this);
							builder.setTitle(R.string.registration_failed_title);
							builder.setMessage(getString(R.string.connection_check_text) + " Error: " + e.getMessage());
							builder.setIcon(R.drawable.ic_launcher);
							builder.setCancelable(true);
							builder.show();
						}
						myProgressDialog.dismiss();
					}
				});
			}
		});
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, getString(R.string.google_disconnected), Toast.LENGTH_SHORT).show();
		myProgressDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		if (!plusClient.isConnected()) {
			plusClient.connect();
			myProgressDialog = ProgressDialog.show(AuthScreen.this, getString(R.string.connection), getString(R.string.connection_auth), true);
		}
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.login_text), Toast.LENGTH_SHORT).show();
	}

}
