package net.cyberspirit;

import java.util.EventObject;

public class PagerEvent extends EventObject {

    private long currentPage = 1;

    public PagerEvent(Object source) {
        super(source);
    }

    public PagerEvent(Object source, long currentPage) {
        super(source);
        this.currentPage = currentPage;
    }

    public long getCurrentPage() {
        return currentPage;
    }

}
