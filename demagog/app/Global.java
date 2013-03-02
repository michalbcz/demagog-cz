import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Locale;

import models.Quote;
import models.Quote.QuoteState;
import models.User;

import org.bson.types.ObjectId;

import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;

import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application arg0) {
		try {
			Field field = MorphiaLoggerFactory.class.getDeclaredField("loggerFactory");
			field.setAccessible(true);
			field.set(null, null);
		} catch (Exception e) {
			throw new RuntimeException("Failed to reset logger factory", e);
		}
		
		MorphiaLoggerFactory.registerLogger(SLF4JLogrImplFactory.class);
		
		initCustomFormatters();
		
		initDeveloperData();
	}
	
	private void initCustomFormatters() {
		Formatters.register(ObjectId.class, new Formatters.SimpleFormatter<ObjectId>() {

			@Override
			public ObjectId parse(String input, Locale l) throws ParseException {
				return new ObjectId(input);
			}

			@Override
			public String print(ObjectId id, Locale l) {
				return id.toString();
			}
		});
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
		
		String defaultUsername = System.getProperty("demagog.defaultUser");
		if (defaultUsername == null) {
			throw new IllegalStateException("System property demagog.defaultUser must be set.");
		}

		String defaultPassword = System.getProperty("demagog.defaultPassword");
		if (defaultPassword == null) {
			throw new IllegalStateException("System property demagog.defaultPassword must be set.");
		}
		
		new User(defaultUsername, defaultPassword).save();
	}
	
}
