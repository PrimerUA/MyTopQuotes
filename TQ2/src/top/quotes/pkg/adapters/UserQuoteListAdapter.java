package top.quotes.pkg.adapters;

import java.util.ArrayList;

import top.quotes.pkg.R;
import top.quotes.pkg.entity.UserQuote;
import top.quotes.pkg.util.PreferencesLoader;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class UserQuoteListAdapter extends BaseAdapter {

	private Context context;
	private View view;

	private LayoutInflater inflater = null;

	private String title;
	private ArrayList<UserQuote> quotesList;
	private boolean isShareable;

	public UserQuoteListAdapter(Context context, ArrayList<UserQuote> quotesList, boolean isShareable) {
		this.context = context;
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
			view = inflater.inflate(R.layout.user_quote_item, null);

		TextView quoteText = (TextView) view.findViewById(R.id.UserQuote_quoteText);
		TextView showTitle = (TextView) view.findViewById(R.id.UserQuote_showTitle);
		TextView seasonEpisodeText = (TextView) view.findViewById(R.id.UserQuote_seasonEpisodeText);
		TextView authorText = (TextView) view.findViewById(R.id.UserQuote_authorText);
		ImageButton shareButton = (ImageButton) view.findViewById(R.id.UserQuote_shareButton);
		LinearLayout contentLayout = (LinearLayout) view.findViewById(R.id.UserQuote_contentLayout);

		if (PreferencesLoader.getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getTheme() == 1) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}

		final UserQuote quote = quotesList.get(position);
		quoteText.setText(quote.getText());
		showTitle.setText(quote.getTitle());
		seasonEpisodeText.setText(context.getString(R.string.quote_season) + " " + quote.getSeason() +", " + context.getString(R.string.quote_episode) + " " + quote.getEpisode());
		authorText.setText(context.getString(R.string.quote_by) + " " + quote.getUserName());
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
