package top.quotes.pkg.fragments;

import top.quotes.pkg.R;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.core.CoreFragment;
import top.quotes.pkg.data.Show;
import top.quotes.pkg.data.ShowsList;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.controllers.LanguageController.LanguageChanger;
import top.quotes.pkg.util.providers.QuoteViewsProvider;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;

/**
 * Fragment that appears in the "content_frame", shows quotes for a specified
 * show
 */
public class ShowsFragment extends CoreFragment implements LanguageChanger {

	private View rootView;
	private LinearLayout contentLayout;
	private ImageView drawerImage;

	private int position;
	private Show show;

	private ScrollView contentScroll;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_shows, container, false);
		initFragment();

		position = getArguments().getInt(ConstantsFacade.ARG_SHOWS_NUMBER);
		show = ShowsList.getList().get(position);

		LanguageController.setOnLanguageChangedListener(this);
		getSherlockActivity().setTitle(show.getTitle(getLanguage()));

		QuoteViewsProvider.clearOnScreenQuotesList();

		addQuotesOnScreen();
		return rootView;
	}

	@Override
	public void onLanguageChanged() {
		updateContent();
	}

	@SuppressLint("NewApi")
	@Override
	protected void initFragment() {
		contentLayout = (LinearLayout) rootView.findViewById(R.id.ShowsFragment_contentLayout);
		contentScroll = (ScrollView) rootView.findViewById(R.id.ShowsFragment_scrollLayout);
		drawerImage = (ImageView) rootView.findViewById(R.id.ShowsFragment_drawerImage);

		if (getTheme() == 0) {
			drawerImage.setBackgroundColor(Color.parseColor("#c92064"));
		} else if (getTheme() == 1) {
			drawerImage.setBackgroundColor(Color.DKGRAY);
		} else {
			drawerImage.setBackgroundColor(Color.parseColor("#ff7400"));
		}

		getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(getLanguage().ordinal());
		
	}
	
	@Override
	protected void addQuotesOnScreen() {
		Button previousAddButton = (Button) contentLayout.findViewWithTag("add_button");
		if (previousAddButton != null) {
			contentLayout.removeView(previousAddButton);
		}
		Button addButton = new Button(rootView.getContext());
		int difference = show.getQuotesList(getLanguage()).size() - QuoteViewsProvider.getOnScreenQuotesListSize();
		if (difference > 10) {
			for (int i = 0; i < ConstantsFacade.ADD_QUOTES_COUNT; i++) {
				contentLayout.addView(QuoteViewsProvider.getQuoteView(rootView.getContext(), position, getLanguage(), true));
			}
		} else {
			for (int i = 0; i < difference; i++) {
				contentLayout.addView(QuoteViewsProvider.getQuoteView(rootView.getContext(), position, getLanguage(), true));
				addButton.setVisibility(View.GONE);
			}
		}

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(1, 5, 1, 3);
		addButton.setLayoutParams(layoutParams);
		addButton.setTag("add_button");
		addButton.setText(R.string.add_button);
		if (getTheme() == 0) {
			addButton.setBackgroundResource(R.drawable.quote_selector_pink);
		} else if (getTheme() == 1) {
			addButton.setBackgroundResource(R.drawable.quote_selector_white);
		} else {
			addButton.setBackgroundResource(R.drawable.quote_selector_orange);
		}
		addButton.setShadowLayer(2, 2, 2, Color.WHITE);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				addQuotesOnScreen();
			}
		});
		contentLayout.addView(addButton);
	}

	@Override
	protected void updateContent() {
		if (getTheme() == 0) {
			drawerImage.setBackgroundColor(Color.parseColor("#c92064"));
		} else if (getTheme() == 1) {
			drawerImage.setBackgroundColor(Color.DKGRAY);
		} else {
			drawerImage.setBackgroundColor(Color.parseColor("#ff7400"));
		}
		QuoteViewsProvider.clearOnScreenQuotesList();
		contentLayout.removeAllViews();
		addQuotesOnScreen();
		contentScroll.fullScroll(ScrollView.FOCUS_UP);
	}
	
}