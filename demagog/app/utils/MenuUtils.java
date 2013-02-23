package utils;

public class MenuUtils {

	public static String getMenuType(boolean admin) {
		if (admin)
			return "menu-admin";
		else
			return "menu-main";
	}
}
