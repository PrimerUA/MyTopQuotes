package top.quotes.pkg.adapters;

import java.util.ArrayList;

import com.parse.ParseObject;

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
import android.widget.TextView;

public class UserQuoteListAdapter extends BaseAdapter {

	private Context context;
	private View view;

	private LayoutInflater inflater = null;

	private ArrayList<ParseObject> quotesList;

	public UserQuoteListAdapter(Context context, ArrayList<ParseObject> quotesList) {
		this.context = context;
		this.quotesList = quotesList;
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

		final TextView quoteText = (TextView) view.findViewById(R.id.UserQuote_quoteText);
		final TextView showTitle = (TextView) view.findViewById(R.id.UserQuote_showTitle);
		TextView seasonEpisodeText = (TextView) view.findViewById(R.id.UserQuote_seasonEpisodeText);
		TextView authorText = (TextView) view.findViewById(R.id.UserQuote_authorText);
		ImageButton shareButton = (ImageButton) view.findViewById(R.id.UserQuote_shareButton);
		LinearLayout contentLayout = (LinearLayout) view.findViewById(R.id.UserQuote_contentLayout);

		if (PreferencesLoader.getInstance().getTheme() == 0) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_pink);
		} else if (PreferencesLoader.getInstance().getTheme() == 1) {
			contentLayout.setBackgroundResource(R.drawable.quote_border_white);
		} else {
			contentLayout.setBackgroundResource(R.drawable.quote_border_orange);
		}

		final ParseObject quote = quotesList.get(position);
		quoteText.setText(quote.get("text").toString());
		showTitle.setText(quote.get("title").toString());
		int season = Integer.parseInt(("".equals(quote.get("season").toString()) ? "0" : quote.get("season").toString()));
		int episode = Integer.parseInt(("".equals(quote.get("episode").toString()) ? "0" : quote.get("episode").toString()));
		if (season == 0) {
			seasonEpisodeText.setVisibility(View.VISIBLE);
			seasonEpisodeText.setText(context.getString(R.string.quote_episode) + " " + episode);
		}
		if (episode == 0) {
			seasonEpisodeText.setVisibility(View.VISIBLE);
			seasonEpisodeText.setText(context.getString(R.string.quote_season) + " " + season);
		}
		if (season != 0 && episode != 0) {
			seasonEpisodeText.setVisibility(View.VISIBLE);
			seasonEpisodeText.setText(context.getString(R.string.quote_season) + " " + season + ", " + context.getString(R.string.quote_episode)
					+ " " + episode);
		}
		if (season == 0 && episode == 0)
			seasonEpisodeText.setVisibility(View.GONE);
		authorText.setText(context.getString(R.string.quote_by) + " " + quote.get("user").toString());
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				shareQuote(context, showTitle.getText().toString(), quoteText.getText().toString());
			}
		});

		return view;
	}

	private static void shareQuote(Context context, String title, String quote) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = quote + " " + context.getString(R.string.share_text) + " - " + title;
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_with)));
	}
}
