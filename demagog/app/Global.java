import java.lang.reflect.Field;

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
	}
	
}
