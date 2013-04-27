import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Locale;

import models.User;

import org.bson.types.ObjectId;

import play.Application;
import play.GlobalSettings;
import play.Play;
import play.data.format.Formatters;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

import com.google.code.morphia.logging.MorphiaLoggerFactory;
import com.google.code.morphia.logging.slf4j.SLF4JLogrImplFactory;

public class BaseGlobal extends GlobalSettings {

	private static final int CODE_NOT_FOUND = 404;
	private static final int CODE_BAD_REQUEST = 400;
	private static final int CODE_INTERNAL_ERROR = 500;
	
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
	
	@Override
	public Result onError(RequestHeader request, Throwable t) {
		return Results.internalServerError(views.html.system.error.render(request, CODE_INTERNAL_ERROR));
	}

	@Override
	public Result onHandlerNotFound(RequestHeader request) {
		return Results.notFound(views.html.system.error.render(request, CODE_NOT_FOUND));
	} 

	@Override
	public Result onBadRequest(RequestHeader request, String error) {
		return Results.badRequest(views.html.system.error.render(request, CODE_BAD_REQUEST));
	}

    @Override
    public Action<?> onRequest(Http.Request request, Method method) {
        return new Action.Simple() {
            @Override
			public Result call(Http.Context context) throws Throwable {
                Http.Response response = context.response();
                enableCors(response);
                return delegate.call(context);
            }
        };
    }

    /**
     *
     * enable CORS (Cross-Origin Resource Sharing) - see
     * http://enable-cors.org/index.html
     * with this post and other request can be called from javascript running on other domains
     * eg. our bookmarklet use this to send selected quote to our server
     *
     * @param response
     */
    private void enableCors(Http.Response response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "accept, origin, content-type");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    }

    private void initAdminUser() {
		createAdminUser().save();
	}

    protected User createAdminUser() {

        String defaultUsername = getConfigurationParameter("demagog.defaultUser");
        if (defaultUsername == null) {
            throw new IllegalStateException("System property demagog.defaultUser must be set.");
        }

        String defaultPassword = getConfigurationParameter("demagog.defaultPassword");
        if (defaultPassword == null) {
            throw new IllegalStateException("System property demagog.defaultPassword must be set.");
        }

        return new User(defaultUsername, defaultPassword);
    }

    private String getConfigurationParameter(String parameterKey) {
        return Play.application().configuration().getString(parameterKey);
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
