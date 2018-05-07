package net.cyberspirit;

import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.inject.Inject;
import java.util.Locale;

@CDIUI("")
public class VaadinGridUI extends UI {

    @Inject
    private CDIViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setLocale(Locale.GERMAN);
        Responsive.makeResponsive(this);

        showMainLayout();
    }

    private void showMainLayout() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setMargin(new MarginInfo(false, false, false, true));

        Navigator navigator = new Navigator(this, mainLayout);
        navigator.addProvider(viewProvider);

        HorizontalLayout inhalt = new HorizontalLayout(new Button("test", e -> navigator.navigateTo("test")), new Button("hallo", e -> navigator.navigateTo("hallo")), mainLayout);
        inhalt.setMargin(new MarginInfo(false, false, true, false));
        setContent(inhalt);

        navigator.navigateTo("hallo");
    }
}
