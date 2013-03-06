package utils;

import models.Quote.QuoteState;

public class LabelProvider {
	
	private LabelProvider() {}
	
	public static String getLabel(QuoteState state) {

        if (state == null) {
			throw new IllegalArgumentException("State cannot be null");
		}

		switch (state) {
			case ANALYSIS_IN_PROGRESS:
				return "Ověřuje se";
			
			case APPROVED_FOR_VOTING:
				return "V hlasování";
				
			case NEW:
				return "Nový";

            case CHECKED_AND_PUBLISHED:
                return "Ověřeno";

		    default:
			    throw new IllegalArgumentException("Neznamy stav: " + state);
		}

	}
}
