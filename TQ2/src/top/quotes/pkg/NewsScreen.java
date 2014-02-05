package top.quotes.pkg;

import top.quotes.pkg.util.PreferencesLoader;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class NewsScreen extends Activity {

	private LinearLayout contentLayout;
	private LinearLayout newsLayout;
	private LinearLayout firstBlockLayout;
	private LinearLayout secondBlockLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_screen);

		initScreen();
	}

	private void initScreen() {
		contentLayout = (LinearLayout) findViewById(R.id.NewsScreen_contentLayout);
		newsLayout = (LinearLayout) findViewById(R.id.NewsScreen_newsLayout);
		firstBlockLayout = (LinearLayout) findViewById(R.id.NewsScreen_firstBlockLayout);
		secondBlockLayout = (LinearLayout) findViewById(R.id.NewsScreen_secondBlockLayout);
		
		newsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("http://vk.com/top_quotes_apps");
			}
		});
		
		firstBlockLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=com.primerworldapps.vitasolution");
			}
		});
		
		
		secondBlockLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("http://vk.com/top_quotes_apps");
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
	
	private void visitUrl(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

}
