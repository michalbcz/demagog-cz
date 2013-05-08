import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Locale;

import models.User;

import org.bson.types.ObjectId;
import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.play.Config;

import play.Application;
import play.GlobalSettings;
import play.Play;
import play.data.format.Formatters;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

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

		initOAuth();

		initCustomFormatters();

		initAdminUser();
	}

    @Override
    public Action onRequest(Http.Request request, Method method) {

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

	private void initOAuth() {
		// OAuth
//		  FacebookClient facebookClient = new FacebookClient("fb_key", "fb_secret");
		  Google2Client googleClient = new Google2Client("514697974066-rvf8s83h65uhhv40f46aeukgn59756ul.apps.googleusercontent.com", "ROQUI8qFY_ofC-Hu4-xV13Yy");
//		  TwitterClient twitterClient = new TwitterClient("tw_key", "tw_secret");

		  final Clients clients = new Clients("http://localhost:9000/oauth2callback", googleClient);
		  Config.setClients(clients);
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
