package top.quotes.pkg.util.providers;

import java.util.ArrayList;

import top.quotes.pkg.util.Rating;
import top.quotes.pkg.util.controllers.FilesController;
import top.quotes.pkg.util.controllers.LanguageController;
import android.content.Context;
import android.widget.Toast;

public class QuoteRatingsProvider {

    private static ArrayList<String> ratingListRUS1;
    private static ArrayList<String> ratingListENG1;

    private static ArrayList<String> ratingListRUS2;
    private static ArrayList<String> ratingListENG2;

    private static ArrayList<String> ratingListRUS3;
    private static ArrayList<String> ratingListENG3;

    private static Context context;

    public static void initRatings(Context context) {
	QuoteRatingsProvider.context = context;
	if (LanguageController.getCurrentLanguage() == LanguageController.RUS) {
	    ratingListRUS1 = FilesController.loadRatingsFromFile(context, "rating" + LanguageController.RUS + "1.txt");
	    ratingListRUS2 = FilesController.loadRatingsFromFile(context, "rating" + LanguageController.RUS + "2.txt");
	    ratingListRUS3 = FilesController.loadRatingsFromFile(context, "rating" + LanguageController.RUS + "3.txt");
	} else {
	    ratingListENG1 = FilesController.loadRatingsFromFile(context, "rating" + LanguageController.ENG + "1.txt");
	    ratingListENG2 = FilesController.loadRatingsFromFile(context, "rating" + LanguageController.ENG + "2.txt");
	    ratingListENG3 = FilesController.loadRatingsFromFile(context, "rating" + LanguageController.ENG + "3.txt");
	}
    }

    public static Rating getQuoteRating(String quote) {
	nullCheck();
	if (ratingListRUS1.contains(quote) || ratingListENG1.contains(quote)) {
	    return Rating.BAD;
	} else if (ratingListRUS2.contains(quote) || ratingListENG2.contains(quote)) {
	    return Rating.NORMAL;
	} else if (ratingListRUS3.contains(quote) || ratingListENG3.contains(quote)) {
	    return Rating.GOOD;
	} else {
	    return Rating.NONE;
	}
    }

    private static void nullCheck() {
	if (ratingListENG1 == null) {
	    ratingListENG1 = new ArrayList<String>();
	}
	if (ratingListENG2 == null) {
	    ratingListENG2 = new ArrayList<String>();
	}
	if (ratingListENG3 == null) {
	    ratingListENG3 = new ArrayList<String>();
	}
	if (ratingListRUS1 == null) {
	    ratingListRUS1 = new ArrayList<String>();
	}
	if (ratingListRUS2 == null) {
	    ratingListRUS2 = new ArrayList<String>();
	}
	if (ratingListRUS3 == null) {
	    ratingListRUS3 = new ArrayList<String>();
	}
    }

    public static void setQuoteRating(String quote, Rating rating) {
	if (checkQuote(quote, rating)) {
	    String fileName = null;
	    if (rating == Rating.BAD) {
		fileName = "rating" + LanguageController.getCurrentLanguage().toString() + "1.txt";
	    } else if (rating == Rating.NORMAL) {
		fileName = "rating" + LanguageController.getCurrentLanguage().toString() + "2.txt";
	    } else if (rating == Rating.GOOD) {
		fileName = "rating" + LanguageController.getCurrentLanguage().toString() + "3.txt";
	    }
	    FilesController.saveRating(context, fileName, quote);
	    initRatings(context);
	}
    }

    private static boolean checkQuote(String quote, Rating rating) {
	if (rating == Rating.BAD) {
	    if (ratingListENG1.contains(quote) || ratingListRUS1.contains(quote)) {
		return false;
	    }
	} else if (rating == Rating.NORMAL) {
	    if (ratingListENG2.contains(quote) || ratingListRUS2.contains(quote)) {
		return false;
	    }
	} else if (rating == Rating.GOOD) {
	    if (ratingListENG3.contains(quote) || ratingListRUS3.contains(quote)) {
		return false;
	    }
	}
	return true;
    }

    public static float getFloatRating(Rating rating) {
	if (rating == Rating.BAD) {
	    return 1;
	} else if (rating == Rating.NORMAL) {
	    return 2;
	} else if (rating == Rating.GOOD) {
	    return 3;
	} else {
	    return 0;
	}
    }

    public static Rating getRatingFromFloat(float rating) {
	if (rating <= 1) {
	    return Rating.BAD;
	} else if (rating > 1 && rating <= 2) {
	    return Rating.NORMAL;
	} else if (rating > 2 && rating <= 3) {
	    return Rating.GOOD;
	} else {
	    return Rating.NONE;
	}
    }

    public static ArrayList<String> getRatingListRUS1() {
	return ratingListRUS1;
    }

    public static ArrayList<String> getRatingListRUS2() {
	return ratingListRUS2;
    }

    public static ArrayList<String> getRatingListRUS3() {
	return ratingListRUS3;
    }

    public static ArrayList<String> getRatingListENG1() {
	return ratingListENG1;
    }

    public static ArrayList<String> getRatingListENG2() {
	return ratingListENG2;
    }

    public static ArrayList<String> getRatingListENG3() {
	return ratingListENG3;
    }

    public static void clearArrays() {
	ratingListENG1.clear();
	ratingListENG2.clear();
	ratingListENG3.clear();

	ratingListRUS1.clear();
	ratingListRUS2.clear();
	ratingListRUS3.clear();
    }
}
