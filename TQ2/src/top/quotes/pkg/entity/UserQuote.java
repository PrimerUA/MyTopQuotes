package top.quotes.pkg.entity;

public class UserQuote {

	private String title;
	private String quote;
	private int season;
	private int episode;
	private int userId;
	private String userName;
	
	public String getText() {
		return quote;
	}

	public UserQuote setText(String quote) {
		this.quote = quote;
		return this;
	}

	public int getSeason() {
		return season;
	}

	public UserQuote setSeason(int season) {
		this.season = season;
		return this;
	}

	public int getEpisode() {
		return episode;
	}

	public UserQuote setEpisode(int episode) {
		this.episode = episode;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public UserQuote setTitle(String title) {
		this.title = title;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public UserQuote setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public UserQuote setUserName(String userName) {
		this.userName = userName;
		return this;
	}
}
