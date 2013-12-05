package top.quotes.pkg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeScreen extends Activity {

	private Button startButton;

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
	}
	
	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.welcome_screen_toast), Toast.LENGTH_SHORT).show();
	}

}
