package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemsApiServiceTest {

    @InjectMocks
    private ItemsApiService apiService = new ItemsApiServiceImpl();

    @Mock
    private ItemsRepository itemsRepository;

    @Nested
    @DisplayName("query by example")
    class Qbe {

        @Test
        void qbe() {
            ExampleSpecification<Item> specification = mock(ExampleSpecification.class);
            Sort sort = mock(Sort.class);

            apiService.qbe(specification, sort);

            verify(itemsRepository, times(1)).findAll(eq(specification), eq(sort));
        }

    }

    @Nested
    @DisplayName("find by id")
    class FindById {

        @Test
        void findById() {
            apiService.findById(34273);

            verify(itemsRepository, times(1)).findById(eq(34273));
        }

    }

    @Nested
    @DisplayName("for troll build")
    class ForTrollBuild {

        @Test
        void forTrollBuild() {
            apiService.forTrollBuild(GameMapUtil.SUMMONERS_RIFT_ID);

            verify(itemsRepository, times(1)).forTrollBuild(eq(GameMapUtil.SUMMONERS_RIFT_ID));
        }

    }

    @Nested
    @DisplayName("boots")
    class Boots {

        @Test
        void boots() {
            apiService.boots(GameMapUtil.SUMMONERS_RIFT_ID);

            verify(itemsRepository, times(1)).boots(eq(GameMapUtil.SUMMONERS_RIFT_ID));
        }

    }

    @Nested
    @DisplayName("trinkets")
    class Trinkets {

        @Test
        void trinkets() {
            apiService.trinkets(GameMapUtil.SUMMONERS_RIFT_ID);

            verify(itemsRepository, times(1)).trinkets(eq(GameMapUtil.SUMMONERS_RIFT_ID));
        }

    }

}
