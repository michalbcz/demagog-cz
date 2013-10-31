package models;

import play.mvc.PathBindable;

/**
 * Type of content shown in list of quotes. <br>
 * Cannot be enum, because PathBindable requires public zero argument contructor.
 * 
 * @author vlasta
 *
 */
public class QuotesListContent implements PathBindable<QuotesListContent> {
	/**
	 * All approved quotes prepared for voting
	 */
	public static final QuotesListContent APPROVED = new QuotesListContent("hlasovani");
	
	/**
	 * All checked quotes.
	 */
	public static final QuotesListContent CHECKED = new QuotesListContent("overene");
	
	public static final QuotesListContent[] VALUES = new QuotesListContent[] {APPROVED, CHECKED};

	/**
	 * Part of URL for this content type;
	 */
	private final String path;
	
	/**
	 * Required because of PathBindable shitty pattern.
	 */
	public QuotesListContent() {
		this.path = null;
	}

	public QuotesListContent(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public String javascriptUnbind() {			
		return getPath();
	}

	@Override
	public String unbind(String key) {
		return getPath();
	}

	@Override
	public QuotesListContent bind(String key, String text) {
		return valueOf(text);
	}		
	
	private static QuotesListContent valueOf(String text) {
		for (QuotesListContent value : VALUES) {
			if (value.getPath().equals(text)) {
				return value;
			}
		}
		
		return null;
	}
}