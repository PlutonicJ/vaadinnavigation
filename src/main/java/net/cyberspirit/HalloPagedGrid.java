package net.cyberspirit;

import com.vaadin.cdi.ViewScoped;
import com.vaadin.data.provider.DataProvider;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class HalloPagedGrid extends PagedGrid<TestEntity, TestEntitySearchParameters> {

    @Inject
    private TestEntityDataProvider dataProvider;

    @Inject
    private TestEntitySearchParameters searchParameters;

    @Override
    @PostConstruct
    public void buildGrid() {
        super.buildGrid();
        getInnerGrid().setColumnOrder("test1", "test2");
    }

    @Override
    protected Class<TestEntity> getBeanType() {
        return TestEntity.class;
    }

//    @Override
//    protected void resetSearchParametersPanel() {
//        searchParameters.setTest1(null);
//        searchParameters.setTest2(null);
//    }

//    @Override
//    protected Panel getSearchParametersPanel() {
//        VerticalLayout content = new VerticalLayout();
//        Panel panel = new Panel(content);
//        TextField test1 = new TextField("test2");
//        content.addComponent(test1);
//        searchParameters = new TestEntitySearchParameters();
//        getBinder().readBean(searchParameters);
//        getBinder().forField(test1).withNullRepresentation("").bind(TestEntitySearchParameters::getTest1, TestEntitySearchParameters::setTest1);
//        return panel;
//    }


    @Override
    public TestEntitySearchParameters getSearchParameters() {
        return searchParameters;
    }

    @Override
    protected DataProvider<TestEntity, TestEntitySearchParameters> getDataProvider() {
        return dataProvider;
    }

    @Override
    protected boolean isLoadDataOnInitialization() {
        return true;
    }
}
