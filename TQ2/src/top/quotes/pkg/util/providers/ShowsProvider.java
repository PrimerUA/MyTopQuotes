package top.quotes.pkg.util.providers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import top.quotes.pkg.R;
import top.quotes.pkg.data.Quote;
import top.quotes.pkg.util.controllers.FilesController;
import android.annotation.SuppressLint;
import android.content.Context;

public class ShowsProvider {

    private static Map<Integer, Integer> listFiles;
    private static Map<Integer, Integer> listBackgrounds;

    public static ArrayList<Quote> getQuotesList(Context context, int showId) {
	ArrayList<Quote> quotes = new ArrayList<Quote>();
	ArrayList<String> texts = readQuoteFiles(context, listFiles.get(showId));
	for (int i = 0; i < texts.size(); i++) {
	    Quote quote = new Quote();
	    quote.setText(texts.get(i));
	    quote.setRating(QuoteRatingsProvider.getQuoteRating(quote.getText()));
	    quotes.add(quote);
	}
	return quotes;
    }

    public static Integer getShowBackgroundResourceId(int showId) {
	return listBackgrounds.get(showId);
    }

    @SuppressWarnings("finally")
    private static ArrayList<String> readQuoteFiles(Context context, Integer show) {
	ArrayList<String> result = new ArrayList<String>();
	try {
	    BufferedReader in = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(show)));

	    String line;
	    String buff = new String();
	    while ((line = in.readLine()) != null) {
		if (FilesController.process(line,"***")) {
		    result.add(buff);
		    buff = new String();
		} else
		    buff = buff + line + "\n";
	    }
	    in.close();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    return result;
	}
    }

    @SuppressLint("UseSparseArrays")
    public static void quotesCollectionInit() {
	listFiles = new HashMap<Integer, Integer>();
	listBackgrounds = new HashMap<Integer, Integer>();

	listFiles.put(1, R.raw.borgia);
	listBackgrounds.put(1, R.drawable.the_borgias);

	listFiles.put(101, R.raw.borgia_en);

	listFiles.put(2, R.raw.got);
	listBackgrounds.put(2, R.drawable.game_of_thrones);

	listFiles.put(102, R.raw.got_en);

	listFiles.put(3, R.raw.house);
	listBackgrounds.put(3, R.drawable.house_pic);

	listFiles.put(103, R.raw.house_en);

	listFiles.put(4, R.raw.california);
	listBackgrounds.put(4, R.drawable.californication);

	listFiles.put(104, R.raw.california_en);

	listFiles.put(5, R.raw.how);
	listBackgrounds.put(5, R.drawable.himym);

	listFiles.put(105, R.raw.how_en);

	listFiles.put(6, R.raw.tbbt);
	listBackgrounds.put(6, R.drawable.big_bang_theory);

	listFiles.put(106, R.raw.tbbt_en);

	listFiles.put(7, R.raw.dexter);
	listBackgrounds.put(7, R.drawable.dexter);

	listFiles.put(107, R.raw.dexter_en);

	listFiles.put(8, R.raw.gg);
	listBackgrounds.put(8, R.drawable.gg);

	listFiles.put(108, R.raw.gg_en);

	listFiles.put(9, R.raw.supernat);
	listBackgrounds.put(9, R.drawable.supernaturals);

	listFiles.put(109, R.raw.super_en);

	listFiles.put(10, R.raw.misfits);
	listBackgrounds.put(10, R.drawable.misfits);

	listFiles.put(110, R.raw.misfits_en);

	listFiles.put(11, R.raw.fringe);
	listBackgrounds.put(11, R.drawable.misfits);

	listFiles.put(111, R.raw.fringe_en);

	listFiles.put(12, R.raw.scrubs);
	listBackgrounds.put(12, R.drawable.scrubs);

	listFiles.put(112, R.raw.scrubs_en);

	listFiles.put(13, R.raw.interns);
	listBackgrounds.put(13, R.drawable.interns);

	listFiles.put(113, R.raw.interns);

	listFiles.put(14, R.raw.happy);
	listBackgrounds.put(14, R.drawable.happy_together);

	listFiles.put(114, R.raw.happy);

	listFiles.put(15, R.raw.vamp);
	listBackgrounds.put(15, R.drawable.vamp);

	listFiles.put(115, R.raw.vamp_en);

	listFiles.put(16, R.raw.sex);
	listBackgrounds.put(16, R.drawable.sex);

	listFiles.put(116, R.raw.sex_en);

	listFiles.put(17, R.raw.glee);
	listBackgrounds.put(17, R.drawable.glee);

	listFiles.put(117, R.raw.glee_en);

	listFiles.put(18, R.raw.who);
	listBackgrounds.put(18, R.drawable.doc_who);

	listFiles.put(118, R.raw.who_en);

	listFiles.put(19, R.raw.tb);
	listBackgrounds.put(19, R.drawable.trueblood);

	listFiles.put(119, R.raw.tb_en);

	listFiles.put(20, R.raw.friends);
	listBackgrounds.put(20, R.drawable.friends);

	listFiles.put(120, R.raw.friends_en);

	listFiles.put(21, R.raw.lost);
	listBackgrounds.put(21, R.drawable.lost);

	listFiles.put(121, R.raw.lost_en);

	listFiles.put(22, R.raw.desperate);
	listBackgrounds.put(22, R.drawable.desperate);

	listFiles.put(122, R.raw.desperate_en);

	listFiles.put(23, R.raw.charmed);
	listBackgrounds.put(23, R.drawable.charmed);

	listFiles.put(123, R.raw.charmed_en);

	listFiles.put(24, R.raw.taahm);
	listBackgrounds.put(24, R.drawable.taahm);

	listFiles.put(124, R.raw.taahm_en);

	listFiles.put(25, R.raw.karpov);
	listBackgrounds.put(25, R.drawable.karpov);

	listFiles.put(125, R.raw.karpov);

	listFiles.put(26, R.raw.spartak);
	listBackgrounds.put(26, R.drawable.spartacus);

	listFiles.put(126, R.raw.spartak_en);

	listFiles.put(27, R.raw.sherlock);
	listBackgrounds.put(27, R.drawable.sherlock);

	listFiles.put(127, R.raw.sherlock_en);

	listFiles.put(28, R.raw.univer);
	listBackgrounds.put(28, R.drawable.univer);

	listFiles.put(128, R.raw.univer);

	listFiles.put(29, R.raw.hill);
	listBackgrounds.put(29, R.drawable.hill);

	listFiles.put(129, R.raw.hill_en);

	listFiles.put(30, R.raw.liars);
	listBackgrounds.put(30, R.drawable.liars);

	listFiles.put(130, R.raw.liars_en);
    }
}
