package top.quotes.pkg.util.providers;

import java.util.ArrayList;
import java.util.Random;

import top.quotes.pkg.R;
import top.quotes.pkg.data.DailyQuote;
import top.quotes.pkg.data.Quote;
import top.quotes.pkg.data.Show;
import top.quotes.pkg.data.ShowsList;
import top.quotes.pkg.util.PreferencesLoader;
import top.quotes.pkg.util.Rating;
import top.quotes.pkg.util.controllers.LanguageController;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class QuoteViewsProvider {

	private static ArrayList<Integer> onScreenQuotesList = new ArrayList<Integer>();
	private static ArrayList<Integer> russianOnlyList = new ArrayList<Integer>();

	private static DailyQuote dailyQuote;

	public static int getOnScreenQuotesListSize() {
		return onScreenQuotesList.size();
	}

	public static void clearOnScreenQuotesList() {
		onScreenQuotesList.clear();
	}

	private static void russianOnlyListGenerate() {
		russianOnlyList.add(12);
		russianOnlyList.add(13);
		russianOnlyList.add(24);
		russianOnlyList.add(27);
	}

	public static View getDailyQuoteView(final Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.quote_item, null);

		TextView quoteText = (TextView) view
				.findViewById(R.id.DailyQuote_quoteText);
		TextView showTitle = (TextView) view
				.findViewById(R.id.DailyQuote_showTitle);
		RatingBar ratingBar = (RatingBar) view
				.findViewById(R.id.DailyQuote_ratingBar);
		ImageButton shareButton = (ImageButton) view
				.findViewById(R.id.DailyQuote_shareButton);
		LinearLayout contentLayout = (LinearLayout) view
				.findViewById(R.id.DailyQuote_contentLayout);

		if (PreferencesLoader.getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getTheme() == 1) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}

		quoteText.setText(dailyQuote.getText());
		showTitle.setText(dailyQuote.getShowTitle());
		ratingBar.setRating(QuoteRatingsProvider.getFloatRating(dailyQuote
				.getRating()));
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				dailyQuote.setRating(QuoteRatingsProvider
						.getRatingFromFloat(rating));
				QuoteRatingsProvider.setQuoteRating(dailyQuote.getText(),
						dailyQuote.getRating());
			}
		});
		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareQuote(context, dailyQuote.getShowTitle(),
						dailyQuote.getText());
			}
		});

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 1, 0, 1);
		view.setLayoutParams(params);

		return view;
	}

	public static View getRatedQuoteView(final Context context, int position,
			Rating rating) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.quote_item, null);

		TextView quoteText = (TextView) view
				.findViewById(R.id.DailyQuote_quoteText);
		TextView showTitle = (TextView) view
				.findViewById(R.id.DailyQuote_showTitle);
		RatingBar ratingBar = (RatingBar) view
				.findViewById(R.id.DailyQuote_ratingBar);
		ImageButton shareButton = (ImageButton) view
				.findViewById(R.id.DailyQuote_shareButton);
		LinearLayout contentLayout = (LinearLayout) view
				.findViewById(R.id.DailyQuote_contentLayout);

		if (PreferencesLoader.getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getTheme() == 1) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}

		ArrayList<String> ratingRUS1;
		ArrayList<String> ratingRUS2;
		ArrayList<String> ratingRUS3;

		ArrayList<String> ratingENG1;
		ArrayList<String> ratingENG2;
		ArrayList<String> ratingENG3;

		final String quote;
		final LanguageController language = LanguageController
				.getCurrentLanguage();
		if (language == LanguageController.RUS) {
			if (rating == Rating.GOOD) {
				ratingRUS3 = QuoteRatingsProvider.getRatingListRUS3();
				if (ratingRUS3.size() <= position) {
					return null;
				}
				quote = ratingRUS3.get(position);
			} else if (rating == Rating.NORMAL) {
				ratingRUS2 = QuoteRatingsProvider.getRatingListRUS2();
				if (ratingRUS2.size() <= position) {
					return null;
				}
				quote = ratingRUS2.get(position);
			} else {
				ratingRUS1 = QuoteRatingsProvider.getRatingListRUS1();
				if (ratingRUS1.size() <= position) {
					return null;
				}
				quote = ratingRUS1.get(position);
			}
		} else {
			if (rating == Rating.GOOD) {
				ratingENG3 = QuoteRatingsProvider.getRatingListENG3();
				if (ratingENG3.size() <= position) {
					return null;
				}
				quote = ratingENG3.get(position);
			} else if (rating == Rating.NORMAL) {
				ratingENG2 = QuoteRatingsProvider.getRatingListENG2();
				if (ratingENG2.size() <= position) {
					return null;
				}
				quote = ratingENG2.get(position);
			} else {
				ratingENG1 = QuoteRatingsProvider.getRatingListENG1();
				if (ratingENG1.size() <= position) {
					return null;
				}
				quote = ratingENG1.get(position);
			}
		}

		quoteText.setText(quote);
		ratingBar.setRating(QuoteRatingsProvider.getFloatRating(rating));

		final Show show;
		show = ShowsList.getShowByQuote(quote);
		if (show != null) {
			showTitle.setText(show.getTitle(language));

			shareButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					shareQuote(context, show.getTitle(LanguageController
							.getCurrentLanguage()), quote);
				}
			});
		} else {
			showTitle.setText(R.string.app_name);
			shareButton.setVisibility(View.GONE);
		}
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 1, 0, 1);
		view.setLayoutParams(params);

		return view;
	}

	private static void shareQuote(Context context, String title, String quote) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = quote + context.getString(R.string.share_text)
				+ " - " + title;
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		context.startActivity(Intent.createChooser(sharingIntent,
				context.getString(R.string.share_with)));
	}

	public static DailyQuote getDailyQuote() {
		final Show show;
		Random randInt = new Random();
		Integer random = randInt.nextInt(ShowsList.SHOWS_LIST_SIZE);
		if (PreferencesLoader.getLanguage() == LanguageController.ENG) {
			russianOnlyListGenerate();
			while (russianOnlyList.contains(random)) {
				random = randInt.nextInt(ShowsList.SHOWS_LIST_SIZE);
			}
		}
		show = ShowsList.getList().get(random);
		int item = randInt.nextInt(show.getQuotesList(
				PreferencesLoader.getLanguage()).size());
		Quote quote = show.getQuote(item, PreferencesLoader.getLanguage());
		dailyQuote = new DailyQuote(show.getTitle(PreferencesLoader
				.getLanguage()), quote);
		return dailyQuote;
	}

}
