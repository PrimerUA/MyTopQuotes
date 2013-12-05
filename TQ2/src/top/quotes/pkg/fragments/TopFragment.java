package top.quotes.pkg.fragments;

import top.quotes.pkg.R;
import top.quotes.pkg.core.CoreFragment;
import top.quotes.pkg.util.Rating;
import top.quotes.pkg.util.controllers.FilesController;
import top.quotes.pkg.util.controllers.LanguageController;
import top.quotes.pkg.util.controllers.LanguageController.LanguageChanger;
import top.quotes.pkg.util.providers.QuoteRatingsProvider;
import top.quotes.pkg.util.providers.QuoteViewsProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class TopFragment extends CoreFragment implements LanguageChanger {

    private View rootView;

    private LinearLayout contentLayout;
    private ScrollView contentScroll;
    private ImageView drawerImage;

    private Button clearButton;
    private TextView emptyText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	rootView = inflater.inflate(R.layout.fragment_top, container, false);
	initFragment();

	LanguageController.setOnLanguageChangedListener(this);
	getSherlockActivity().setTitle(getResources().getString(R.string.fragment_top));

	addQuotesOnScreen();

	if (contentLayout.getChildCount() != 0) {
	    emptyText.setVisibility(View.GONE);
	    clearButton.setVisibility(View.VISIBLE);
	} else {
	    if (getTheme() == 0) {
		emptyText.setBackgroundResource(R.drawable.quote_pressed_pink);
	    } else {
		emptyText.setBackgroundResource(R.drawable.quote_pressed_white);
	    }
	    clearButton.setVisibility(View.GONE);
	}

	return rootView;
    }

    @Override
    public void onLanguageChanged() {
	updateContent();
    }

    @Override
    protected void initFragment() {
	contentLayout = (LinearLayout) rootView.findViewById(R.id.TopFragment_contentLayout);
	emptyText = (TextView) rootView.findViewById(R.id.TopFragment_emptyText);
	clearButton = (Button) rootView.findViewById(R.id.TopFragment_clearButton);
	contentScroll = (ScrollView) rootView.findViewById(R.id.TopFragment_scrollLayout);
	drawerImage = (ImageView) rootView.findViewById(R.id.TopFragment_drawerImage);

	if (getTheme() == 0) {
	    drawerImage.setBackgroundColor(Color.parseColor("#c92064"));
	    clearButton.setBackgroundResource(R.drawable.quote_selector_pink);
	} else {
	    drawerImage.setBackgroundColor(Color.DKGRAY);
	    clearButton.setBackgroundResource(R.drawable.quote_selector_white);
	}

	clearButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		FilesController.clearFile("ratingRUS1.txt");
		FilesController.clearFile("ratingRUS2.txt");
		FilesController.clearFile("ratingRUS3.txt");

		FilesController.clearFile("ratingENG1.txt");
		FilesController.clearFile("ratingENG2.txt");
		FilesController.clearFile("ratingENG3.txt");

		emptyText.setVisibility(View.VISIBLE);
		if (getTheme() == 0) {
		    emptyText.setBackgroundResource(R.drawable.quote_pressed_pink);
		} else {
		    emptyText.setBackgroundResource(R.drawable.quote_pressed_white);
		}
		clearButton.setVisibility(View.GONE);

		contentLayout.removeAllViews();
		QuoteRatingsProvider.clearArrays();
	    }
	});
    }

    @Override
    protected void addQuotesOnScreen() {
	if (LanguageController.getCurrentLanguage() == LanguageController.RUS) {
	    for (int i = 0; i < QuoteRatingsProvider.getRatingListRUS3().size(); i++) {
		View view = QuoteViewsProvider.getRatedQuoteView(rootView.getContext(), i, Rating.GOOD);
		if (view != null) {
		    contentLayout.addView(view);
		} else {
		    break;
		}
	    }
	    for (int i = 0; i < QuoteRatingsProvider.getRatingListRUS2().size(); i++) {
		View view = QuoteViewsProvider.getRatedQuoteView(rootView.getContext(), i, Rating.NORMAL);
		if (view != null) {
		    contentLayout.addView(view);
		} else {
		    break;
		}
	    }
	    for (int i = 0; i < QuoteRatingsProvider.getRatingListRUS1().size(); i++) {
		View view = QuoteViewsProvider.getRatedQuoteView(rootView.getContext(), i, Rating.BAD);
		if (view != null) {
		    contentLayout.addView(view);
		} else {
		    break;
		}
	    }
	} else {
	    for (int i = 0; i < QuoteRatingsProvider.getRatingListENG3().size(); i++) {
		View view = QuoteViewsProvider.getRatedQuoteView(rootView.getContext(), i, Rating.GOOD);
		if (view != null) {
		    contentLayout.addView(view);
		} else {
		    break;
		}
	    }
	    for (int i = 0; i < QuoteRatingsProvider.getRatingListENG2().size(); i++) {
		View view = QuoteViewsProvider.getRatedQuoteView(rootView.getContext(), i, Rating.NORMAL);
		if (view != null) {
		    contentLayout.addView(view);
		} else {
		    break;
		}
	    }
	    for (int i = 0; i < QuoteRatingsProvider.getRatingListENG1().size(); i++) {
		View view = QuoteViewsProvider.getRatedQuoteView(rootView.getContext(), i, Rating.BAD);
		if (view != null) {
		    contentLayout.addView(view);
		} else {
		    break;
		}
	    }
	}
    }

    @Override
    protected void updateContent() {
	if (getTheme() == 0) {
	    clearButton.setBackgroundResource(R.drawable.quote_selector_pink);
	} else {
	    clearButton.setBackgroundResource(R.drawable.quote_selector_white);
	}
	contentLayout.removeAllViews();
	addQuotesOnScreen();
	contentScroll.fullScroll(ScrollView.FOCUS_UP);
    }
}
