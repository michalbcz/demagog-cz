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
  
  public static Result submit() {
  	Form<Quote> submitForm = form(Quote.class);
  	Quote quote = submitForm.bindFromRequest().get();

  	quote.save();
  	
  	return ok(submitResponse.render(""));
  }
  
	public static Result showQuotes() {
		List<Quote> quotes = Quote.findAll();
		
		return ok(quotes_list.render(quotes));
	}  

}