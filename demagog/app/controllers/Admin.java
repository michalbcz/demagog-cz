package controllers;

import java.util.List;

import models.Quote;
import models.Quote.QuoteState;
import models.User;
import java.util.UUID;

import org.bson.types.ObjectId;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.loginForm;
import views.html.quotes_list;

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
	  		return redirect(controllers.routes.Admin.showApproveQuotes());
	  	}
	  	
	  	return redirect(controllers.routes.Admin.index());
		
	}
	
	public static Result showQuotes(QuoteState state) {
		String sid = session("sid");
		
		User user = User.findSID(sid);
		
		if (user != null) {
			List<Quote> quotes = Quote.findAllWithApprovedState(state);
			
			return ok(quotes_list.render(quotes, true, null));
		}
		
		return redirect(controllers.routes.Admin.index());
		
	}
	
	public static Result approve() {
		String sid = session("sid");
		
		User user = User.findSID(sid);
		
		if (user != null) {
			Form<Quote> quoteForm = form(Quote.class);
			Quote quote = quoteForm.bindFromRequest().get();
			
			Quote.approve(quote.id, quote.quoteText, quote.author);			
			
		}
		
		return redirect(controllers.routes.Admin.showApproveQuotes());
	}
	
	public static Result reject() {
		String sid = session("sid");
		
		User user = User.findSID(sid);
		
		if (user != null) {
			String id = request().body().asFormUrlEncoded().get("id")[0];
			
			Quote.delete(new ObjectId(id));
			
		}
		
		return redirect(controllers.routes.Admin.index());
	}

	public static Result setChecked() {
		String sid = session("sid");
		
		User user = User.findSID(sid);
		
		if (user != null) {
			String id = request().body().asFormUrlEncoded().get("id")[0];
			
			Quote.setChecked(new ObjectId(id));			
			return redirect(controllers.routes.Admin.showCheckQuotes());
		}

		return redirect(controllers.routes.Admin.index());
	}

	public static Result showApproveQuotes() {
		return Admin.showQuotes(QuoteState.NEW);
	}

	public static Result showCheckQuotes() {

		return Admin.showQuotes(QuoteState.APPROVED);
	}

	public static Result logout() {
		session().clear();
		return redirect(controllers.routes.Admin.index());
	}
	
	private static String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
