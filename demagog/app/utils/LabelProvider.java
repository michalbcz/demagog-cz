package utils;

import models.Quote.QuoteState;

import org.springframework.util.Assert;

public class LabelProvider {

	private LabelProvider() {}

	public static String getDescription(QuoteState state) {
		Assert.notNull(state);

		switch (state) {
			case ANALYSIS_IN_PROGRESS:
				return "Výrok získal dostatečný počet hlasů a je ověřován.";

			case APPROVED_FOR_VOTING:
				return "Pro výrok lze hlasovat";

			case NEW:
				return "Nově přidaný výrok, který musí být schválen k hlasování.";

            case CHECKED_AND_PUBLISHED:
            	return "Výrok byl ověřen a je publikován na demagog.cz";

		    default:
			    throw new IllegalArgumentException("Neznamy stav: " + state);
		}
	}

	public static String getLabel(QuoteState state) {
		Assert.notNull(state);

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
