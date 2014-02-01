package top.quotes.pkg;

import top.quotes.pkg.util.PreferencesLoader;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockActivity;

public class NewQuoteScreen extends SherlockActivity {

	private LinearLayout contentLayout;
	private EditText titleText;
	private EditText quoteText;
	private EditText showText;
	private EditText episodeText;
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
		showText = (EditText) findViewById(R.id.NewQuoteScreen_seasonText);
		episodeText = (EditText) findViewById(R.id.NewQuoteScreen_episodeText);
		
		addButton = (Button) findViewById(R.id.NewQuoteScreen_addButton);
		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showConfirmationDialog();
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
		// check if user exists in DB
		// post quote to Stream
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
		    	dialog.dismiss();
		    }
		});
		builder.show();
	}
}
