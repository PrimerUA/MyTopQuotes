package top.quotes.pkg.fragments;

import top.quotes.pkg.R;
import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.core.CoreFragment;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.controllers.LanguageController.LanguageChanger;
import top.quotes.pkg.util.providers.QuoteViewsProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class MainFragment extends CoreFragment {

	private View rootView;

	private static LinearLayout leftColumnLayout;
	private static LinearLayout rightColumnLayout;

	private ScrollView leftScroll;
	private ScrollView rightScroll;

	private ImageView drawerImage;


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
		leftScroll.fullScroll(ScrollView.FOCUS_UP);
		rightScroll.fullScroll(ScrollView.FOCUS_UP);

		leftColumnLayout.removeAllViews();
		rightColumnLayout.removeAllViews();
		addQuotesOnLeft();
		addQuotesOnRight();
	}

	@Override
	protected void initFragment() {
		leftColumnLayout = (LinearLayout) rootView.findViewById(R.id.MainFragment_leftColumnLayout);
		rightColumnLayout = (LinearLayout) rootView.findViewById(R.id.MainFragment_rightColumnLayout);

		leftScroll = (ScrollView) rootView.findViewById(R.id.MainFragment_leftScrollLayout);
		rightScroll = (ScrollView) rootView.findViewById(R.id.MainFragment_rightScrollLayout);

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
		addQuotesOnLeft();
		addQuotesOnRight();
	}

}