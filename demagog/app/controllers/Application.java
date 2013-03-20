package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import models.Quote;
import models.Quote.QuoteState;
import models.QuotesListContent;

import org.bson.types.ObjectId;

import play.Logger;
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

		quote.userIp = request().remoteAddress();
		quote.quoteState = QuoteState.NEW;

		quote.save();

		return ok(quote_detail.render(quote, getAllreadyVotedIds()));
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

		return ok(quotes_list.render(quotes, false, content, Quote.getAllAuthorNames(state), author, getAllreadyVotedIds()));
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
			return notFound();
		}

		return ok(quote_detail.render(quote, getAllreadyVotedIds()));
	}

	public static Result upVote(QuotesListContent content) {
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

		return showQuotes(content);
	}

    public static Result upVoteAjax(QuotesListContent content) {
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

        return ok();
    }

	/**
	 * Find ids of quotes user allready voted on. (from cookie)
	 *
	 * @return
	 */
	private static List<String> getAllreadyVotedIds() {
		List<String> allreadyVoted = new ArrayList<String>();

		Cookie cookie = null;

		// first looak after the cookies from the response
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

			allreadyVoted = Arrays.asList(votes.split(COOKIE_VALUE_SEPARATOR));
		}

		return allreadyVoted;
	}
}
