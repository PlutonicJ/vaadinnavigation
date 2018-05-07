package net.cyberspirit.navigation;

import com.vaadin.ui.CssLayout;

import java.util.*;

public class NavigationElement extends CssLayout {

    private String label;
    private List<String> viewIds;
    private LinkedList<NavigationElement> children = new LinkedList<>();
    private NavigationElement parent;
    private String externalUrl;
    private boolean enabled = true;
    private boolean visible;

    private NavigationElement() {
    }

    public NavigationElement(String label) {
        this.label = label;
    }

    private NavigationElement(String label, List<String> viewIds) {
        this.label = label;
        if (!viewIds.isEmpty()) {
            this.viewIds = viewIds;
        }
    }

    public NavigationElement(String label, String externalUrl, boolean enabled) {
        this.label = label;
        this.externalUrl = externalUrl;
        this.enabled = enabled;
    }

    public NavigationElement(String label, String externalUrl) {
        this(label, externalUrl, true);
    }

    public static NavigationElementBuilder.NavigationItemBuilder builder() {
        return new NavigationItemBuilder();
    }

    public static class NavigationItemBuilder implements NavigationElementBuilder.NavigationItemBuilder, NavigationElementBuilder.ExpandableNavigationItemBuilder {
        private NavigationElement instance;
        private NavigationElement current;

        private NavigationItemBuilder addNavigationElement(NavigationElement navigationElement) {
            if (instance == null) {
                instance = navigationElement;
                current = instance;
            } else {
                navigationElement.parent = current;
                current.children.add(navigationElement);
                current = navigationElement;
            }
            return this;
        }

        @Override
        public NavigationElementBuilder.ExpandableNavigationItemBuilder internalLink(String label, String viewId, String... alternativeViewIds) {
            List<String> viewIds;
            if (alternativeViewIds.length > 0) {
                viewIds = new ArrayList<>(alternativeViewIds.length + 1);
                viewIds.addAll(Arrays.asList(alternativeViewIds));
                viewIds.add(viewId);
            } else {
                viewIds = Collections.singletonList(viewId);
            }
            addNavigationElement(new NavigationElement(label, viewIds));
            return this;
        }

        @Override
        public NavigationElementBuilder.ExpandableNavigationItemBuilder externalLink(String label, String url) {
            externalLink(label, url, true);
            return this;
        }

        @Override
        public NavigationElementBuilder.ExpandableNavigationItemBuilder externalLink(String label, String url, boolean enabled) {
            addNavigationElement(new NavigationElement(label, url, enabled));
            return this;
        }

        @Override
        public NavigationElementBuilder.ExpandableNavigationItemBuilder expandable(String label) {
            addNavigationElement(new NavigationElement(label));
            return this;
        }

        @Override
        public NavigationElementBuilder.ExpandableNavigationItemBuilder expandable(String label, String viewId) {
            addNavigationElement(new NavigationElement(label, viewId));
            return this;
        }

        @Override
        public NavigationElementBuilder.ExpandableNavigationItemBuilder element(NavigationElement navigationElement) {
            addNavigationElement(navigationElement);
            return this;
        }

        @Override
        public NavigationElement build() {
            return instance;
        }

        @Override
        public NavigationElementBuilder.ExpandableNavigationItemBuilder endExpandable() {
            if (current.parent != null) {
                current = current.parent;
            }
            return this;
        }
    }
}
