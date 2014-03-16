package top.quotes.pkg;

import top.quotes.pkg.util.PreferencesLoader;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WelcomeScreen extends Activity {

	private Button startButton;
	private LinearLayout contentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);

		initScreen();

	}

	private void initScreen() {
		startButton = (Button) findViewById(R.id.WelcomeScreen_startButton);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		contentLayout = (LinearLayout) findViewById(R.id.WelcomeScreen_contentLayout);
		if (PreferencesLoader.getInstance().getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getInstance().getTheme() == 1) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.welcome_screen_toast),
				Toast.LENGTH_SHORT).show();
	}
}
