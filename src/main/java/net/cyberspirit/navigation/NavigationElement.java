package net.cyberspirit.navigation;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NavigationElement extends CssLayout {

    List<String> viewIds = new ArrayList<>();
    LinkedList<NavigationElement> children = new LinkedList<>();
    NavigationElement parent;
    String externalUrl;

	public NavigationElement() {
		super();
	}

	NavigationElement(String label, int level) {
        this(label, null, true, level);
    }

    NavigationElement(String label, List<String> viewIds, int level) {
        super();
        setVisible(false);
        if (label != null && !label.isEmpty()) {
			StringBuilder labelText = new StringBuilder();
			for (int i = 0; i < level; i++) {
				labelText.append("&nbsp;&nbsp;");
			}
			labelText.append(label);
			Label component = new Label(labelText.toString());
			component.setContentMode(ContentMode.HTML);
			this.addComponent(component);
		}
		if (viewIds != null && !viewIds.isEmpty()) {
			this.viewIds = viewIds;
		}
	}

    NavigationElement(String label, String externalUrl, boolean enabled, int level) {
        super();
        setVisible(false);
        setEnabled(enabled);
		if (label != null && !label.isEmpty()) {
			StringBuilder labelText = new StringBuilder();
			for (int i = 0; i < level; i++) {
				labelText.append("&nbsp;&nbsp;");
			}
			labelText.append(label);
			Label component = new Label(labelText.toString());
			component.setContentMode(ContentMode.HTML);
			this.addComponent(component);
		}
        this.externalUrl = externalUrl;
    }

    NavigationElement(String label, String externalUrl, int level) {
        this(label, externalUrl, true, level);
    }


}
