package top.quotes.pkg;

import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;

public class QuotesApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "pCqJIUiu8jgxPO2Oe6OcHpTeWFbeBv9OMlSnBJTD", "RwEQwd2mTeDwBQ2gAOwHfFsg0Ae4G2FOaJGEZnIr");
		PushService.setDefaultPushCallback(this, MainScreen.class);
	}

}
