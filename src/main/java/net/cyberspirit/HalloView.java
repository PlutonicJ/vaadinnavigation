package net.cyberspirit;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import javax.inject.Inject;
import javax.inject.Named;

@Named("hallo")
@CDIView("hallo")
public class HalloView extends CustomComponent implements View {

    @Inject
    private HalloPagedGrid grid;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        VerticalLayout layout = new VerticalLayout();
        setCompositionRoot(layout);

        layout.addComponent(grid);
    }
}
