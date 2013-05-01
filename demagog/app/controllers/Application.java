package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import models.Quote;
import models.Quote.QuoteState;
import models.QuotesListContent;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.bson.types.ObjectId;

import play.Logger;
import play.api.http.Status$;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import utils.ReCaptchaService;
import utils.RequestUtils;
import views.html.quote_detail;
import views.html.quote_new;
import views.html.quotes_list;

import com.google.code.morphia.Key;

public class Application extends Controller {

	private static final String COOKIE_NAME = "demagog.cz-votes";
	private static final String COOKIE_VALUE_SEPARATOR = "_";

	public static Result untrail(String path) {
		//trailing slash workaround
		// viz http://stackoverflow.com/questions/13189095/play-framework2-remove-trailing-slash-from-urls
		return movedPermanently("/" + path);
	}
	
	public static Result showNewQuoteForm() {
		return ok(quote_new.render(new Quote()));
	}

    public static Result showNewQuoteForm(Quote quote) {
        return ok(quote_new.render(quote));
    }

   	public static Result submitQuote() {

        /* binding concrete request fields to prevent attack - without this attacker could change for example
           quote state and directly publish it after save */
		Form<Quote> quoteForm = form(Quote.class).bindFromRequest("quoteText", "url", "author");
        Quote quote = quoteForm.get();

        if (quoteForm.hasErrors()) {
            flash().put("error", "Ve formuláři jsou chyby opravte je.");
            return ok(quote_new.render(quote));
        }

        String remoteAddress = request().remoteAddress();
        quote.userIp = remoteAddress;
		quote.quoteState = QuoteState.NEW;

        Map<String, String[]> formBody = request().body().asFormUrlEncoded();
        String reCaptchaChallengeField = formBody.get("recaptcha_challenge_field")[0];
        String reCaptchaResponseField = formBody.get("recaptcha_response_field")[0];

        ReCaptchaService recaptchaService = ReCaptchaService.get();
        ReCaptchaResponse reCaptchaResponse =
                        recaptchaService.checkAnswer(remoteAddress, reCaptchaChallengeField, reCaptchaResponseField);


        if (!reCaptchaResponse.isValid()) {
            quoteForm.reject("recaptcha.error", "recaptcha.failed");
            flash().put("error", "Zadali jste špatně captchu, zkuste to znovu.");
        }

        if (quoteForm.hasErrors()) {
            return ok(quote_new.render(quote));
        } else {
            Key savedQuoteKey = quote.save();
            return redirect(routes.Application.showQuoteDetail(savedQuoteKey.getId().toString()));
        }

	}


	public static Result showApprovedQuotes() {
		return showQuotes(QuotesListContent.APPROVED);
	}

	public static Result showCheckedQuotes() {
		return showQuotes(QuotesListContent.CHECKED);
	}

	public static Result showQuotes(QuotesListContent content) {
		Map<String, String[]> requestParams = request().body().asFormUrlEncoded();
		String author;
		if (requestParams == null || requestParams.get("author") == null || requestParams.get("author").length == 0) {
			author = Quote.AUTHOR_EMPTY_FILTER;
		} else {
			author = requestParams.get("author")[0];
		}

		QuoteState state;
		if (QuotesListContent.APPROVED.equals(content)) {
			state = QuoteState.APPROVED_FOR_VOTING;
		} else {
			state = QuoteState.CHECKED_AND_PUBLISHED;
		}

		final List<Quote> quotes;
		if (author == null || Quote.AUTHOR_EMPTY_FILTER.equals(author)) {
			quotes = Quote.findAllSortedByVote(state);
		} else {
			quotes = Quote.findAllSortedByVoteFilteredByAuthor(author, state);
		}

		return ok(quotes_list.render(quotes, false, content, Quote.getAllAuthorNames(state), author, getAlreadyVotedQuotesByUser()));
	}

	public static Result showQuoteDetail(String id) {
		Quote quote;
		try {
			quote = Quote.findById(new ObjectId(id));
		} catch (IllegalArgumentException e) {
			Logger.warn("Nekdo zadal shitovy quote id.", e);
			quote = null;
		}

		if (quote == null) {
            //TODO mbernhard 01.05.2013 : it should be best when just notFound is enough and handling (ie. showing appropriate error page) would be on one place (like BaseGlobal)
			return notFound(views.html.system.error.render(request(), NOT_FOUND));
		}

		return ok(quote_detail.render(quote, getAlreadyVotedQuotesByUser()));
	}

	public static Result upVote(QuotesListContent content) {
        String id = request().body().asFormUrlEncoded().get("id")[0];
        upVoteQuote(id);

		return showQuotes(content);
	}

    public static Result upVoteAjax(QuotesListContent content) {
        String id = request().body().asFormUrlEncoded().get("id")[0];
        upVoteQuote(id);

        return ok();
    }

    private static void upVoteQuote(String id) {

        Quote.upVote(new ObjectId(id), RequestUtils.getRemoteAddress(request()));

        Cookie cookie = request().cookies().get(COOKIE_NAME);
        String votes = "";
        if (cookie != null && cookie.value() != null) {
            votes = cookie.value();
            votes += COOKIE_VALUE_SEPARATOR;
        }

        votes += id;

        response().setCookie(COOKIE_NAME, votes);
    }


	/**
	 * @return ids of quotes user already voted on (from cookies).
	 */
	private static List<String> getAlreadyVotedQuotesByUser() {
		List<String> alreadyVoted = new ArrayList<String>();

		Cookie cookie = null;

		// first look after the cookies from the response
		for (Cookie responseCookie : response().cookies()) {
			if (responseCookie.name().equals(COOKIE_NAME)) {
				cookie = responseCookie;
				break;
			}
		}
		if (cookie == null) {
			cookie = request().cookies().get(COOKIE_NAME);
		}
		if (cookie != null && cookie.value() != null) {
			String votes = cookie.value();

			alreadyVoted = Arrays.asList(votes.split(COOKIE_VALUE_SEPARATOR));
		}

		return alreadyVoted;
	}
}
