package top.quotes.pkg.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import top.quotes.pkg.R;
import top.quotes.pkg.adapters.QuoteListAdapter;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.core.CoreFragment;
import top.quotes.pkg.data.Quote;
import top.quotes.pkg.data.Show;
import top.quotes.pkg.data.ShowsList;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.controllers.LanguageController.LanguageChanger;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;

public class ShowsFragment extends CoreFragment implements LanguageChanger {

	private View rootView;
	private ListView contentList;
	private ImageView drawerImage;

	private int position;
	private Show show;
	private List<Quote> quotesList;
	private List<String> titlesList;

	private boolean isEnd;
	private int itemsQuantity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_shows, container, false);
		initFragment();

		position = getArguments().getInt(ConstantsFacade.ARG_SHOWS_NUMBER);
		show = ShowsList.getList().get(position);

		LanguageController.setOnLanguageChangedListener(this);
		getSherlockActivity().setTitle(show.getTitle(getLanguage()));

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
		isEnd = false;

		boolean tabletSize = getActivity().getResources().getBoolean(R.bool.isTablet);
		if (tabletSize) {
			itemsQuantity = 25;
		} else {
			itemsQuantity = 10;
		}

		contentList = (ListView) rootView.findViewById(R.id.ShowsFragment_contentList);
		drawerImage = (ImageView) rootView.findViewById(R.id.ShowsFragment_drawerImage);

		quotesList = new ArrayList<Quote>();
		titlesList = new ArrayList<String>();

		if (getTheme() == 0) {
			drawerImage.setBackgroundColor(Color.parseColor("#c92064"));
		} else if (getTheme() == 1) {
			drawerImage.setBackgroundColor(Color.DKGRAY);
		} else {
			drawerImage.setBackgroundColor(Color.parseColor("#ff7400"));
		}

		getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(getLanguage().ordinal());

		contentList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			private int lastSavedFirst = -1;

			@Override
			public void onScroll(final AbsListView view, final int first, final int visible, final int total) {
				if (!isEnd && (visible < total) && (first + visible == total) && (first != lastSavedFirst)) {
					lastSavedFirst = first;
					addItemsOnScreen();
				}
			}
		});
	}

	@Override
	protected void addQuotesOnScreen() {
		quotesList.clear();
		for (int i = 0; i < itemsQuantity; i++) {
			titlesList.add(show.getTitle(getLanguage()));
			Quote quote = show.getQuote(new Random().nextInt(ShowsList.SHOWS_LIST_SIZE), LanguageController.getCurrentLanguage());
			if (!quotesList.contains(quote))
				quotesList.add(quote);
		}
		contentList.setAdapter(new QuoteListAdapter(getActivity(), (ArrayList<String>) titlesList, (ArrayList<Quote>) quotesList));
	}

	protected void addItemsOnScreen() {
		for (int i = 0; i < itemsQuantity; i++) {
			titlesList.add(show.getTitle(getLanguage()));
			Quote quote = show.getQuote(new Random().nextInt(ShowsList.SHOWS_LIST_SIZE), LanguageController.getCurrentLanguage());
			if (!quotesList.contains(quote))
				quotesList.add(quote);
		}
		((QuoteListAdapter) contentList.getAdapter()).notifyDataSetChanged();
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
		addQuotesOnScreen();
	}

}