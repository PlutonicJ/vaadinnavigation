package net.cyberspirit;

import com.vaadin.cdi.ViewScoped;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.shared.ui.grid.ScrollDestination;
import com.vaadin.ui.*;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

public abstract class PagedGrid<BeanType, SearchType> extends VerticalLayout {
    private Grid<BeanType> innerGrid;
    private VerticalLayout pagerLayout = new VerticalLayout();
    private Binder<SearchType> binder;

    @Inject @ViewScoped
    private Pager pager;

    private boolean dataInitialized = false;
    private DataProviderWrapper<BeanType, SearchType> wrappedDataProvider = null;

    @PostConstruct
    public void buildGrid() {
        innerGrid = new Grid<>(getBeanType());
        if (isLoadDataOnInitialization()) {
            pagerLayout.setVisible(true);
            setSearchParametersForDataProvider();
        } else {
            pagerLayout.setVisible(false);
        }

        Panel searchParametersPanel = getSearchParametersPanel();
        if (searchParametersPanel != null) {
            VerticalLayout searchLayout = new VerticalLayout();
            searchLayout.addComponent(searchParametersPanel);
            HorizontalLayout buttonLayout = new HorizontalLayout();
            Button search = new Button("search", c -> {
                if (getBinder().isValid()) {
                    setSearchParametersForDataProvider();
                }
            });
            buttonLayout.addComponentsAndExpand(search);
            Button reset = new Button("reset", c -> {
                resetSearchParametersPanel();
                getBinder().readBean(getSearchParameters());
                if (!isLoadDataOnInitialization()) {
                    pagerLayout.setVisible(false);
                    dataInitialized = false;
                }
            });
            buttonLayout.addComponentsAndExpand(reset);
            searchLayout.addComponent(buttonLayout);
            addComponent(searchLayout);
        }
        pagerLayout.addComponent(pager);
        pagerLayout.addComponent(getInnerGrid());
        addComponent(pagerLayout);

        pager.setPageSize(getNumberOfItemsToShow());
        getInnerGrid().setHeightByRows(pager.getPageSize());
    }

    private void setSearchParametersForDataProvider() {
        try {
            if (getBinder().isValid()) {
                getBinder().writeBean(getSearchParameters());
                ConfigurableFilterDataProvider<BeanType, Void, SearchType> dataProvider = getWrappedDataProvider().withConfigurableFilter();
                dataProvider.setFilter(getSearchParameters());
                if (!dataInitialized) {
                    getInnerGrid().setDataProvider(dataProvider);
                    dataInitialized = true;
                    pagerLayout.setVisible(true);
                }
            }
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPagerInfo(@Observes SizeDeterminedEvent event) {
        if (getWrappedDataProvider().getUUID().equals(event.getUuid())) {
            pager.setRowCount(event.getSize());
            pagerLayout.addComponent(pager, 0);
        }
    }

    public void showData(@Observes PagerEvent event) {
        if (((Pager)event.getSource()).getPagerId().equals(pager.getPagerId())) {
            getInnerGrid().scrollTo(new Long((event.getCurrentPage() - 1) * pager.getPageSize()).intValue(), ScrollDestination.START);
        }
    }

    protected abstract SearchType getSearchParameters();

    protected void resetSearchParametersPanel() {}

    protected abstract Class<BeanType> getBeanType();

    protected Panel getSearchParametersPanel() {
        return null;
    }

    protected final Binder<SearchType> getBinder() {
        if (binder == null) {
            binder = new Binder<>();
        }
        return binder;
    }

    protected abstract DataProvider<BeanType, SearchType> getDataProvider();

    protected Grid<BeanType> getInnerGrid() {
        return innerGrid;
    }

    protected boolean isLoadDataOnInitialization() {
        return true;
    }

    protected int getNumberOfItemsToShow() {
        return 25;
    }

    private DataProviderWrapper<BeanType, SearchType> getWrappedDataProvider() {
        if (wrappedDataProvider == null) {
            wrappedDataProvider = new DataProviderWrapper<>(getDataProvider());
        }
        return wrappedDataProvider;
    }
}
