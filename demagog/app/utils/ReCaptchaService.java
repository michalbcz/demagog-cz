package utils;

import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;

import java.util.Properties;

/**
 *
 *
 * @author Michal Bernhard (michal@bernhard.cz) 2013
 */
public class ReCaptchaService {

    private net.tanesha.recaptcha.ReCaptcha reCaptchaService;

    private final static String CREATE_API_KEYS_ERROR_MESSAGE =
                                    "For that key you need to create account on http://www.google.com/recaptcha";

    public static final synchronized ReCaptchaService get() {
        return new ReCaptchaService();
    }

    private ReCaptchaService() {
        String publicKey = systemPropertyOrError("demagog.recaptcha.publicKey", CREATE_API_KEYS_ERROR_MESSAGE);
        String privateKey = systemPropertyOrError("demagog.recaptcha.privateKey", CREATE_API_KEYS_ERROR_MESSAGE);
        reCaptchaService = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, /* include non script */ false);
    }

    private String systemPropertyOrError(String systemPropertyName, String errorMessage) {
        String systemPropertyValue = System.getProperty(systemPropertyName);

        if (systemPropertyValue == null) {
            throw new RuntimeException("System property '" + systemPropertyName + "' must be set. " + errorMessage);
        }

        return systemPropertyValue;
    }

    public String createRecaptchaHtml(String errorMessage, Properties options) {
        return reCaptchaService.createRecaptchaHtml(errorMessage, options);
    }

    public ReCaptchaResponse checkAnswer(String remoteAddr, String challenge, String response) {
        return reCaptchaService.checkAnswer(remoteAddr, challenge, response);
    }

    public String createRecaptchaHtml(String errorMessage, String theme, Integer tabindex) {
        return reCaptchaService.createRecaptchaHtml(errorMessage, theme, tabindex);
    }
}
