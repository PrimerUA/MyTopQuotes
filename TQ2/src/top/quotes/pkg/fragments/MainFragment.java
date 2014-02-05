package top.quotes.pkg.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import top.quotes.pkg.NewQuoteScreen;
import top.quotes.pkg.R;
import top.quotes.pkg.adapters.QuoteListAdapter;
import top.quotes.pkg.adapters.UserQuoteListAdapter;
import top.quotes.pkg.core.CoreFragment;
import top.quotes.pkg.data.Quote;
import top.quotes.pkg.data.Show;
import top.quotes.pkg.data.ShowsList;
import top.quotes.pkg.entity.UserQuote;
import top.quotes.pkg.server.Executor;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.controllers.LanguageController.LanguageChanger;
import top.quotes.pkg.util.providers.ConnectionProvider;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainFragment extends CoreFragment implements OnClickListener {

	private View rootView;

	private ListView leftList;
	private ListView rightList;

	private ImageView drawerImage;

	private List<UserQuote> userQuotesList;
	private List<Quote> quotesList;
	private List<String> titlesList;

	private Button newQuoteButton;
	private ImageButton refreshButton;

	private boolean isEndLeft;
	private boolean isEndRight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main, container, false);
		initFragment();

		getSherlockActivity().setTitle(getString(R.string.fragment_stream));

		LanguageController.setOnLanguageChangedListener(new LanguageChanger() {

			@Override
			public void onLanguageChanged() {
				updateContent();
			}
		});

		addQuotesOnScreen();
		return rootView;
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
	}

	@Override
	protected void initFragment() {
		isEndLeft = false;
		isEndRight = false;

		quotesList = new ArrayList<Quote>();
		userQuotesList = new ArrayList<UserQuote>();
		titlesList = new ArrayList<String>();

		newQuoteButton = (Button) rootView.findViewById(R.id.MainFragment_newQuoteButton);
		newQuoteButton.setOnClickListener(this);
		refreshButton = (ImageButton) rootView.findViewById(R.id.MainFragment_refreshButton);
		refreshButton.setOnClickListener(this);

		leftList = (ListView) rootView.findViewById(R.id.MainFragment_leftList);
		rightList = (ListView) rootView.findViewById(R.id.MainFragment_rightList);

		drawerImage = (ImageView) rootView.findViewById(R.id.MainFragment_drawerImage);

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
		final ProgressDialog myProgressDialog = ProgressDialog.show(getActivity(), getString(R.string.connection),
				getString(R.string.connection_loading_quote), true);
		new Thread() {
			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						doLoadContent();
						myProgressDialog.dismiss();
					}
				});
			}
		}.start();
	}

	private void doLoadContent() {
		int itemsQuantity = 0;
		boolean tabletSize = getActivity().getResources().getBoolean(R.bool.isTablet);
		if (tabletSize) {
			itemsQuantity = 25;
		} else {
			itemsQuantity = 10;
		}
		isEndLeft = false;
		isEndRight = false;
		if (ConnectionProvider.isConnectionAvailable(getActivity())) {
			newQuoteButton.setVisibility(View.VISIBLE);
			refreshButton.setVisibility(View.VISIBLE);
			userQuotesList = new Executor().list(0, itemsQuantity, LanguageController.getCurrentLanguage().ordinal());
			leftList.setAdapter(new UserQuoteListAdapter(getActivity(), (ArrayList<UserQuote>) userQuotesList, false));
			userQuotesList = new Executor().list(itemsQuantity, itemsQuantity * 2, LanguageController.getCurrentLanguage().ordinal());
			rightList.setAdapter(new UserQuoteListAdapter(getActivity(), (ArrayList<UserQuote>) userQuotesList, false));
		} else {
			newQuoteButton.setVisibility(View.GONE);
			refreshButton.setVisibility(View.GONE);
			updateQuoteList(itemsQuantity);
			leftList.setAdapter(new QuoteListAdapter(getActivity(), (ArrayList<String>) titlesList, (ArrayList<Quote>) quotesList, false));
			rightList.setAdapter(new QuoteListAdapter(getActivity(), (ArrayList<String>) titlesList, (ArrayList<Quote>) quotesList, false));
		}

		leftList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			private int lastSavedFirst = -1;

			@Override
			public void onScroll(final AbsListView view, final int first, final int visible, final int total) {
				if (!isEndLeft && (visible < total) && (first + visible == total) && (first != lastSavedFirst)) {
					lastSavedFirst = first;
					addItemsOnScreen();
				}
			}
		});
		rightList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			private int lastSavedFirst = -1;

			@Override
			public void onScroll(final AbsListView view, final int first, final int visible, final int total) {
				if (!isEndRight && (visible < total) && (first + visible == total) && (first != lastSavedFirst)) {
					lastSavedFirst = first;
					addItemsOnScreen();
				}
			}
		});
	}

	protected void addItemsOnScreen() {
		final ProgressDialog myProgressDialog = ProgressDialog.show(getActivity(), getString(R.string.connection),
				getString(R.string.connection_loading_quote), true);
		new Thread() {
			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						doAddItems();
						myProgressDialog.dismiss();
					}
				});
			}
		}.start();
	}

	protected void doAddItems() {
		int itemsQuantity = 0;
		boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
		if (tabletSize) {
			itemsQuantity = 10;
		} else {
			itemsQuantity = 5;
		}
		if (ConnectionProvider.isConnectionAvailable(getActivity())) {
			List<UserQuote> newPostsLeft = new Executor().list(userQuotesList.size(), itemsQuantity, LanguageController.getCurrentLanguage().ordinal());
			List<UserQuote> newPostsRight = new Executor().list(userQuotesList.size() + itemsQuantity, itemsQuantity, LanguageController.getCurrentLanguage()
					.ordinal());
			if (newPostsLeft.size() == 0) {
				isEndLeft = true;
				Toast.makeText(getActivity(), getString(R.string.all_items_loaded), Toast.LENGTH_SHORT).show();
			} else if (newPostsRight.size() == 0) {
				isEndRight = true;
				Toast.makeText(getActivity(), getString(R.string.all_items_loaded), Toast.LENGTH_SHORT).show();
			} else {
				userQuotesList.addAll(newPostsLeft);
				userQuotesList.addAll(newPostsRight);
				((UserQuoteListAdapter) leftList.getAdapter()).notifyDataSetChanged();
				((UserQuoteListAdapter) rightList.getAdapter()).notifyDataSetChanged();
			}
		} else {
			updateQuoteList(itemsQuantity);
			((QuoteListAdapter) leftList.getAdapter()).notifyDataSetChanged();
			((QuoteListAdapter) rightList.getAdapter()).notifyDataSetChanged();
		}
	}

	private void updateQuoteList(int itemsQuantity) {
		for (int i = 0; i < itemsQuantity; i++) {
			Show show = ShowsList.getList().get(new Random().nextInt(ShowsList.SHOWS_LIST_SIZE));
			titlesList.add(show.getTitle(getLanguage()));
			Quote quote = show.getQuote(new Random().nextInt(ShowsList.getList().size()), LanguageController.getCurrentLanguage());
			if (!quotesList.contains(quote))
				quotesList.add(quote);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.MainFragment_newQuoteButton:
			startActivity(new Intent(getActivity(), NewQuoteScreen.class));
			break;
		case R.id.MainFragment_refreshButton:
			addQuotesOnScreen();
			break;
		}
	}

}