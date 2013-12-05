package top.quotes.pkg.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.quotes.pkg.R;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.util.providers.ShowsProvider;
import android.content.Context;

public class ShowsList {

    private static ArrayList<Show> showsList;
    private static List<String> showTitlesRUS;
    private static List<String> showTitlesENG;

    public static int SHOWS_LIST_SIZE;

    public static ArrayList<Show> getList() {
	return showsList;
    }

    public static void initShows(Context context) {
	setTitles(context);

	SHOWS_LIST_SIZE = showTitlesRUS.size();
	showsList = new ArrayList<Show>(SHOWS_LIST_SIZE);
	ShowsProvider.quotesCollectionInit();
	for (int i = 0; i < SHOWS_LIST_SIZE; i++) {
	    Show show = new Show(i, showTitlesRUS.get(i), showTitlesENG.get(i), ShowsProvider.getQuotesList(context, i + 1), ShowsProvider.getQuotesList(context, i + 101),
		    ShowsProvider.getShowBackgroundResourceId(i + 1));
	    showsList.add(show);
	}
    }

    private static void setTitles(Context context) {
	showTitlesRUS = new ArrayList<String>();
	showTitlesENG = new ArrayList<String>();
	List<String> bufferRUS = Arrays.asList(context.getResources().getStringArray(R.array.shows_array_rus));
	List<String> bufferENG = Arrays.asList(context.getResources().getStringArray(R.array.shows_array_eng));
	for (int i = ConstantsFacade.MENU_ITEMS_NOT_SHOWS; i < bufferRUS.size(); i++) {
	    showTitlesRUS.add(bufferRUS.get(i));
	    showTitlesENG.add(bufferENG.get(i));
	}
    }

    public static int getBackgroundByShowId(int showId) {
	int id = 0;
	for (Show show : showsList) {
	    if (show.getId() == showId) {
		id = show.getBackground();
	    }
	}
	return id;
    }

    public static Show getShowById(int showId) {
	for (int i = 0; i < getList().size(); i++) {
	    Show show = getList().get(i);
	    if (show.getId() == showId) {
		return show;
	    }
	}
	return null;
    }

    public static Show getShowByQuote(String quote) {
	for (int i = 0; i < getList().size(); i++) {
	    Show show = getList().get(i);
	    if (show.getQuotesListRUS().contains(quote) || show.getQuotesListENG().contains(quote)) {
		return show;
	    }
	}
	return null;
    }

    public static void clearShows() {
	showsList.clear();
    }
}
