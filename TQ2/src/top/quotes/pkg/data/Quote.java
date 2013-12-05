package top.quotes.pkg.data;

import top.quotes.pkg.util.Rating;

public class Quote {

	private String text;
	private Rating rating;

	public Quote() {
		
	}
	
	public Quote(String text, Rating rating) {
		super();
		this.text = text;
		this.rating = rating;
	}

	public Quote(Quote quote) {
		super();
		this.text = quote.getText();
		this.rating = quote.getRating();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}
}
