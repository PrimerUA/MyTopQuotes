package top.quotes.pkg;

import top.quotes.pkg.entity.User;
import top.quotes.pkg.util.PreferencesLoader;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.providers.ConnectionProvider;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class PreferencesScreen extends SherlockActivity implements OnCheckedChangeListener {

	private LinearLayout contentLayout;

	private RadioButton themePinkRadio;
	private RadioButton themeWhiteRadio;
	private RadioButton themeOrangeRadio;

	private RadioButton languageRusRadio;
	private RadioButton languageEngRadio;

	private CheckBox dailyCheckBox;

	private Button moreButton;
	private Button rateButton;
	private Button logoutButton;

	private TextView userText;
	private Context context;

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences_screen);

		initScreen();

		loadPreferencesOnScreen();
	}

	private void loadPreferencesOnScreen() {
		LanguageController language = PreferencesLoader.getLanguage();
		int theme = PreferencesLoader.getTheme();
		Boolean daily = PreferencesLoader.isDaily();

		if (language == LanguageController.RUS) {
			languageRusRadio.setChecked(true);
		} else {
			languageEngRadio.setChecked(true);
		}

		if (theme == 0) {
			themePinkRadio.setChecked(true);
		} else if (theme == 1) {
			themeWhiteRadio.setChecked(true);
		} else {
			themeOrangeRadio.setChecked(true);
		}

		dailyCheckBox.setChecked(daily);
	}

	private void initScreen() {
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.prefs_title));
		
		context = this;
		prefs = getSharedPreferences("topquotes", 0);

		contentLayout = (LinearLayout) findViewById(R.id.PreferencesScreen_contentLayout);

		userText = (TextView) findViewById(R.id.PreferencesScreen_userText);
		userText.setText(getString(R.string.loggined_as) + " " + User.getInstance().getName() + ", " +  User.getInstance().getEmail());
		
		moreButton = (Button) findViewById(R.id.PreferencesScreen_moreButton);
		rateButton = (Button) findViewById(R.id.PreferencesScreen_rateButton);
		logoutButton = (Button) findViewById(R.id.PreferencesScreen_exitButton);

		themePinkRadio = (RadioButton) findViewById(R.id.PreferencesScreen_themeRadioPink);
		themeWhiteRadio = (RadioButton) findViewById(R.id.PreferencesScreen_themeRadioWhite);
		themeOrangeRadio = (RadioButton) findViewById(R.id.PreferencesScreen_themeRadioOrange);

		languageRusRadio = (RadioButton) findViewById(R.id.PreferencesScreen_languageRadioRussian);
		languageEngRadio = (RadioButton) findViewById(R.id.PreferencesScreen_languageRadioEnglish);

		dailyCheckBox = (CheckBox) findViewById(R.id.PreferencesScreen_dailyCheckBox);

		OnCheckedChangeListener themeChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (themePinkRadio.isChecked()) {
					PreferencesLoader.setTheme(0);
					contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
				} else if (themeWhiteRadio.isChecked()) {
					PreferencesLoader.setTheme(1);
					contentLayout.setBackgroundResource(R.drawable.quote_border_white);
				} else {
					PreferencesLoader.setTheme(2);
					contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
				}
			}
		};

		themeWhiteRadio.setOnCheckedChangeListener(themeChangeListener);
		themePinkRadio.setOnCheckedChangeListener(themeChangeListener);

		OnCheckedChangeListener languageChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (languageRusRadio.isChecked()) {
					PreferencesLoader.setLanguage(LanguageController.RUS);
				} else {
					PreferencesLoader.setLanguage(LanguageController.ENG);
				}
			}
		};

		languageRusRadio.setOnCheckedChangeListener(languageChangeListener);
		languageEngRadio.setOnCheckedChangeListener(languageChangeListener);

		dailyCheckBox.setOnCheckedChangeListener(this);

		moreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(getApplicationContext(), MoreScreen.class);
				startActivity(browserIntent);
			}
		});

		rateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(R.string.rate_title_text);
				builder.setMessage(R.string.rate_content_text);
				builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_market_url)));
						startActivity(browserIntent);
					}
				});
				builder.setCancelable(false);
				builder.show();
			}
		});

		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean("loggedIn", false);
				editor.commit();
				if (ConnectionProvider.isConnectionAvailable(PreferencesScreen.this)) {
					startActivity(new Intent(PreferencesScreen.this, AuthScreen.class));
				}
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		PreferencesLoader.setDaily(isChecked);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		default:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
