package top.quotes.pkg;

import java.util.ArrayList;

import top.quotes.pkg.R;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.util.PreferencesLoader;
import top.quotes.pkg.util.controllers.FilesController;
import top.quotes.pkg.util.providers.ScoresProvider;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoresScreen extends Activity {

	private LinearLayout contentLayout;
	private LinearLayout backgroundLayout;
	private Button clearButton;
	private Button retryButton;

	private ArrayList<String> scoresList;

	private Integer currentScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scores_screen);

		initScreen();

		currentScore = getIntent()
				.getIntExtra(ConstantsFacade.CURRENT_SCORE, 0);
		scoresList = ScoresProvider.getScoresList(this);
		if (currentScore != 0 && !scoresList.contains(currentScore)) {
			scoresList = ScoresProvider.saveResults(currentScore);
		}

		for (int i = 0; i < scoresList.size(); i++) {
			addScoresOnScreen(i);
		}
	}

	private void addScoresOnScreen(int i) {
		View view = getLayoutInflater().inflate(R.layout.score_item, null);
		TextView score = (TextView) view
				.findViewById(R.id.ScoreItem_resultText);
		// LinearLayout contenLayout = (LinearLayout) view
		// .findViewById(R.id.ScoreItem_contentLayout);

		score.setText(scoresList.get(i).toString());

		contentLayout.addView(view);
	}

	private void initScreen() {
		contentLayout = (LinearLayout) findViewById(R.id.ScoresScreen_contentLayout);
		backgroundLayout = (LinearLayout) findViewById(R.id.ScoresScreen_backgroundLayout);
		retryButton = (Button) findViewById(R.id.ScoresScreen_retryButton);
		clearButton = (Button) findViewById(R.id.ScoresScreen_clearButton);

		clearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				contentLayout.removeAllViews();
				FilesController.clearFile("scores.txt");
			}
		});

		retryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if (PreferencesLoader.getInstance().getTheme() == 0) {
			backgroundLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getInstance().getTheme() == 1) {
			backgroundLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			backgroundLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}
	}

}
