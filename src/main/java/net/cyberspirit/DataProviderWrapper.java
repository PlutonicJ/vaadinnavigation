package net.cyberspirit;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.Query;
import com.vaadin.shared.Registration;

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.UUID;
import java.util.stream.Stream;

public class DataProviderWrapper<BeanType, SearchType> implements DataProvider<BeanType, SearchType> {
    private DataProvider<BeanType, SearchType> dataProvider;
    private UUID uuid;

    public DataProviderWrapper(DataProvider<BeanType, SearchType> dataProvider) {
        this.dataProvider = dataProvider;
        uuid = UUID.randomUUID();
    }

    @Override
    public boolean isInMemory() {
        return dataProvider.isInMemory();
    }

    @Override
    public int size(Query<BeanType, SearchType> query) {
        int size = dataProvider.size(query);
        getBeanManager().fireEvent(new SizeDeterminedEvent(uuid, size, query.getFilter().orElse(null)));
        return size;
    }

    private BeanManager getBeanManager() {
        try {
            return (BeanManager)new InitialContext().lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<BeanType> fetch(Query<BeanType, SearchType> query) {
        Stream<BeanType> fetch = dataProvider.fetch(query);
        getBeanManager().fireEvent(new DataLoadedEvent(uuid, fetch, query.getFilter().orElse(null)));
        return fetch;
    }

    @Override
    public void refreshItem(BeanType item) {
        dataProvider.refreshItem(item);
    }

    @Override
    public void refreshAll() {
        dataProvider.refreshAll();
    }

    @Override
    public Registration addDataProviderListener(DataProviderListener<BeanType> listener) {
        return dataProvider.addDataProviderListener(listener);
    }

    public UUID getUUID() {
        return uuid;
    }
}
