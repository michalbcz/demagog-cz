package utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import play.mvc.Http.Request;

public class RequestUtils {

	private RequestUtils() {}
	
	/**
	 * Gets the remote ip address from the given HTTP request.
	 * 
	 * @param request
	 *            the HTTP request, can't be <code>null</code>
	 * @return the remote ip address, never returns <code>null</code>
	 */
	public static String getRemoteAddress(Request request) {
		Assert.notNull(request);
		
		String header = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(header)) {
			header = request.remoteAddress();
		}
		final String[] split = StringUtils.split(header, ',');
		if (split != null && split.length > 0) {
			return StringUtils.trimToEmpty(split[0]);
		} else {
			return "";
		}
	}
}
