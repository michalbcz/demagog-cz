package controllers;

import java.util.Date;
import java.util.Map;

import models.Quote;
import models.Quote.QuoteState;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class Api extends Controller {

    public static Result header(String restOfUrl) {

        Http.Response response = response();

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "accept, origin, content-type");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");

        return ok();

    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveQuotePost() {

        JsonNode jsonNode = request().body().asJson();

        Quote quote = new Quote();
        quote.userIp = request().remoteAddress();

        ObjectNode json = Json.newObject();

        ObjectNode metaData = Json.newObject();
        metaData.put("status", "ok");
        metaData.put("status_message", "quote successfully saved");
        json.put("metadata", metaData);

        ObjectNode data = Json.newObject();
        json.put("data", data);

        return ok(json);

    }
	
	public static Result saveQuote() {
		
		Map<String, String[]> queryString = request().queryString();
		String jsonpCallbackFunctionName = queryString.get("callback")[0];
		String quoteText = queryString.get("quoteText")[0];
		String sourceUrl = queryString.get("url")[0];
		
//		JsonNode json = request().body().asJson();
//		String quoteText = json.get("quoteText").asText();
//		String sourceUrl = json.get("url").asText();
		
		Quote quote = new Quote();
		quote.creationDate = new Date();
		quote.quoteState = QuoteState.NEW;
		quote.url = sourceUrl;
		quote.quoteText = quoteText;
		quote.userIp = request().remoteAddress();
		
		quote.save();
		
		String quotePermalinkUrl = controllers.routes.Application.showQuoteDetail(quote.id.toString()).absoluteURL(request());
		
		ObjectNode jsonResponse = Json.newObject();
		jsonResponse.put("quotePermalinkUrl", quotePermalinkUrl);
		
		return ok(jsonpCallbackFunctionName + "(" + jsonResponse.toString() + ");");
		
	}

}
