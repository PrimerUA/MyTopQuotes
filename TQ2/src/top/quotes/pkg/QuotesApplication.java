package top.quotes.pkg;

import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;

public class QuotesApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "GQQLwOZSuTsuvcqS0YsdBRECedXibvl8t7SB1qNA", "byO16Pd6ol1GomNWXeNteNU5mVD4XodrMR8fIfam");
		PushService.setDefaultPushCallback(this, MainScreen.class);
	}

}
