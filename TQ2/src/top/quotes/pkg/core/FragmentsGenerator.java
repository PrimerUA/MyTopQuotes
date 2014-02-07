package top.quotes.pkg.core;

import com.actionbarsherlock.app.SherlockFragment;

import top.quotes.pkg.constants.ConstantsFacade;
import top.quotes.pkg.fragments.MainFragment;
import top.quotes.pkg.fragments.QuizFragment;
import top.quotes.pkg.fragments.ShowsFragment;
import top.quotes.pkg.fragments.TopFragment;
import android.os.Bundle;

public class FragmentsGenerator {

    public static SherlockFragment generateFragment(int position) {
	if (position == 0) {
	    return new MainFragment();
	} else if (position == 1) {
	    return new QuizFragment();
	} else if (position == 2) {
	    return new TopFragment();
	} else {
	    SherlockFragment showsFragment = new ShowsFragment();
	    Bundle args = new Bundle();
	    args.putInt(ConstantsFacade.ARG_SHOWS_NUMBER, position - ConstantsFacade.MENU_ITEMS_NOT_SHOWS);
	    showsFragment.setArguments(args);
	    return showsFragment;
	}
    }
}
