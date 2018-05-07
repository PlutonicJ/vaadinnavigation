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
import net.cyberspirit.navigation.NavigationBuilder;
import net.cyberspirit.navigation.NavigationElement;

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

        NavigationElement navigationElement = new NavigationBuilder()
                .expandable("testMenü")
                    .internalLink("test", "test")
                    .expandable("test1234")
                        .internalLink("test5678", "test")
                    .endExpandable()
                    .internalLink("hallo", "hallo")
                .endExpandable()
            .build();

        HorizontalLayout inhalt = new HorizontalLayout(navigationElement, mainLayout);
        inhalt.setMargin(new MarginInfo(false, false, true, false));
        setContent(inhalt);

        navigator.navigateTo("hallo");
    }
}
