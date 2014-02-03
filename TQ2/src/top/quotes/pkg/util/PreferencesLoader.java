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

	public static void initPrefs(Context context) {
		PreferencesLoader.context = context;
		loadPreferences();
	}

	private static void savePreferences() {
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(context.getString(R.string.prefs_theme), theme);
		editor.putString(context.getString(R.string.prefs_language), language.toString());
		editor.putBoolean(context.getString(R.string.prefs_daily), daily);
		editor.commit();
	}
	
	public static void saveUserData() {
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("id", User.getInstance().getId());
		editor.putString("name", User.getInstance().getName());
		editor.putString("email", User.getInstance().getEmail());
		editor.putBoolean("loggedIn", User.getInstance().isLoggedIn());
		editor.commit();
	}

	private static void loadPreferences() {
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
		User.getInstance().setId(sharedPreferences.getInt("id", -1));
		User.getInstance().setName(sharedPreferences.getString("name", ""));
		User.getInstance().setEmail(sharedPreferences.getString("email", ""));
	}

	public static int getTheme() {
		return theme;
	}

	public static void setTheme(int theme) {
		PreferencesLoader.theme = theme;
		savePreferences();
	}

	public static LanguageController getLanguage() {
		return language;
	}

	public static void setLanguage(LanguageController language) {
		PreferencesLoader.language = language;
		savePreferences();
	}

	public static Boolean isDaily() {
		return daily;
	}

	public static void setDaily(Boolean daily) {
		PreferencesLoader.daily = daily;
		DailyNotificationController.initNotification(context);
		savePreferences();
	}

}
