package top.quotes.pkg.adapters;

import java.util.ArrayList;

import top.quotes.pkg.R;
import top.quotes.pkg.data.Quote;
import top.quotes.pkg.util.PreferencesLoader;
import top.quotes.pkg.util.providers.QuoteRatingsProvider;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class QuoteListAdapter extends BaseAdapter {

	private Context context;
	private View view;

	private LayoutInflater inflater = null;

	private String title;
	private ArrayList<Quote> quotesList;
	private boolean isShareable;

	public QuoteListAdapter(Context context, String title, ArrayList<Quote> quotesList, boolean isShareable) {
		this.context = context;
		this.title = title;
		this.quotesList = quotesList;
		this.isShareable = isShareable;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return quotesList.size();
	}

	@Override
	public Object getItem(int position) {
		return quotesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		view = convertView;
		if (view == null)
			view = inflater.inflate(R.layout.quote_item, null);

		TextView quoteText = (TextView) view.findViewById(R.id.DailyQuote_quoteText);
		TextView showTitle = (TextView) view.findViewById(R.id.DailyQuote_showTitle);
		RatingBar ratingBar = (RatingBar) view.findViewById(R.id.DailyQuote_ratingBar);
		ImageButton shareButton = (ImageButton) view.findViewById(R.id.DailyQuote_shareButton);
		LinearLayout contentLayout = (LinearLayout) view.findViewById(R.id.DailyQuote_contentLayout);

		if (PreferencesLoader.getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getTheme() == 1) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}

		final Quote quote = quotesList.get(position);
		quoteText.setText(quote.getText());
		showTitle.setText(title);
		ratingBar.setRating(QuoteRatingsProvider.getFloatRating(quote.getRating()));
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				quote.setRating(QuoteRatingsProvider.getRatingFromFloat(rating));
				QuoteRatingsProvider.setQuoteRating(quote.getText(), quote.getRating());
			}
		});
		if (isShareable) {
			shareButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					shareQuote(context, title, quote.getText());
				}
			});
		} else {
			shareButton.setVisibility(View.GONE);
			contentLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					shareQuote(context, title, quote.getText());
				}
			});
		}

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 1);
		view.setLayoutParams(params);

		return view;
	}

	private static void shareQuote(Context context, String title, String quote) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = quote + context.getString(R.string.share_text) + " - " + title;
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_with)));
	}
}
