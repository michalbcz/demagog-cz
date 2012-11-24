package controllers;

import models.Quote;
import play.mvc.Controller;
import play.mvc.Result;
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


}