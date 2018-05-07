package net.cyberspirit;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import javax.inject.Inject;
import javax.inject.Named;

@Named("test")
@CDIView("test")
public class TestView extends CustomComponent implements View {

    @Inject
    private TestPagedGrid grid;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        VerticalLayout layout = new VerticalLayout();
        setCompositionRoot(layout);

        layout.addComponent(grid);
    }
}
