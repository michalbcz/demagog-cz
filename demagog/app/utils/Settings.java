package utils;

public class Settings {

	/**
	 * development / staging / production
	 */
	private static final String environment;

	private static final String applicationBaseUrl;

	static {
		environment = System.getProperty("DEMAGOG_ENVIRONMENT", "development");

		String url = System.getProperty("DEMAGOG_BASE_URL");
		if (url == null) {
			if ("development".equals(environment)) {
				url = "http://localhost:9000/";
			} else {
				throw new RuntimeException("System property DEMAGOG_BASE_URL has to be set.");
			}
		}
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		applicationBaseUrl = url;
	}

	public static String getApplicationBaseUrl() {
		return applicationBaseUrl;
	}

	public static String getUrl(String relativePortion) {
		return getApplicationBaseUrl() + relativePortion;
	}
}
