import models.Quote;
import models.Quote.QuoteState;
import play.Application;

/**
 * Development specific settings
 *
 * @author vlasta
 *
 */
public class DevelopmentGlobal extends BaseGlobal {

	@Override
	public void onStart(Application application) {
		super.onStart(application);

		initDeveloperData();
	}

	private void initDeveloperData() {
		Quote.deleteAll();

		new Quote(
				"I po tomto daňovém balíčku, který byl tedy napodruhé prosazen, bude daňová zátěž o 2% daňové kvóty nižší, než byla za pana ministra Sobotky.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Miroslav Kalousek",
				QuoteState.NEW).save();

		new Quote(
				"My jsme zdědili deficit po Fischerově vládě ve výši 5,8% HDP.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Miroslav Kalousek",
				QuoteState.NEW).save();

		new Quote(
				"Každý správce programu (operačního programu, čerpajícího evropské fondy, pozn.), máme jich bohužel strašně moc, máme jich 24, má svého auditora, který provádí kontrolu, a to bývá většinou podřízený toho správce programu.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Jiří Paroubek",
				QuoteState.NEW).save();

		new Quote(
				"Ale ten ekonomický mechanismus (převodu finančních prostředků státního podniku do státního rozpočtu, pozn.) tam neexistuje a to právě je předmětem teď novely zákona o státním podniku, který už je ve sněmovně a který má právě tento transfer zajistit.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Jiří Oliva",
				QuoteState.NEW).save();

		new Quote(
				"I po tomto daňovém balíčku, který byl tedy napodruhé prosazen, bude daňová zátěž o 2% daňové kvóty nižší, než byla za pana ministra Sobotky.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Miroslav Kalousek",
				QuoteState.APPROVED_FOR_VOTING).save();

		new Quote(
				"My jsme zdědili deficit po Fischerově vládě ve výši 5,8% HDP.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Miroslav Kalousek",
				QuoteState.APPROVED_FOR_VOTING).save();

		new Quote(
				"Každý správce programu (operačního programu, čerpajícího evropské fondy, pozn.), máme jich bohužel strašně moc, máme jich 24, má svého auditora, který provádí kontrolu, a to bývá většinou podřízený toho správce programu.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Jiří Paroubek",
				QuoteState.APPROVED_FOR_VOTING).save();

		new Quote(
				"Ale ten ekonomický mechanismus (převodu finančních prostředků státního podniku do státního rozpočtu, pozn.) tam neexistuje a to právě je předmětem teď novely zákona o státním podniku, který už je ve sněmovně a který má právě tento transfer zajistit.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Jiří Oliva",
				QuoteState.APPROVED_FOR_VOTING).save();

	}
}
