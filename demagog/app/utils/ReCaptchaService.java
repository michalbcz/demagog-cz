package utils;

import java.util.Properties;

import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.util.Assert;

import play.Play;

/**
 *
 *
 * @author Michal Bernhard (michal@bernhard.cz) 2013
 */
public class ReCaptchaService {

    private net.tanesha.recaptcha.ReCaptcha reCaptchaService;
    
    /**
     * The public development key. For development purposes only!
     */
    private final static String PUBLIC_DEVELOPMENT_KEY = "6LcePuESAAAAACXN6NSveoRs29nYU4_0TOZLUuLM";
    
    /**
     * The private development key. For development purposes only!
     */
    private final static String PRIVATE_DEVELOPMENT_KEY = "6LcePuESAAAAAKC9wsaPA_O5IB4mihZYOsJCL8Ca";
    
    /**
     * System property to get public recaptcha key
     */
    private final static String PUBLIC_KEY_PROPERTY = "demagog.recaptcha.publicKey";
    
    /**
     * System property to get private recaptcha key
     */
    private final static String PRIVATE_KEY_PROPERTY = "demagog.recaptcha.privateKey";

    private final static String CREATE_API_KEYS_ERROR_MESSAGE =
                                    "For that key you need to create account on http://www.google.com/recaptcha";

    public static final synchronized ReCaptchaService get() {
        return new ReCaptchaService();
    }

    private ReCaptchaService() {
        String publicKey = getKeyFromSystemProperty(PUBLIC_KEY_PROPERTY, PUBLIC_DEVELOPMENT_KEY);
        String privateKey = getKeyFromSystemProperty(PRIVATE_KEY_PROPERTY, PRIVATE_DEVELOPMENT_KEY);
        reCaptchaService = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, /* include non script */ false);
    }
    
    private String getKeyFromSystemProperty(String systemPropertyName, String defaultValue) {
    	if (Play.isProd()) {
    		return systemPropertyOrError(systemPropertyName, CREATE_API_KEYS_ERROR_MESSAGE);
    	} else {
    		return systemPropertyOrDefault(systemPropertyName, defaultValue);
    	}
    }
    
    private String systemPropertyOrDefault(String systemPropertyName, String defaultValue) {
    	Assert.notNull(systemPropertyName);
        
    	String systemPropertyValue = System.getProperty(systemPropertyName);

        if (systemPropertyValue == null) {
        	systemPropertyValue = defaultValue;
        }
        return systemPropertyValue;
    }

    private String systemPropertyOrError(String systemPropertyName, String errorMessage) {
    	Assert.notNull(systemPropertyName);
        
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
