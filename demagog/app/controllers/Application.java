package controllers;

import java.util.List;

import models.Quote;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
	public static Result index() {
		return ok(index.render("Your new application is ready."));
	} 
  
	public static Result showQuotes() {
		List<Quote> quotes = Quote.findAll();
		
		return ok(quotes_list.render(quotes));
	}
	
	public static Result upVote() {
		return redirect(controllers.routes.Application.showQuotes());
	}
}