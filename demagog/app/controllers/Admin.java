package controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Quote;
import models.Quote.QuoteState;
import models.QuotesListContent;
import models.User;

import org.apache.commons.lang3.BooleanUtils;
import org.bson.types.ObjectId;

import play.Configuration;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http.Request;
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
		final Map<String, String[]> requestContent = request().body().asFormUrlEncoded();
		
		String id = requestContent.get("id")[0];

		Quote.delete(new ObjectId(id), false);

		if (requestContent.containsKey("fullEditable") && BooleanUtils.toBoolean(requestContent.get("fullEditable")[0])) {
        	return redirect(routes.Admin.showAllQuotes());
        }
		
		Quote oldQuote = Quote.findById(new ObjectId(id));
		
        return redirectByQuoteState(oldQuote.quoteState);
	}

	@Authenticated(UserAuthenticator.class)
    public static Result updateQuote() {
        Quote quote = form(Quote.class).bindFromRequest().get();

        final UpdateOperations<Quote> updateOperations = DBHolder.ds.createUpdateOperations(Quote.class);
        updateOperations.set("lastUpdateDate", new Date());
        
        if (quote.quoteText != null) {
        	updateOperations.set("quoteText", quote.quoteText);
        }
        if (quote.demagogBacklinkUrl != null) {
        	updateOperations.set("demagogBacklinkUrl", quote.demagogBacklinkUrl);
        }
        if (quote.author != null) {
        	updateOperations.set("author", quote.author);
        }
        if (quote.url != null) {
        	updateOperations.set("url", quote.url);
        }

        Quote oldQuote = Quote.findById(quote.id);
        
        final String stateProperty = "quoteState";

        if (isQuoteFormToGetApproved(request())) {
        	updateOperations.set(stateProperty, QuoteState.APPROVED_FOR_VOTING);
        	if (oldQuote.quoteState != QuoteState.APPROVED_FOR_VOTING) {
        		updateOperations.set("approvalDate", new Date());
        	}
        } else {
        	updateOperations.set(stateProperty, QuoteState.NEW);
        }
        if (isQuoteFormToGetAnalyze(request())) {
        	updateOperations.set(stateProperty, QuoteState.ANALYSIS_IN_PROGRESS);
        } else {
        	if (isQuoteFormToGetApproved(request())) {
        		updateOperations.set(stateProperty, QuoteState.APPROVED_FOR_VOTING);
        	} else {
        		updateOperations.set(stateProperty, QuoteState.NEW);
        	}
        }
        if (isQuoteFormToGetPublished(request())) {
        	updateOperations.set(stateProperty, QuoteState.CHECKED_AND_PUBLISHED);
        	if (oldQuote.quoteState != QuoteState.CHECKED_AND_PUBLISHED) {
        		updateOperations.set("publishedDate", new Date());
        	}
        } else {
        	if (isQuoteFormToGetAnalyze(request())) {
        		updateOperations.set(stateProperty, QuoteState.ANALYSIS_IN_PROGRESS);
        	} else if (isQuoteFormToGetApproved(request())) {
        		updateOperations.set(stateProperty, QuoteState.APPROVED_FOR_VOTING);
        	} else {
        		updateOperations.set(stateProperty, QuoteState.NEW);
        	}
        }

        DBHolder.ds.update(new Key<Quote>(Quote.class, quote.id), updateOperations);

        final Map<String, String[]> requestContent = request().body().asFormUrlEncoded();
        if (requestContent.containsKey("fullEditable") && BooleanUtils.toBoolean(requestContent.get("fullEditable")[0])) {
        	return redirect(routes.Admin.showAllQuotes());
        }
        return redirectByQuoteState(oldQuote.quoteState);
    }
	
	private static boolean isQuoteFormToGetApproved(Request request) {
		 return request.body().asFormUrlEncoded().containsKey("approved");
	}
	
	private static boolean isQuoteFormToGetAnalyze(Request request) {
		 return request.body().asFormUrlEncoded().containsKey("analyze");
	}
	
	private static boolean isQuoteFormToGetPublished(Request request) {
		 return request.body().asFormUrlEncoded().containsKey("published");
	}

	private static Result redirectByQuoteState(QuoteState quoteState) {
        switch (quoteState) {
			case NEW:
		        return redirect(routes.Admin.showNewlyAddedQuotes());
			case APPROVED_FOR_VOTING:
		        return redirect(routes.Admin.showApprovedQuotes());
			case ANALYSIS_IN_PROGRESS:
		        return redirect(routes.Admin.showQuotesInAnalysis());
			case CHECKED_AND_PUBLISHED:
		        return redirect(routes.Admin.showPublishedQuotes());
		    default:
		    	throw new IllegalArgumentException();
        }
	}

	@Authenticated(UserAuthenticator.class)
	public static Result showQuotes(QuoteState state, boolean fullyEditable) {
		final List<Quote> quotes;
		if (state == null) {
			quotes = Quote.findAllSortedByStateAndCreationDate(false);
		} else {
			quotes = Quote.findAllWithStateOrderedByCreationDate(state);
		}

        // FIXME Michal Bernhard 26.03 : when instead of fourth parameter 'QuotesListContent.CHECKED' is null it
        // doesn't work, dunnno why
		return ok(quotes_list.render(quotes, true, fullyEditable, QuotesListContent.CHECKED, null, null, null));
	}

	@Authenticated(UserAuthenticator.class)
	public static Result showAllQuotes() {
		return showQuotes(null, true);
	}
	
	@Authenticated(UserAuthenticator.class)
	public static Result showNewlyAddedQuotes() {
		return showQuotes(QuoteState.NEW, false);
	}

	@Authenticated(UserAuthenticator.class)
	public static Result showApprovedQuotes() {
		return showQuotes(QuoteState.APPROVED_FOR_VOTING, false);
	}

	@Authenticated(UserAuthenticator.class)
	public static Result showQuotesInAnalysis() {
		return showQuotes(QuoteState.ANALYSIS_IN_PROGRESS, false);
	}

	@Authenticated(UserAuthenticator.class)
	public static Result showPublishedQuotes() {
		return showQuotes(QuoteState.CHECKED_AND_PUBLISHED, false);
	}

    @Authenticated(UserAuthenticator.class)
    public static Result showSettings() {
        Configuration configuration = Play.application().configuration();

        StringBuilder sb = new StringBuilder();

        for(String key : configuration.keys()) {
            String keyValue = "n/a";
            try {
                keyValue = configuration.getString(key);
            } catch (Exception /*com.typesafe.config.ConfigException*/ ex) {
                keyValue = "Cannot obtain string value of configuration key because of: " + ex.getMessage();
            }

            sb.append(key).append(" : ").append(keyValue).append("\n\n");
        }

        return ok(sb.toString());
    }

}
