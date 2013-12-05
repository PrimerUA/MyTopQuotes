package top.quotes.pkg.util.controllers;

public enum LanguageController {
    RUS, ENG;

    private static LanguageController currentLanguage;
    private static LanguageChanger languageChanger;

    public interface LanguageChanger {
	public void onLanguageChanged();
    }

    public static void setOnLanguageChangedListener(LanguageChanger changer) {
	languageChanger = changer;
    }

    public static void notifyLanguageChanged() {
	if (languageChanger != null) {
	    languageChanger.onLanguageChanged();
	}
    }

    public static LanguageController getCurrentLanguage() {
	return currentLanguage;
    }

    public static void setCurrentLanguage(LanguageController currentLanguage) {
	LanguageController.currentLanguage = currentLanguage;
	notifyLanguageChanged();
    }
}
