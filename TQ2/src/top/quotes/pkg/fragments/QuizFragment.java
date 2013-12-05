package top.quotes.pkg.fragments;

import java.util.ArrayList;
import java.util.Random;

import top.quotes.pkg.R;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.data.Show;
import top.quotes.pkg.data.ShowsList;
import top.quotes.pkg.messages.QuizDialog;
import top.quotes.pkg.util.PreferencesLoader;
import top.quotes.pkg.util.controllers.LanguageController;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class QuizFragment extends SherlockFragment implements OnClickListener {

	private static View rootView;

	private static TextView score;
	private TextView quote;
	private static TextView attempts;

	private Button firstAnswer;
	private Button secondAnswer;
	private Button thirdAnswer;
	private Button fourthAnswer;

	private ScrollView contentScroll;
	private LinearLayout answersLayout;
	private ImageView drawerImage;

	private ArrayList<Show> showsList;

	private String correctAnswer;
	private ArrayList<String> titles;

	private static int scoreCount;
	private static int attemptsCount;

	private LanguageController currentLanguage;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
		initFragment();

		getSherlockActivity().setTitle(getResources().getString(R.string.fragment_quiz));

		setScoreAndAttempts(0, ConstantsFacade.QUIZ_MAX_ATTEMPTS_COUNT);

		showsList = ShowsList.getList();
		titles = new ArrayList<String>();

		loadQuizQuote();

		return rootView;
	}

	private void loadQuizQuote() {
		contentScroll.fullScroll(ScrollView.FOCUS_UP);
		if (!titles.isEmpty()) {
			titles.clear();
		}
		currentLanguage = LanguageController.getCurrentLanguage();
		Random rand = new Random();
		Integer randInt = rand.nextInt(ShowsList.getList().size());
		if (currentLanguage == LanguageController.ENG) {
			while (randInt == 12 || randInt == 13 || randInt == 24 || randInt == 27) {
				randInt = rand.nextInt(ShowsList.getList().size());
			}
		}
		Show show = showsList.get(randInt);
		correctAnswer = show.getTitle(currentLanguage);
		titles.add(correctAnswer);
		for (int i = 0; i < 3; i++) {
			randInt = rand.nextInt(ShowsList.getList().size());
			if (currentLanguage == LanguageController.ENG) {
				while (randInt == 12 || randInt == 13 || randInt == 24 || randInt == 27) {
					randInt = rand.nextInt(ShowsList.getList().size());
				}
			}
			Show otherShow = showsList.get(randInt);
			while (titles.contains(otherShow.getTitle(currentLanguage))) {
				otherShow = showsList.get(rand.nextInt(ShowsList.getList().size()));
			}
			titles.add(otherShow.getTitle(currentLanguage));
		}
		quote.setText(show.getQuote(rand.nextInt(show.getQuotesList(currentLanguage).size()), currentLanguage).getText());
		setButtonTitles();
	}

	private void setButtonTitles() {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		Random rand = new Random();
		int k = rand.nextInt(4);

		numbers.add(k);
		firstAnswer.setText(titles.get(k));
		k = rand.nextInt(4);
		while (numbers.contains(k)) {
			k = rand.nextInt(4);
		}
		secondAnswer.setText(titles.get(k));

		numbers.add(k);
		k = rand.nextInt(4);
		while (numbers.contains(k)) {
			k = rand.nextInt(4);
		}
		thirdAnswer.setText(titles.get(k));

		numbers.add(k);
		k = rand.nextInt(4);
		while (numbers.contains(k)) {
			k = rand.nextInt(4);
		}
		fourthAnswer.setText(titles.get(k));

	}

	@SuppressLint("NewApi")
	private void initFragment() {
		score = (TextView) rootView.findViewById(R.id.QuizFragment_scoreText);
		quote = (TextView) rootView.findViewById(R.id.QuizFragment_quoteText);
		attempts = (TextView) rootView.findViewById(R.id.QuizFragment_attemptsText);

		contentScroll = (ScrollView) rootView.findViewById(R.id.QuizFragment_contentScroll);
		answersLayout = (LinearLayout) rootView.findViewById(R.id.QuizFragment_answersLayout);
		drawerImage = (ImageView) rootView.findViewById(R.id.QuizFragment_drawerImage);

		firstAnswer = (Button) rootView.findViewById(R.id.QuizFragment_firstAnswerButton);
		secondAnswer = (Button) rootView.findViewById(R.id.QuizFragment_secondAnswerButton);
		thirdAnswer = (Button) rootView.findViewById(R.id.QuizFragment_thirdAnswerButton);
		fourthAnswer = (Button) rootView.findViewById(R.id.QuizFragment_fourthAnswerButton);

		firstAnswer.setOnClickListener(this);
		secondAnswer.setOnClickListener(this);
		thirdAnswer.setOnClickListener(this);
		fourthAnswer.setOnClickListener(this);

		currentLanguage = LanguageController.getCurrentLanguage();

		getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(currentLanguage.ordinal());
		
	}

	@Override
	public void onClick(View v) {
		Button button = (Button) v;
		String userAnswer = button.getText().toString();
		if (userAnswer.equals(correctAnswer)) {
			scoreCount += ConstantsFacade.QUIZ_STANDART_SCORE_POINTS;
			QuizDialog.showQuizDialog(rootView.getContext(), userAnswer, correctAnswer, scoreCount, false);
			setScoreAndAttempts(scoreCount, attemptsCount);
		} else {
			attemptsCount--;
			if (attemptsCount == 0) {
				QuizDialog.showQuizDialog(rootView.getContext(), userAnswer, correctAnswer, scoreCount, true);
				setScoreAndAttempts(0, ConstantsFacade.QUIZ_MAX_ATTEMPTS_COUNT);
			} else {
				QuizDialog.showQuizDialog(rootView.getContext(), userAnswer, correctAnswer, scoreCount, false);
				setScoreAndAttempts(scoreCount, attemptsCount);
			}
		}
		loadQuizQuote();
	}

	public static void setScoreAndAttempts(int newScore, int newAttempts) {
		scoreCount = newScore;
		attemptsCount = newAttempts;
		score.setText(rootView.getContext().getString(R.string.score) + " " + scoreCount);
		attempts.setText(rootView.getContext().getString(R.string.attempts) + " " + attemptsCount + "/" + ConstantsFacade.QUIZ_MAX_ATTEMPTS_COUNT);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (PreferencesLoader.getTheme() == 0) {
			quote.setBackgroundResource(R.drawable.quote_border_pink);
			answersLayout.setBackgroundResource(R.drawable.quote_border_pink);
			score.setBackgroundResource(R.drawable.quote_border_pink);
			attempts.setBackgroundResource(R.drawable.quote_border_pink);
			drawerImage.setBackgroundColor(Color.parseColor("#c92064"));
			firstAnswer.setBackgroundResource(R.drawable.quote_selector_pink);
			secondAnswer.setBackgroundResource(R.drawable.quote_selector_pink);
			thirdAnswer.setBackgroundResource(R.drawable.quote_selector_pink);
			fourthAnswer.setBackgroundResource(R.drawable.quote_selector_pink);
		} else {
			quote.setBackgroundResource(R.drawable.quote_border_white);
			answersLayout.setBackgroundResource(R.drawable.quote_border_white);
			drawerImage.setBackgroundColor(Color.DKGRAY);
			score.setBackgroundResource(R.drawable.quote_border_white);
			attempts.setBackgroundResource(R.drawable.quote_border_white);
			firstAnswer.setBackgroundResource(R.drawable.quote_selector_white);
			secondAnswer.setBackgroundResource(R.drawable.quote_selector_white);
			thirdAnswer.setBackgroundResource(R.drawable.quote_selector_white);
			fourthAnswer.setBackgroundResource(R.drawable.quote_selector_white);
		}
	}
	
}