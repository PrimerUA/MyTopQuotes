package top.quotes.pkg.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import top.quotes.pkg.R;
import top.quotes.pkg.adapters.QuoteListAdapter;
import top.quotes.pkg.adapters.RandomQuoteListAdapter;
import top.quotes.pkg.adapters.UserQuoteListAdapter;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.core.CoreFragment;
import top.quotes.pkg.data.Quote;
import top.quotes.pkg.data.Show;
import top.quotes.pkg.data.ShowsList;
import top.quotes.pkg.entity.UserQuote;
import top.quotes.pkg.server.Executor;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.controllers.LanguageController.LanguageChanger;
import top.quotes.pkg.util.providers.ConnectionProvider;
import top.quotes.pkg.util.providers.QuoteViewsProvider;
import top.quotes.pkg.util.providers.ShowsProvider;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.QuoteSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

public class MainFragment extends CoreFragment {

	private View rootView;

	private ListView leftList;
	private ListView rightList;

	private ImageView drawerImage;

	private List<UserQuote> userQuotesList;
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

		QuoteViewsProvider.clearOnScreenQuotesList();

		addQuotesOnScreen();

		return rootView;
	}

	private void addQuotesOnLeft() {
		ImageButton previousLeftAddButton = (ImageButton) leftColumnLayout.findViewWithTag("add_button_left");
		if (previousLeftAddButton != null) {
			leftColumnLayout.removeView(previousLeftAddButton);
		}
		for (int i = 0; i < ConstantsFacade.RANDOM_QUOTES_COUNT; i++) {
			leftColumnLayout.addView(QuoteViewsProvider.getQuoteView(rootView.getContext(), null, getLanguage(), false));
		}
		setLeftAddButton();
	}

	private void addQuotesOnRight() {
		ImageButton previousRightAddButton = (ImageButton) rightColumnLayout.findViewWithTag("add_button_right");
		if (previousRightAddButton != null) {
			rightColumnLayout.removeView(previousRightAddButton);
		}
		for (int i = 0; i < ConstantsFacade.RANDOM_QUOTES_COUNT; i++) {
			rightColumnLayout.addView(QuoteViewsProvider.getQuoteView(rootView.getContext(), null, getLanguage(), false));
		}
		setRightAddButton();
	}

	private void setRightAddButton() {
		ImageButton addButtonRight = new ImageButton(rootView.getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(1, 5, 1, 3);
		addButtonRight.setLayoutParams(params);
		addButtonRight.setTag("add_button_right");
		addButtonRight.setImageResource(android.R.drawable.ic_menu_add);
		if (getTheme() == 0) {
			addButtonRight.setBackgroundResource(R.drawable.quote_selector_pink);
		} else if (getTheme() == 0) {
			addButtonRight.setBackgroundResource(R.drawable.quote_selector_white);
		} else {
			addButtonRight.setBackgroundResource(R.drawable.quote_selector_orange);
		}
		addButtonRight.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				addQuotesOnRight();
			}
		});
		rightColumnLayout.addView(addButtonRight);
	}

	private void setLeftAddButton() {
		ImageButton addButtonLeft = new ImageButton(rootView.getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(1, 5, 1, 3);
		addButtonLeft.setLayoutParams(params);
		addButtonLeft.setTag("add_button_left");
		addButtonLeft.setImageResource(android.R.drawable.ic_menu_add);
		if (getTheme() == 0) {
			addButtonLeft.setBackgroundResource(R.drawable.quote_selector_pink);
		} else if (getTheme() == 0) {
			addButtonLeft.setBackgroundResource(R.drawable.quote_selector_white);
		} else {
			addButtonLeft.setBackgroundResource(R.drawable.quote_selector_orange);
		}
		addButtonLeft.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				addQuotesOnLeft();
			}
		});
		leftColumnLayout.addView(addButtonLeft);
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
		leftColumnLayout.removeAllViews();
		rightColumnLayout.removeAllViews();
		addQuotesOnLeft();
		addQuotesOnRight();
	}

	@Override
	protected void initFragment() {
		isEndLeft = false;
		isEndRight = false;

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
		boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
		if (tabletSize) {
			itemsQuantity = 25;
		} else {
			itemsQuantity = 10;
		}
		isEndLeft = false;
		isEndRight = false;
		if (ConnectionProvider.isConnectionAvailable(getActivity())) {
			userQuotesList = new Executor().list(0, itemsQuantity, LanguageController.getCurrentLanguage());
			leftList.setAdapter(new UserQuoteListAdapter(getActivity(), (ArrayList<UserQuote>) userQuotesList, false));
			userQuotesList = new Executor().list(itemsQuantity, itemsQuantity * 2, LanguageController.getCurrentLanguage());
			rightList.setAdapter(new UserQuoteListAdapter(getActivity(), (ArrayList<UserQuote>) userQuotesList, false));
		} else {
			leftList.setAdapter(new RandomQuoteListAdapter(getActivity()));
			rightList.setAdapter(new RandomQuoteListAdapter(getActivity()));
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
			List<UserQuote> newPostsLeft = new Executor().list(userQuotesList.size(), itemsQuantity, LanguageController.getCurrentLanguage());
			List<UserQuote> newPostsRight = new Executor().list(userQuotesList.size() + itemsQuantity, itemsQuantity, LanguageController.getCurrentLanguage());
			if (newPostsLeft.size() == 0) {
				isEndLeft = true;
				Toast.makeText(getActivity(), getString(R.string.all_items_loaded), Toast.LENGTH_SHORT).show();
			} else if (newPostsRight.size() == 0) {
				isEndRight = true;
				Toast.makeText(getActivity(), getString(R.string.all_items_loaded), Toast.LENGTH_SHORT).show();
			}else {
				userQuotesList.addAll(newPostsLeft);
				userQuotesList.addAll(newPostsRight);
				((RandomQuoteListAdapter) leftList.getAdapter()).notifyDataSetChanged();
				((RandomQuoteListAdapter) rightList.getAdapter()).notifyDataSetChanged();
			}
		} else {
			final Show show = ShowsList.getList().get(new Random().nextInt(ShowsList.SHOWS_LIST_SIZE));
			final Quote quote = show.getQuote(new Random().nextInt(ShowsList.getList().size()), LanguageController.getCurrentLanguage());
		}
		
	}

}