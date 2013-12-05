package top.quotes.pkg.data;

public class DailyQuote extends Quote {

	private String showTitle;

	public DailyQuote(String showTitle, Quote quote) {
		super(quote);
		this.showTitle = showTitle;
	}

	public String getShowTitle() {
		return showTitle;
	}

	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}
	
}
