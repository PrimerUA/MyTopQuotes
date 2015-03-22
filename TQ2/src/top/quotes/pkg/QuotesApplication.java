package top.quotes.pkg;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;

public class QuotesApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ParseCrashReporting.enable(this);
		Parse.initialize(this, "pCqJIUiu8jgxPO2Oe6OcHpTeWFbeBv9OMlSnBJTD", "RwEQwd2mTeDwBQ2gAOwHfFsg0Ae4G2FOaJGEZnIr");
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}

}
