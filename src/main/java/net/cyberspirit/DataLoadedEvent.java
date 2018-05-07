package net.cyberspirit;

import java.util.UUID;
import java.util.stream.Stream;

public class DataLoadedEvent {
    private UUID uuid;
    private Stream<Object> data;
    private Object searchParameters;

    public DataLoadedEvent(UUID uuid, Stream data, Object searchParameters) {
        this.data = data;
        this.searchParameters = searchParameters;
        this.uuid = uuid;
    }

    public Stream<Object> getData() {
        return data;
    }

    public void setData(Stream<Object> data) {
        this.data = data;
    }

    public Object getSearchParameters() {
        return searchParameters;
    }

    public void setSearchParameters(Object searchParameters) {
        this.searchParameters = searchParameters;
    }

    public UUID getUuid() {
        return uuid;
    }
}
