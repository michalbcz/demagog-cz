package utils;

import models.Quote.QuoteState;

public class LabelProvider {
	
	private LabelProvider() {}
	
	public static String getLabel(QuoteState state) {
		if (state == null) {
			return null;
		};
		switch (state) {
			case APPROVED:
				return "Schválen";
				
			case CHECKED:
				return "Schvaluje se";
				
			case NEW:
				return "Nový";

		default:
			throw new IllegalArgumentException("Neznamy stav: " + state);
		}
	}
}
