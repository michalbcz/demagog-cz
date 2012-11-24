package controllers;

import java.util.List;

import org.bson.types.ObjectId;

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

		return ok(quotes_list.render(quotes, false));
	}
	
	public static Result upVote() {
		String id = request().body().asFormUrlEncoded().get("id")[0];
		
		Quote.upVote(new ObjectId(id));
		
		return redirect(controllers.routes.Application.showQuotes());
	}
}
