package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticator;

public class UserAuthenticator extends Authenticator {
	
	private static final UserAuthenticator INTERNAL_INSTANCE = new UserAuthenticator();

	@Override
	public String getUsername(Context ctx) {
		return ctx.session().get("userId");
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Admin.login());
	}
	
	public static boolean isUserLoggedIn() {
		return INTERNAL_INSTANCE.getUsername(Context.current()) != null;
	}
	
}
