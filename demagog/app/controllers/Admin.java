package controllers;

import java.util.Date;
import java.util.List;

import models.Quote;
import models.Quote.QuoteState;
import models.User;

import org.bson.types.ObjectId;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import utils.DBHolder;
import views.html.loginForm;
import views.html.quotes_list;

import com.google.code.morphia.Key;
import com.google.code.morphia.query.UpdateOperations;

public class Admin extends Controller {
	
	public static Result login() {
		if (UserAuthenticator.isUserLoggedIn()) {
			return redirect(controllers.routes.Admin.showNewlyAddedQuotes());
		}
		return ok(loginForm.render());
	}
	
	public static Result authenticate() {
        final User formUser = form(User.class).bindFromRequest().get();
        final User user = User.findByName(formUser.getUsername());

        if (user == null) {
        	flash("error", "Zadali jste špatné uživatelské jméno");
        } else {
        	if (formUser.getPassword().equals(user.getPassword())) {
                session("userId", user.getId().toString());
                return redirect(controllers.routes.Admin.showNewlyAddedQuotes());
            } else {
            	flash("error", "Zadali jste špatné heslo");
            }
        }
        return redirect(controllers.routes.Admin.login());
    }
	
	public static Result logout() {
		session().clear();
		return redirect(controllers.routes.Application.showApprovedQuotes());
	}
	
	@Authenticated(UserAuthenticator.class)
	public static Result reject() {
		String id = request().body().asFormUrlEncoded().get("id")[0];
			
		Quote.delete(new ObjectId(id), false);
			
		return redirect(controllers.routes.Admin.showNewlyAddedQuotes());
	}

	@Authenticated(UserAuthenticator.class)
	public static Result setChecked() {
		String id = request().body().asFormUrlEncoded().get("id")[0];
		
		Quote.setChecked(new ObjectId(id));	
		
		return redirect(controllers.routes.Admin.showApprovedQuotes());
	}

	@Authenticated(UserAuthenticator.class)
    public static Result updateQuote() {
        Quote quote = form(Quote.class).bindFromRequest().get();
		
        final UpdateOperations<Quote> updateOperations =
                DBHolder.ds.createUpdateOperations(Quote.class)
                        .set("quoteText", quote.quoteText)
                        .set("demagogBacklinkUrl", quote.demagogBacklinkUrl)
                        .set("author", quote.author)
                        .set("lastUpdateDate", new Date());
        
        if (request().body().asFormUrlEncoded().containsKey("approved")) {
        	updateOperations.set("quoteState", QuoteState.APPROVED_FOR_VOTING).set("approvalDate", new Date());
        }

        DBHolder.ds.update(new Key<Quote>(Quote.class, quote.id), updateOperations);

        return redirect(routes.Admin.showNewlyAddedQuotes());
    }

	@Authenticated(UserAuthenticator.class)
	public static Result showQuotes(QuoteState state) {
		List<Quote> quotes = Quote.findAllWithStateOrderedByCreationDate(state);
		
		return ok(quotes_list.render(quotes, true, null, null, null, null));
	}
	
	@Authenticated(UserAuthenticator.class)
	public static Result showNewlyAddedQuotes() {
		return showQuotes(QuoteState.NEW);
	}

	@Authenticated(UserAuthenticator.class)
	public static Result showApprovedQuotes() {
		return showQuotes(QuoteState.APPROVED_FOR_VOTING);
	}
	
}
