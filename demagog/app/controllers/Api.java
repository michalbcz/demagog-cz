package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.code.morphia.Key;

import models.Quote;
import net.tanesha.recaptcha.ReCaptchaResponse;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.ReCaptchaService;
import utils.RequestUtils;

public class Api extends Controller {

    /**
     * For CORS (http://en.wikipedia.org/wiki/Cross-origin_resource_sharing) purposes.
     * Handling OPTIONS http's method.
     *
     * @param restOfUrl
     * @return
     */
    public static Result header(String restOfUrl) {

        Http.Response response = response();

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "accept, origin, content-type");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");

        return ok();

    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveQuote() {

        JsonNode jsonNode = request().body().asJson();

        Form<Quote> quoteForm = Form.form(Quote.class).bindFromRequest("quoteText", "url", "addedFromBookmarklet", "recaptchaChallenge", "recaptchaResponse");

        String recaptchaResponse = jsonNode.get("recaptchaResponse").asText();
        String recaptchaChallenge = jsonNode.get("recaptchaChallenge").asText();

        ReCaptchaService recaptchaService = ReCaptchaService.get();

        String remoteAddress = request().remoteAddress(); //TODO isnt remote address address of heroku routers mesh on production ?
        ReCaptchaResponse reCaptchaResponse =
                recaptchaService.checkAnswer(remoteAddress, recaptchaChallenge, recaptchaResponse); // TODO : what happens when recaptcha is down ?

        if (!reCaptchaResponse.isValid()) {
            quoteForm.reject("recaptcha.error", "recaptcha.failed");
        }

        if (quoteForm.hasErrors()) {
            ObjectNode json = Json.newObject();

            ObjectNode data = Json.newObject();
            json.put("data", data);

            JsonNode errors = quoteForm.errorsAsJson();
            data.put("errors", errors);

            /* meta data */
            ObjectNode metaData = Json.newObject();
            json.put("metadata", metaData);

            ObjectNode status = Json.newObject();
            metaData.put("status", status);

            status.put("text", "error");
            status.put("code", "500");
            status.put("detail", "Captcha is not valid.");

            return ok(json);

        } else {

            Quote quote = quoteForm.get();
            quote.userIp = RequestUtils.getRemoteAddress(request());



            Key<Quote> savedQuoteKey = quote.save(); //TODO: what happend when save failed ? (btw activate safe save!)

            ObjectNode json = Json.newObject();

            /* response data */
            ObjectNode data = Json.newObject();

            String quoteKey = savedQuoteKey.getId().toString();
            String quotePermalinkUrl = controllers.routes.Application.showQuoteDetail(quoteKey).absoluteURL(request());
            data.put("quotePermalinkUrl", quotePermalinkUrl);
            data.put("quoteId", quoteKey);

            json.put("data", data);

            /* meta data */
            ObjectNode metaData = Json.newObject();
            json.put("metadata", metaData);

            ObjectNode status = Json.newObject();
            metaData.put("status", status);

            status.put("text", "ok");
            status.put("code", "404");
            status.put("detail", "Quote successfully saved with id: " + quoteKey);

            return ok(json);

        }

    }


}
