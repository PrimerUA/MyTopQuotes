package top.quotes.pkg.data;

import java.util.ArrayList;

import top.quotes.pkg.util.controllers.LanguageController;

public class Show {

    private int id;
    private final String titleRUS;
    private final String titleENG;
    private ArrayList<Quote> quotesRUS;
    private ArrayList<Quote> quotesENG;
    private int background;

    public Show(int id, String titleRUS, String titleENG, ArrayList<Quote> quotesRUSList, ArrayList<Quote> quotesENGList, int backgroundResourceId) {
	this.setId(id);
	this.titleRUS = titleRUS;
	this.titleENG = titleENG;
	this.setQuotesRus(quotesRUSList);
	this.setQuotesEng(quotesENGList);
	this.background = backgroundResourceId;
    }

    public String getTitleRUS() {
	return titleRUS;
    }

    public String getTitleENG() {
	return titleENG;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getTitle(LanguageController language) {
	if (language == LanguageController.RUS) {
	    return titleRUS;
	} else {
	    return titleENG;
	}
    }

    public ArrayList<Quote> getQuotesList(LanguageController language) {
	if (language == LanguageController.RUS) {
	    return quotesRUS;
	} else {
	    return quotesENG;
	}
    }

    public ArrayList<Quote> getQuotesListRUS() {
	return quotesRUS;
    }

    public ArrayList<Quote> getQuotesListENG() {
	return quotesENG;
    }

    public void setQuotesRus(ArrayList<Quote> quotesRUS) {
	this.quotesRUS = quotesRUS;
    }

    public void setQuotesEng(ArrayList<Quote> quotesENG) {
	this.quotesENG = quotesENG;
    }

    public Quote getQuote(int i, LanguageController language) {
	if (language == LanguageController.RUS) {
	    return quotesRUS.get(i);
	} else {
	    return quotesENG.get(i);
	}
    }

    public int getBackground() {
	return background;
    }

    public void setBackground(int background) {
	this.background = background;
    }

}
