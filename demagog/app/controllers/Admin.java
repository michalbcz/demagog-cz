package controllers;

import java.util.List;

import models.Quote;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.data.*;
import models.User;

import org.bson.types.ObjectId;

public class Admin extends Controller {
	
	public static Result index() {
		return ok(loginForm.render());
	}
	
	public static Result login() {
		Form<User> loginForm = form(User.class);
	  	User formUser = loginForm.bindFromRequest().get();
		
	  	User user = User.findUser(formUser.username);
	  	
	  	if (user != null && user.password.equals(formUser.password)) {
	  		String sid = Admin.generateUUID();
	  		user.updateSID(sid);
	  		session("sid", sid);
	  		return redirect(controllers.routes.Admin.showQuotes());
	  	}
	  	
	  	return redirect(controllers.routes.Admin.index());
		
	}
	
	public static Result showQuotes() {
		String sid = session("sid");
		
		User user = User.findSID(sid);
		
		if (user != null) {
			List<Quote> quotes = Quote.findAllWithApprovedState(false);
			
			return ok(quotes_list.render(quotes, true));
		}
		
		return redirect(controllers.routes.Admin.index());
		
	}
	
	public static Result approve() {
		String sid = session("sid");
		
		User user = User.findSID(sid);
		
		if (user != null) {
			String id = request().body().asFormUrlEncoded().get("id")[0];
			
			Quote.approve(new ObjectId(id));
			
			System.out.println("id: " + id);
			
			
		}
		
		return redirect(controllers.routes.Admin.showQuotes());
	}
	
	public static Result reject() {
		String sid = session("sid");
		
		User user = User.findSID(sid);
		
		if (user != null) {
			String id = request().body().asFormUrlEncoded().get("id")[0];
			
			Quote.delete(new ObjectId(id));
			
		}
		
		return redirect(controllers.routes.Admin.showQuotes());
	}
	
	private static String generateUUID() {
		return "...";
	}
}
