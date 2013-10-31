package utils;

import java.util.List;

import org.springframework.util.Assert;

import com.google.common.collect.Lists;

public class MenuUtils {
	
	public static final String USER_NEW_QUOTE_ID = "user-new-quote";
	public static final String USER_APPROVED_ID = "user-approved";
	public static final String USER_CHECKED_ID = "user-checked";
	
	public static final String ADMIN_QUOTES_NEW_ID = "admin-quotes-new";
	public static final String ADMIN_QUOTES_APPROVED_ID = "admin-quotes-approved";
	public static final String ADMIN_QUOTES_ANALYSIS_ID = "admin-quotes-analysis";
	public static final String ADMIN_QUOTES_PUBLISHED_ID = "admin-quotes-published";
	public static final String ADMIN_QUOTES_ALL_ID = "admin-quotes-all";
	public static final String ADMIN_LOGOUT_ID = "admin-logout";
	
	public interface IMenuItem {
		public String getId();
		public String getUrl();
		public String getLabel();
		public void setActive(boolean active);
		public boolean isActive();
	}
	
	public static class MenuItem implements IMenuItem {
		
		private final String id;
		private final String url;
		private final String label;
		private boolean active;

		public MenuItem(String id, String url, String label) {
			Assert.hasText(id);
			Assert.notNull(url);
			Assert.notNull(label);
			this.id = id;
			this.url = url;
			this.label = label;
		}
		
		@Override
		public String getId() {
			return id;
		}
		
		@Override
		public String getUrl() {
			return url;
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public void setActive(boolean active) {
			this.active = active;
		}

	}
	
	public static class MenuBuilder {
		
		private final List<IMenuItem> menuItems = Lists.newArrayList();
		
		public MenuBuilder createUser() {
			menuItems.add(new MenuItem(USER_NEW_QUOTE_ID, controllers.routes.Application.showNewQuoteForm().url(), "Chci ověřit výrok"));
			menuItems.add(new MenuItem(USER_APPROVED_ID, controllers.routes.Application.showQuotes(models.QuotesListContent.APPROVED).url(), "Hlasování"));
			menuItems.add(new MenuItem(USER_CHECKED_ID, controllers.routes.Application.showQuotes(models.QuotesListContent.CHECKED).url(), "Ověřené citáty"));
			return this;
		}
		
		public MenuBuilder createAdmin() {
			menuItems.add(new MenuItem(ADMIN_QUOTES_NEW_ID, controllers.routes.Admin.showNewlyAddedQuotes().url(), "Nově přidané"));
			menuItems.add(new MenuItem(ADMIN_QUOTES_APPROVED_ID, controllers.routes.Admin.showApprovedQuotes().url(), "V hlasování"));
			menuItems.add(new MenuItem(ADMIN_QUOTES_ANALYSIS_ID, controllers.routes.Admin.showQuotesInAnalysis().url(), "Probíhá analýza"));
			menuItems.add(new MenuItem(ADMIN_QUOTES_PUBLISHED_ID, controllers.routes.Admin.showPublishedQuotes().url(), "Publikované"));
			menuItems.add(new MenuItem(ADMIN_QUOTES_ALL_ID, controllers.routes.Admin.showAllQuotes().url(), "Vše"));
			menuItems.add(new MenuItem(ADMIN_LOGOUT_ID, controllers.routes.Admin.logout().url(), "Odhlásit se"));
			return this;
		}
		
		public MenuBuilder add(IMenuItem menuItem) {
			menuItems.add(menuItem);
			return this;
		}
		
		public MenuBuilder activate(IMenuItem menuItem) {
			Assert.notNull(menuItem);
			activate(menuItem.getId());
			return this;
		}
		
		public MenuBuilder activate(String menuItemId) {
			for (IMenuItem menuItemPom : menuItems) {
				if (menuItemPom.getId().equalsIgnoreCase(menuItemId)) {
					menuItemPom.setActive(true);
				} else {
					menuItemPom.setActive(false);
				}
			}
			return this;
		}
		
		public List<IMenuItem> toList() {
			return Lists.newArrayList(menuItems);
		}
	}
	
}
