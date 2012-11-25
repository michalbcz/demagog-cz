package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Quote;

import org.bson.types.ObjectId;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import views.html.quote_detail;
import views.html.quote_new;
import views.html.quotes_list;

public class Application extends Controller {	

	private static final String COOKIE_NAME = "demagog.cz-votes";
	private static final String COOKIE_VALUE_SEPARATOR = "_";
	
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
		List<Quote> quotes = Quote.findAllSortedByVote(true);
		
		return ok(quotes_list.render(quotes, false, getAllreadyVotedIds()));
	}
	
	public static Result showQuoteDetail(String id) {
		Quote quote = Quote.findById(new ObjectId(id));
		
		if (quote == null) {
			return notFound();			
		}
		
		return ok(quote_detail.render(quote, getAllreadyVotedIds()));
	}
	
	public static Result upVote() {
		String id = request().body().asFormUrlEncoded().get("id")[0];
		
		Quote.upVote(new ObjectId(id));

		Cookie cookie = request().cookies().get(COOKIE_NAME);
		String votes = "";
		if (cookie != null && cookie.value() != null) {
			votes = cookie.value();
			votes += COOKIE_VALUE_SEPARATOR;
		}
		
		votes += id;

		response().setCookie(COOKIE_NAME, votes);
		
		return redirect(controllers.routes.Application.showQuotes());
	}
	
	/**
	 * Find ids of quotes user allready voted on. (from cookie)
	 * 
	 * @return
	 */
	private static List<String> getAllreadyVotedIds() {
		List<String> allreadyVoted = new ArrayList<String>();
		Cookie cookie = request().cookies().get(COOKIE_NAME);
		if (cookie != null && cookie.value() != null) {
			String votes = cookie.value();
			
			allreadyVoted = Arrays.asList(votes.split(COOKIE_VALUE_SEPARATOR));
		}

		return allreadyVoted;
	}	
}
