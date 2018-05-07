package net.cyberspirit.navigation;

public interface NavigationElementBuilder {
    interface NavigationItemBuilder {
        ExpandableNavigationItemBuilder internalLink(String label, String viewId, String... alternativeViewIds);
        ExpandableNavigationItemBuilder externalLink(String label, String url);
        ExpandableNavigationItemBuilder externalLink(String label, String url, boolean enabled);
        ExpandableNavigationItemBuilder expandable(String label);
        ExpandableNavigationItemBuilder expandable(String label, String viewId);
        ExpandableNavigationItemBuilder element(NavigationElement navigationElement);
        NavigationElement build();
    }
    interface ExpandableNavigationItemBuilder extends NavigationItemBuilder {
        ExpandableNavigationItemBuilder endExpandable();
        NavigationElement build();
    }
}
