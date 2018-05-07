package net.cyberspirit;

import com.vaadin.ui.Component;

import java.util.UUID;

public interface Pager extends Component {

    enum PagerLinkType {
        TO_FIRST,
        TO_PREVIOUS,
        TO_NEXT,
        TO_LAST,
        TO_PAGE
    }

    void setCurrentPage(int currentPage);

    void setPageSize(int pageSize);

    void setRowCount(int rowCount);

    UUID getPagerId();

    long getCurrentPage();

    long getPageSize();
}
