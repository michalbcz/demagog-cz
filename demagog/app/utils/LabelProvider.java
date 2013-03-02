package utils;

import models.Quote.QuoteState;

public class LabelProvider {
	
	private LabelProvider() {}
	
	public static String getLabel(QuoteState state) {
		if (state == null) {
			return null;
		};
		switch (state) {
			case CHECKED:
				return "Ověřen";
			
			case APPROVED:
				return "V hlasování";
				
			case NEW:
				return "Nový";

		default:
			throw new IllegalArgumentException("Neznamy stav: " + state);
		}
	}
}