package top.quotes.pkg.core;

import com.actionbarsherlock.app.SherlockFragment;

import top.quotes.pkg.util.PreferencesLoader;
import top.quotes.pkg.util.controllers.LanguageController;

public abstract class CoreFragment extends SherlockFragment {

	private int currentTheme;

	protected abstract void initFragment();

	protected abstract void addQuotesOnScreen();

	protected abstract void updateContent();

	@Override
	public void onPause() {
		super.onPause();
		currentTheme = getTheme();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (currentTheme != getTheme()) {
			updateContent();
		}
	}

	public LanguageController getLanguage() {
		return LanguageController.getCurrentLanguage();
	}

	public int getTheme() {
		return PreferencesLoader.getInstance().getTheme();
	}

}
