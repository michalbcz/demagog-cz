import java.util.Calendar;

import models.Quote;
import models.Quote.QuoteState;
import models.User;
import play.Application;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

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

	@Override
	public Result onError(RequestHeader request, Throwable t) {
		// to get default play behaviour
		return null;
	}

	@Override
	public Result onHandlerNotFound(RequestHeader request) {
		// to get default play behaviour
		return null;
	} 

	@Override
	public Result onBadRequest(RequestHeader request, String error) {
		// to get default play behaviour
		return null;
	} 
	
    @Override
    protected User createAdminUser() {
        return new User("test", "test");
    }

    private void initDeveloperData() {
		Quote.deleteAll();
		final Calendar calendar = Calendar.getInstance();

		new Quote(
				"Útočníci by si měli uvědomit, že svým jednáním nepoškozují Cermat. Doplatí na ně pouze maturanti, kteří se připravují na zkoušku a na webových stránkách si chtěli vyzkoušet ilustrační testy, nebo hledali další informace, které jim mohou pomoci maturitu úspěšně zvládnout.",
				"http://zpravy.idnes.cz/hackeri-napadli-web-maturit-dfv-/domaci.aspx?c=A130428_115154_zahranicni_jj",
				"Petr Habáň",
				QuoteState.NEW
		).save();

		new Quote(
				"Jsem nadšený, že jsem se konečně probojoval do semifinále. Je to dobré, ale ještě nejsem spokojený. Chci výš a doufám, že to půjde co nejdál.",
				"http://sport.aktualne.centrum.cz/skvely-rosol-si-zahraje-o-finale-berdych-podlehl-robredovi/r~ce21cb34ae6411e29bd9002590604f2e",
				"Lukáš Rosol",
				QuoteState.NEW
		).save();

		new Quote(
				"Studená fronta oddělující chladný vzduch v severozápadní Evropě od teplého na jihovýchodě kontinentu bude počasí u nás ovlivňovat nejméně do středy. I nadále proto očekáváme teplotní rozdíly mezi chladnějším západem a teplejším východem území.",
				"http://aktualne.centrum.cz/domaci/pocasi/clanek.phtml?id=778207#utm_source=centrumHP&utm_medium=dynamicleadbox&utm_campaign=A&utm_term=position-1",
				"Dagmar Honsová",
				QuoteState.NEW
		).save();


        new Quote(
                "citat s dlouhym zdrojovim url - url by melo byt zkraceno",
                "http://www.lidovky.cz/nikdo-uz-snad-nemuze-spekulovat-ze-mi-jde-o-funkce-rika-k-odchodu-stastny-14e-/zpravy-domov.aspx?c=A130425_121736_ln_domov_ogo",
                "AutorovoJmeno AutorovoPrijmeni",
                QuoteState.APPROVED_FOR_VOTING
        ).save();

		Quote quote = new Quote(
				"Dokud byly smartphony a tablety používány tak, jak je Pán Jobs stvořiti ráčil, tedy v pazouře, to se nám to smálo. Jenomže čím dál tím více se objevují šílenci, kteří je používají zvráceným a Bohu se příčícím způsobem, jako náhradu televize, media " +
                "playerů, obrázkových rámečků, laptopů a dokonce i desktopů.",
				"http://pctuning.tyden.cz/hardware/notebooky-pda/26828-ocima-uzivatele-srovnani-stojanku-pro-tablety-a-mobily",
				"Michal Rybka",
				QuoteState.APPROVED_FOR_VOTING
		);

		calendar.add(Calendar.MONTH, -5);
		quote.approvalDate = calendar.getTime();
		quote.save();

		quote = new Quote(
				"Každý správce programu (operačního programu, čerpajícího evropské fondy, pozn.), máme jich bohužel strašně moc, máme jich 24, má svého auditora, který provádí kontrolu, a to bývá většinou podřízený toho správce programu.",
				"http://www.miroslav-kalousek.cz/clanek225",
				"Miroslav kalousek",
				QuoteState.APPROVED_FOR_VOTING
		);
		calendar.add(Calendar.MONTH, 2);
		quote.approvalDate = calendar.getTime();
		quote.save();

		quote = new Quote(
				"Ale ten ekonomický mechanismus (převodu finančních prostředků státního podniku do státního rozpočtu, pozn.) tam neexistuje a to právě je předmětem teď novely zákona o státním podniku, který už je ve sněmovně a který má právě tento transfer zajistit.",
				"http://www.vlada.cz/cz/ppov/ekonomicka-rada/clanky/rozhovor-s-miroslavem-zamecnikem-lesni-zisky-i-podezreni-101086/",
				"Jiří Oliva",
				QuoteState.APPROVED_FOR_VOTING
		);
		calendar.add(Calendar.MONTH, -5);
		quote.approvalDate = calendar.getTime();
		quote.save();
		
		quote = new Quote(
				"To číslo je tak absurdní, že se mi nechce věřit, že je reálné. Se zápasem budeme souhlasit jedině v případě, že bude tato částka složena u notáře. Pokud by mělo dojít na platbu po zápase, nebudeme souhlasit.",
				"http://sport.aktualne.centrum.cz/klicka-nabidka-soupere-sokovala-rekordni-ale-neni/r~d241abd8afc411e29ed5002590604f2e/?utm_source=centrumHP&utm_medium=dynamicleadbox&utm_campaign=A&utm_term=position-13",
				"Bernd Bönte",
				QuoteState.ANALYSIS_IN_PROGRESS
		);
		calendar.add(Calendar.DAY_OF_YEAR, -17);
		quote.approvalDate = calendar.getTime();
		quote.save();
		
		quote = new Quote(
				"I po tomto daňovém balíčku, který byl tedy napodruhé prosazen, bude daňová zátěž o 2% daňové kvóty nižší, než byla za pana ministra Sobotky.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek",
				"Miroslav Kalousek",
				QuoteState.CHECKED_AND_PUBLISHED
		);
		calendar.add(Calendar.DAY_OF_YEAR, 6);
		quote.approvalDate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_YEAR, 2);
		quote.publishedDate = calendar.getTime();
		quote.demagogBacklinkUrl = "http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek";
		quote.save();
	}
}
