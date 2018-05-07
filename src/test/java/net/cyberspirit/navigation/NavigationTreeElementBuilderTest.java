package net.cyberspirit.navigation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class NavigationTreeElementBuilderTest {

    @Test
    public void test() {
        NavigationElement navigationElement = NavigationElement.builder()
                .expandable("test")
                .internalLink("test", "testView")
                .endExpandable()
                .build();

        assertNotNull(navigationElement);
    }
}