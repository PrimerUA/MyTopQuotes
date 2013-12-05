package top.quotes.pkg.util.providers;

import java.util.ArrayList;
import java.util.Date;

import top.quotes.pkg.util.controllers.FilesController;
import android.content.Context;

public class ScoresProvider {

    private static ArrayList<String> scoresList;

    private static Context context;

    @SuppressWarnings("deprecation")
    public static ArrayList<String> saveResults(Integer score) {
	Date currentDate = new Date(System.currentTimeMillis());
	scoresList = FilesController.loadScoresFromFile(context);
	scoresList.add(currentDate.toLocaleString() + " - " + score.toString());
	FilesController.saveScore(context, "scores.txt", currentDate.toLocaleString() + " - " + score.toString());
	return scoresList;
    }

    public static ArrayList<String> getScoresList(Context context) {
	scoresList = FilesController.loadScoresFromFile(context);
	return scoresList;
    }

}
