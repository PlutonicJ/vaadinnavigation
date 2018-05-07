package net.cyberspirit;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Logger;


@Dependent
public class SimpleCSSPager extends CssLayout implements Pager {

    private static final int MAX_PAGER_VISIBLE_PAGE_NUMBERS = 8;

    private boolean showPager = true;

    @Inject
    private javax.enterprise.event.Event<PagerEvent> pagerEvent;

    private long currentPage = 1;
    private long pageSize  = 25;
    private long rowCount;
    private long lastPage;
    private UUID uuid;

    public SimpleCSSPager() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public void attach() {
        super.attach();
        rebuildPager();
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    /**
     * Rebuilds the pager for the current page.
     */
    private void rebuildPager(/*boolean addComponent*/) {

        lastPage = rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize +1;

        if ( showPager == false )
            return;

        removeAllComponents();

        // Total row and current page hint.
        if (rowCount == 0) {
            addPagerLabel("No data");
        } else {
            String pattern = "Data from {0} to {1}, {2} total." + " ";
            long pageOffset = (currentPage - 1) * pageSize;
            long firstDisplayedRow = pageOffset + 1;
            long lastDisplayedRow = (currentPage == lastPage ? rowCount : pageOffset + pageSize);
            addPagerLabel(MessageFormat.format(pattern, firstDisplayedRow, lastDisplayedRow, rowCount));
        }

        if (rowCount > 0) {
            // First/Prev navigation
            addPagerLabel("[");
            if (currentPage > 1) {
                addPagerButton(PagerLinkType.TO_FIRST, "First");
                addPagerLabel("/");
                addPagerButton(PagerLinkType.TO_PREVIOUS, "Previous");
            } else {
                addPagerLabel("First" + "/" + "Last");
            }
            addPagerLabel("]");

            // Get the first and the last visible page link to be shown by the pager.
            long firstVisiblePageLink = getFirstVisiblePageLink();
            long lastVisiblePageLink = getLastVisiblePageLink(firstVisiblePageLink);

            // With first and last page link being set now render the page links
            for (long i = firstVisiblePageLink; i <= lastVisiblePageLink; i++) {
                if (i == currentPage) {
                    addPagerLabel("Page" + " " + i);
                } else {
                    addPagerButton(PagerLinkType.TO_PAGE, i);
                }
                if (i < lastVisiblePageLink) {
                    addPagerLabel(",");
                }
            }

            // Next/Last navigation
            addPagerLabel("[");
            if (currentPage < lastPage) {
                addPagerButton(PagerLinkType.TO_NEXT, "Next");
                addPagerLabel("/");
                addPagerButton(PagerLinkType.TO_LAST, "Last");
            } else {
                addPagerLabel("Next" + "/" + "Last");
            }
            addPagerLabel("]");
        } // pagerData.getRowCount() > 0
    }

    /**
     * Calculate the first visible page link to be shown by the pager.
     *
     * @return first visible page link number
     */
    private long getFirstVisiblePageLink() {
        long firstVisiblePageLink = 1;
        if ((currentPage <= (MAX_PAGER_VISIBLE_PAGE_NUMBERS / 2))
                || (lastPage - MAX_PAGER_VISIBLE_PAGE_NUMBERS <= 0)) {
            firstVisiblePageLink = 1;
        } else {
            if (currentPage >= lastPage - (MAX_PAGER_VISIBLE_PAGE_NUMBERS / 2)) {
                firstVisiblePageLink = lastPage - MAX_PAGER_VISIBLE_PAGE_NUMBERS + 1;
            } else {
                firstVisiblePageLink = currentPage - (MAX_PAGER_VISIBLE_PAGE_NUMBERS / 2);
            }
        }
        return firstVisiblePageLink;
    }

    /**
     * Calculate the last visible page link to be shown by the pager.
     *
     * @return last visible page link number
     */
    private long getLastVisiblePageLink(long firstVisiblePageLink) {
        long lastVisiblePageLink = 1;
        if (lastPage - MAX_PAGER_VISIBLE_PAGE_NUMBERS <= 0 || (currentPage >= lastPage - (MAX_PAGER_VISIBLE_PAGE_NUMBERS / 2))) {
            lastVisiblePageLink = lastPage;
        } else {
            if (firstVisiblePageLink + MAX_PAGER_VISIBLE_PAGE_NUMBERS - 1 <= lastPage) {
                lastVisiblePageLink = firstVisiblePageLink + MAX_PAGER_VISIBLE_PAGE_NUMBERS - 1;
            } else {
                lastVisiblePageLink = currentPage + (MAX_PAGER_VISIBLE_PAGE_NUMBERS / 2);
            }
        }
        return lastVisiblePageLink;
    }

    /**
     * Add page number to pager as label.
     *
     * @param label label value
     * @return label instance
     */
    private Label addPagerLabel(String label) {
        Label pageLabel = new Label(label);
        pageLabel.setPrimaryStyleName("pagerlabel");
        pageLabel.setSizeUndefined();
        pageLabel.setStyleName("pagerData.getCurrentPage()");
        addComponent(pageLabel);
        return pageLabel;
    }

    /**
     * Adds a pager button to the layout.
     *
     * @param pagerLinkType link type of the pager
     * @param label label value
     * @return button instance
     */
    private Button addPagerButton(final PagerLinkType pagerLinkType, final String label) {
        return addPagerButton(pagerLinkType, label, -1);
    }

    /**
     * Adds a pager button to the layout.
     *
     * @param pagerLinkType link type of the pager
     * @param pageToGo page to go to
     * @return button instance
     */
    private Button addPagerButton(final PagerLinkType pagerLinkType, long pageToGo) {
        return addPagerButton(pagerLinkType, Long.toString(pageToGo), pageToGo);
    }

    /**
     *
     * @param pagerLinkType link type
     * @param label Button label
     * @return
     */
    @SuppressWarnings("serial")
    private Button addPagerButton(final PagerLinkType pagerLinkType, final String label, final long pageToGo) {
        Button button = new Button(label);
        button.setPrimaryStyleName("pagerlink");
        button.setSizeUndefined();
        button.addClickListener(e -> {
            switch (pagerLinkType) {
                case TO_FIRST:
                    currentPage = 1;
                    break;
                case TO_PREVIOUS:
                    currentPage = (currentPage == 1 ? 1 : currentPage-1);
                    break;
                case TO_NEXT:
                    currentPage = (currentPage == lastPage ? lastPage : currentPage+1);
                    break;
                case TO_LAST:
                    currentPage = lastPage;
                    break;
                case TO_PAGE:
                    currentPage = pageToGo;
            }
            fireReload();
        });
        addComponent(button);
        return button;
    }

    private void fireReload() {
        pagerEvent.fire(new PagerEvent(this,currentPage));
    }

    @Override
    public UUID getPagerId() {
        return uuid;
    }

    @Override
    public long getCurrentPage() {
        return currentPage;
    }

    @Override
    public long getPageSize() {
        return pageSize;
    }
}
