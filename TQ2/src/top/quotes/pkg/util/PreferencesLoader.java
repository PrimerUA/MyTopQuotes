package top.quotes.pkg.util;

import top.quotes.pkg.R;
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

	private static void loadPreferences() {
		sharedPreferences = context.getSharedPreferences(context.getString(R.string.prefs), Context.MODE_PRIVATE);
		String lang = sharedPreferences.getString(context.getString(R.string.prefs_language), LanguageController.RUS.toString());
		if (lang.equals(LanguageController.RUS.toString())) {
			language = LanguageController.RUS;
		} else {
			language = LanguageController.ENG;
		}
		LanguageController.setCurrentLanguage(language);
		theme = sharedPreferences.getInt(context.getString(R.string.prefs_theme), 0);
		daily = sharedPreferences.getBoolean(context.getString(R.string.prefs_daily), true);
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
