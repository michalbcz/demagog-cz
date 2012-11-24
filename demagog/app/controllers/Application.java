package controllers;

import java.util.List;

import models.Quote;
import play.*;
import play.mvc.*;

import views.html.*;
import play.data.*;

public class Application extends Controller {	

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}
	
	public static Result showNewQuoteForm() {
		return ok(quote_new.render());
	}

	public static Result submitQuote() {
		Form<Quote> quoteForm = form(Quote.class);
		Quote quote = quoteForm.bindFromRequest().get();

		quote.save();

		return ok();
	}

	public static Result showQuotes() {
		List<Quote> quotes = Quote.findAll();

		return ok(quotes_list.render(quotes));
	}  

}