import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Locale;

import models.User;

import org.bson.types.ObjectId;

import play.Application;
import play.GlobalSettings;
import play.data.format.Formatters;

import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory;

public class BaseGlobal extends GlobalSettings {

	@Override
	public void onStart(Application application) {
		try {
			Field field = MorphiaLoggerFactory.class.getDeclaredField("loggerFactory");
			field.setAccessible(true);
			field.set(null, null);
		} catch (Exception e) {
			throw new RuntimeException("Failed to reset logger factory", e);
		}

		MorphiaLoggerFactory.registerLogger(SLF4JLogrImplFactory.class);

		initCustomFormatters();

		initAdminUser();
	}

	private void initAdminUser() {
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
}
