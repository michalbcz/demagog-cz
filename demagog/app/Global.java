import java.lang.reflect.Field;

import models.Quote;

import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory;

import play.Application;
import play.GlobalSettings;

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
		
		initDeveloperData();
	}
	
	private void initDeveloperData() {
		Quote.deleteAll();
		
		new Quote(
				"I po tomto daňovém balíčku, který byl tedy napodruhé prosazen, bude daňová zátěž o 2% daňové kvóty nižší, než byla za pana ministra Sobotky.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek", 
				"Miroslav Kalousek").save();

		new Quote(
				"My jsme zdědili deficit po Fischerově vládě ve výši 5,8% HDP.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek", 
				"Miroslav Kalousek").save();

		new Quote(
				"Každý správce programu (operačního programu, čerpajícího evropské fondy, pozn.), máme jich bohužel strašně moc, máme jich 24, má svého auditora, který provádí kontrolu, a to bývá většinou podřízený toho správce programu.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek", 
				"Jiří Paroubek").save();

		new Quote(
				"Ale ten ekonomický mechanismus (převodu finančních prostředků státního podniku do státního rozpočtu, pozn.) tam neexistuje a to právě je předmětem teď novely zákona o státním podniku, který už je ve sněmovně a který má právě tento transfer zajistit.",
				"http://demagog.cz/diskusie/76/trvale-udrzitelna-krize-miroslav-kalousek", 
				"Jiří Oliva").save();
	}
	
}
