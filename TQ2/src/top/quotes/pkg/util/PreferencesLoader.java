package top.quotes.pkg.util;

import top.quotes.pkg.R;
import top.quotes.pkg.entity.User;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.controllers.DailyNotificationController;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesLoader {

	private static SharedPreferences sharedPreferences;
	private static Context context;

	private static LanguageController language;
	private static int theme;
	private static Boolean daily;
	
	private static PreferencesLoader instance = null;

	public static PreferencesLoader getInstance() {
		if (instance == null)
			instance = new PreferencesLoader();
		return instance;
	}

	public void initPrefs(Context context) {
		PreferencesLoader.context = context;
		loadPreferences();
	}

	private void savePreferences() {
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(context.getString(R.string.prefs_theme), theme);
		editor.putString(context.getString(R.string.prefs_language), language.toString());
		editor.putBoolean(context.getString(R.string.prefs_daily), daily);
		editor.commit();
	}
	
	public void saveUserData() {
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("name", User.getInstance().getName());
		editor.putString("email", User.getInstance().getEmail());
		editor.putBoolean("loggedIn", User.getInstance().isLoggedIn());
		editor.commit();
	}

	private void loadPreferences() {
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
		
		String lang = sharedPreferences.getString(context.getString(R.string.prefs_language), LanguageController.RUS.toString());
		if (lang.equals(LanguageController.RUS.toString())) {
			language = LanguageController.RUS;
		} else {
			language = LanguageController.ENG;
		}
		LanguageController.setCurrentLanguage(language);
		theme = sharedPreferences.getInt(context.getString(R.string.prefs_theme), 2);
		daily = sharedPreferences.getBoolean(context.getString(R.string.prefs_daily), true);
		
		User.getInstance().setLoggedIn(sharedPreferences.getBoolean("loggedIn", false));
		User.getInstance().setName(sharedPreferences.getString("name", ""));
		User.getInstance().setEmail(sharedPreferences.getString("email", ""));
	}

	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		PreferencesLoader.theme = theme;
		savePreferences();
	}

	public LanguageController getLanguage() {
		return language;
	}

	public void setLanguage(LanguageController language) {
		PreferencesLoader.language = language;
		savePreferences();
	}

	public Boolean isDaily() {
		return daily;
	}

	public void setDaily(Boolean daily) {
		PreferencesLoader.daily = daily;
		DailyNotificationController.initNotification(context);
		savePreferences();
	}

}
