package net.cyberspirit;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Named
public class TestEntityDataProvider extends AbstractBackEndDataProvider<TestEntity, TestEntitySearchParameters> {

    public static final int NUMBER_OF_ITEMS = 10000;

    @Override
    protected Stream<TestEntity> fetchFromBackEnd(Query<TestEntity, TestEntitySearchParameters> query) {
        Stream<TestEntity> result = getTestEntityStream(query);
        return result;
    }

    private Stream<TestEntity> getTestEntityStream(Query<TestEntity, TestEntitySearchParameters> query) {
        List<TestEntity> list = new ArrayList<>();
        int numberOfAdditionalItems = Math.min(NUMBER_OF_ITEMS, (query.getLimit() * (query.getOffset() + 1)));
        for (int i = 0; i < numberOfAdditionalItems; i++) {
            list.add(new TestEntity("test3", "test1"));
        }
        Stream<TestEntity> result = list.stream();
        TestEntitySearchParameters filter = query.getFilter().orElse(null);
        if (filter != null) {
            result = result.filter(e -> {
                boolean matched = true;
                if (filter.getTest1() != null && !e.getTest1().equals(filter.getTest1())) {
                    matched = false;
                }
                if (filter.getTest2() != null && !e.getTest2().equals(filter.getTest2())) {
                    matched = false;
                }
                return matched;
            });
        }
        result = result.skip(query.getOffset());
        result = result.limit(query.getLimit());

        return result;
    }

    @Override
    protected int sizeInBackEnd(Query<TestEntity, TestEntitySearchParameters> query) {
        // return new Long(getTestEntityStream(query).count()).intValue();
        return new Long(NUMBER_OF_ITEMS).intValue();
    }
}
