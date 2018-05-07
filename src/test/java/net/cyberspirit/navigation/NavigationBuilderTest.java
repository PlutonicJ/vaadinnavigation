package net.cyberspirit.navigation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class NavigationBuilderTest {

    @Test
    public void test() {
        NavigationElement navigationElement = new NavigationBuilder()
                .expandable("test")
                    .internalLink("test", "testView")
                .endExpandable()
            .build();

        assertNotNull(navigationElement);

        navigationElement = new NavigationBuilder()
                .internalLink("test1", "test1")
                .internalLink("test2", "test2")
            .build();

        assertNotNull(navigationElement);
    }
}