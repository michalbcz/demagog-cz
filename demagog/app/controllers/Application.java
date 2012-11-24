package controllers;

import java.util.List;

import models.Quote;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {	

	public static Result showNewQuoteForm() {
		return ok(quote_new.render());
	}

	public static Result submitQuote() {
		Form<Quote> quoteForm = form(Quote.class);
		Quote quote = quoteForm.bindFromRequest().get();

		quote.save();

		return ok("Saved");
	}

	public static Result showQuotes() {
		List<Quote> quotes = Quote.findAll();

		return ok(quotes_list.render(quotes));
	}
	
	public static Result upVote() {
		return redirect(controllers.routes.Application.showQuotes());
	}
}
