package net.cyberspirit;

import java.util.UUID;

public class SizeDeterminedEvent {
    private UUID uuid;
    private int size;
    private Object searchParameters;

    public SizeDeterminedEvent(UUID uuid, int size, Object searchParameters) {
        this.size = size;
        this.searchParameters = searchParameters;
        this.uuid = uuid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
