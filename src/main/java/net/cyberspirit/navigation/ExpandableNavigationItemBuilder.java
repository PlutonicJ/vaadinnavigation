package net.cyberspirit.navigation;

public interface ExpandableNavigationItemBuilder extends NavigationElementBuilder {
	ExpandableNavigationItemBuilder endExpandable();

	NavigationElement build();
}