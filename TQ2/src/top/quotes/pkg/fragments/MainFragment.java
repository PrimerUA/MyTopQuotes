package top.quotes.pkg.fragments;

import java.util.List;

import top.quotes.pkg.R;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.core.CoreFragment;
import top.quotes.pkg.data.Quote;
import top.quotes.pkg.entity.UserQuote;
import top.quotes.pkg.server.Executor;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.controllers.LanguageController.LanguageChanger;
import top.quotes.pkg.util.providers.ConnectionProvider;
import top.quotes.pkg.util.providers.QuoteViewsProvider;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class MainFragment extends CoreFragment {

	private View rootView;

	private ListView leftList;
	private ListView rightList;

	private ImageView drawerImage;
	
	private List<Quote>quotesList;
	private List<UserQuote>userQuotesList;
	
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
		if (!ConnectionProvider.isConnectionAvailable(getActivity())) {
			addQuotesOnLeft();
			addQuotesOnRight();
		} else {
			leftColumnLayout.removeAllViews();
			loadContentOnScreen();
		}
	}

	private void loadContentOnScreen() {
		final ProgressDialog myProgressDialog = ProgressDialog.show(getActivity(), getString(R.string.connection), getString(R.string.connection_loading_quote),
				true);
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
		userQuotesList = new Executor().list(from, length).getList(CaseListType.TYPE_GENERAL, 0, itemsQuantity,
				sortSpinner.getSelectedItemPosition(), type);
		if (caseBeanList == null) {
			MessageDialogs.showMessage(rootView.getContext(), getString(R.string.no_connection_title),
					getString(R.string.no_connection_text));
		} else {
			isEnd = false;
			contentList.setAdapter(new CasesListAdapter(getActivity(), caseBeanList));
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

			TextView infoText = (TextView) getActivity().getLayoutInflater().inflate(R.xml.empty_list_item, null);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(25, 25, 25, 25);
			infoText.setLayoutParams(params);
			if (contentList.getCount() == 0) {
				infoText.setTag(1);
				mainLayout.addView(infoText);
			} else {
				mainLayout.removeView(mainLayout.findViewWithTag(1));
			}
	}

}