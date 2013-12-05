package top.quotes.pkg;

import top.quotes.pkg.util.PreferencesLoader;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MoreScreen extends Activity {
	
	private LinearLayout truthLayout;
	private LinearLayout vitacodeLayout;
	private LinearLayout quarterLayout;
	private LinearLayout blogLayout;
	private LinearLayout vkLayout;
	private LinearLayout brainLayout;
	
	private LinearLayout contentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_screen);
		
		activityInit();
	}

	private void activityInit() {
		contentLayout = (LinearLayout) findViewById(R.id.MoreScreen_contentLayout);
		
		truthLayout = (LinearLayout) findViewById(R.id.MoreScreen_truthLayout);
		vitacodeLayout = (LinearLayout) findViewById(R.id.MoreScreen_vitacodeLayout);
		quarterLayout = (LinearLayout) findViewById(R.id.MoreScreen_quarterLayout);
		blogLayout = (LinearLayout) findViewById(R.id.MoreScreen_blogLayout);
		vkLayout = (LinearLayout) findViewById(R.id.MoreScreen_vkLayout);
		brainLayout = (LinearLayout) findViewById(R.id.MoreScreen_brainLayout);

		truthLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=com.primerworldapps.truthgame");
			}
		});

		brainLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=com.primerworldapps.brainbreak");
			}
		});
		
		vitacodeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=com.softvit.vitacall");
			}
		});

		quarterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("https://play.google.com/store/apps/details?id=pro.top.comic.pkg");
			}
		});

		blogLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("http://suit-up-primer.blogspot.com/");
			}
		});

		vkLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visitUrl("http://vk.com/liveindroid");
			}
		});

		if (PreferencesLoader.getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		}
	}
	
	private void visitUrl(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

}