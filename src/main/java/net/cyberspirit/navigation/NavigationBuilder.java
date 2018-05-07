package net.cyberspirit.navigation;

import com.vaadin.ui.UI;

import java.util.*;

public class NavigationBuilder implements NavigationElementBuilder, ExpandableNavigationItemBuilder {

    private NavigationElement instance;
    private NavigationElement current;
    private int level = 0;

    private NavigationElementBuilder addNavigationElement(NavigationElement navigationElement, boolean newLevel) {
        if (instance == null) {
            instance = navigationElement;
            current = instance;
		} else {
			navigationElement.parent = current;
			current.children.add(navigationElement);
			if (newLevel) {
				current = navigationElement;
			}
		}
        return this;
    }

    @Override
    public ExpandableNavigationItemBuilder internalLink(String label, String viewId, String... alternativeViewIds) {
        List<String> viewIds;
        if (alternativeViewIds.length > 0) {
            viewIds = new ArrayList<>(alternativeViewIds.length + 1);
            viewIds.addAll(Arrays.asList(alternativeViewIds));
            viewIds.add(viewId);
        } else {
            viewIds = Collections.singletonList(viewId);
        }
        addNavigationElement(new NavigationElement(label, viewIds, level), false);
        return this;
    }

    @Override
    public ExpandableNavigationItemBuilder externalLink(String label, String url) {
        externalLink(label, url, true);
        return this;
    }

    @Override
    public ExpandableNavigationItemBuilder externalLink(String label, String url, boolean enabled) {
        addNavigationElement(new NavigationElement(label, url, enabled, level), false);
        return this;
    }

    @Override
    public ExpandableNavigationItemBuilder expandable(String label) {
        addNavigationElement(new NavigationElement(label, level), true);
        level++;
        return this;
    }

    @Override
    public ExpandableNavigationItemBuilder expandable(String label, String viewId) {
        addNavigationElement(new NavigationElement(label, viewId, level), true);
        level++;
        return this;
    }

    @Override
    public ExpandableNavigationItemBuilder element(NavigationElement navigationElement) {
        addNavigationElement(navigationElement, false);
        return this;
    }

    @Override
    public NavigationElement build() {
		NavigationElement navigationElement = this.instance;
		navigationElement.setVisible(true);

		addLayoutClickListenerRecursively(navigationElement);
		current = navigationElement;

		NavigationElement result = new NavigationElement();
		addComponentRecursively(result, navigationElement);

        return result;
    }

	private void addComponentRecursively(NavigationElement container, NavigationElement navigationElement) {
		container.addComponent(navigationElement);
    	for (NavigationElement child : navigationElement.children) {
			addComponentRecursively(container, child);
		}
	}

	private void addLayoutClickListenerRecursively(NavigationElement navigationElement) {
		if (navigationElement.externalUrl != null) {
			navigationElement.addLayoutClickListener(e -> UI.getCurrent().getPage().setLocation(navigationElement.externalUrl));
		} else if (!navigationElement.children.isEmpty() && !navigationElement.viewIds.isEmpty()) {
			navigationElement.addLayoutClickListener(e ->  {
				toggleNextLevelVisibility(navigationElement);
				UI.getCurrent().getNavigator().navigateTo(navigationElement.viewIds.get(0));
			});
		} else if (!navigationElement.children.isEmpty()) {
			navigationElement.addLayoutClickListener(e -> toggleNextLevelVisibility(navigationElement));
		} else if (!navigationElement.viewIds.isEmpty()) {
			navigationElement.addLayoutClickListener(e -> UI.getCurrent().getNavigator().navigateTo(navigationElement.viewIds.get(0)));
		}

		for (NavigationElement child : navigationElement.children) {
			addLayoutClickListenerRecursively(child);
		}
	}

	private void toggleNextLevelVisibility(NavigationElement navigationElement) {
		if (!navigationElement.children.isEmpty()) {
			boolean visible = navigationElement.children.getFirst().isVisible();
			for (NavigationElement child : navigationElement.children) {
				child.setVisible(!visible);
				if (visible) {
					setChildsVisibilityRecursively(child, false);
				}
			}
		}
	}

	private void setChildsVisibilityRecursively(NavigationElement navigationElement, boolean visible) {
		for (NavigationElement child : navigationElement.children) {
			child.setVisible(visible);
			setChildsVisibilityRecursively(child, visible);
		}
	}

    @Override
    public ExpandableNavigationItemBuilder endExpandable() {
        if (current.parent != null) {
            current = current.parent;
            level--;
        }
        return this;
    }
}