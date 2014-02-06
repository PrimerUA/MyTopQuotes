package top.quotes.pkg;

import top.quotes.pkg.entity.User;
import top.quotes.pkg.entity.UserQuote;
import top.quotes.pkg.server.Executor;
import top.quotes.pkg.util.PreferencesLoader;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class NewQuoteScreen extends SherlockActivity {

	private LinearLayout contentLayout;
	private EditText titleText;
	private EditText quoteText;
	private EditText seasonText;
	private EditText episodeText;
	private Spinner languageSpinner;
	private Button addButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_quote_screen);

		initScreen();
	}

	private void initScreen() {
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.user_quote_screen_title));

		contentLayout = (LinearLayout) findViewById(R.id.NewQuoteScreen_contentLayout);

		titleText = (EditText) findViewById(R.id.NewQuoteScreen_titleText);
		quoteText = (EditText) findViewById(R.id.NewQuoteScreen_quoteText);
		seasonText = (EditText) findViewById(R.id.NewQuoteScreen_seasonText);
		episodeText = (EditText) findViewById(R.id.NewQuoteScreen_episodeText);

		languageSpinner = (Spinner) findViewById(R.id.NewQuoteScreen_languageSpinner);
		ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(this, R.array.language_list_full, R.layout.sherlock_spinner_item);
		listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		languageSpinner.setAdapter(listAdapter);

		addButton = (Button) findViewById(R.id.NewQuoteScreen_addButton);
		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(titleText.getText().toString()) || !"".equals(quoteText.getText().toString()))
					showConfirmationDialog();
				else
					Toast.makeText(NewQuoteScreen.this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
			}
		});

		if (PreferencesLoader.getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getTheme() == 1) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}
	}

	protected void showConfirmationDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.user_quote_confirm_dialog_title);
		builder.setMessage(R.string.user_quote_confirm_dialog_text);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setCancelable(true);

		builder.setNegativeButton(android.R.string.no, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton(R.string.user_quote_confirm_dialog_confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				doPostQuote();
			}
		});
		builder.show();
	}

	protected void doPostQuote() {
		final ProgressDialog myProgressDialog = ProgressDialog.show(this, getString(R.string.connection), getString(R.string.connection_posting_quote), true);
		new Thread() {
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						User user = User.getInstance();
						if (user.isLoggedIn() && user.getId() != -1) {
							UserQuote quote = new UserQuote();
							quote.setTitle(titleText.getText().toString()).setText(quoteText.getText().toString()).setUserId(user.getId())
									.setLanguage(languageSpinner.getSelectedItemPosition());
							if ("".equals(seasonText.getText().toString()))
								quote.setSeason(0);
							else
								quote.setSeason(Integer.valueOf(seasonText.getText().toString()));
							if ("".equals(episodeText.getText().toString()))
								quote.setEpisode(0);
							else
								quote.setEpisode(Integer.valueOf(episodeText.getText().toString()));
							if (new Executor().sendQuote(quote) == true) {
								showSuccessDialog();
							} else {
								showErrorDialog();
							}
						} else {
							startActivity(new Intent(NewQuoteScreen.this, AuthScreen.class));
						}
					}
				});
				myProgressDialog.dismiss();
			}
		}.start();
	}

	private void showErrorDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.user_quote_failed_title);
		builder.setMessage(R.string.connection_check_text);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setCancelable(true);
		builder.show();
	}

	private void showSuccessDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.user_quote_dialog_title);
		builder.setMessage(R.string.user_quote_dialog_text);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setCancelable(true);

		builder.setNegativeButton(android.R.string.no, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		builder.setPositiveButton(R.string.user_quote_dialog_add, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				quoteText.getText().clear();
				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
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
