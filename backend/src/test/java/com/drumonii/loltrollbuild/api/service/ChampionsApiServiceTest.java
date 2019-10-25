package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
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
class ChampionsApiServiceTest {

    @InjectMocks
    private ChampionsApiService apiService = new ChampionsApiServiceImpl();

    @Mock
    private ChampionsRepository championsRepository;

    @Nested
    @DisplayName("query by example")
    class Qbe {

        @Test
        void qbe() {
            ExampleSpecification<Champion> specification = mock(ExampleSpecification.class);
            Sort sort = mock(Sort.class);

            apiService.qbe(specification, sort);

            verify(championsRepository, times(1)).findAll(eq(specification), eq(sort));
        }

    }

    @Nested
    @DisplayName("find by id")
    class FindById {

        @Test
        void findsById() {
            apiService.find("48162");

            verify(championsRepository, times(1)).findById(eq(48162));
        }

    }

    @Nested
    @DisplayName("find by name")
    class FindByName {

        @Test
        void findsByName() {
            apiService.find("name");

            verify(championsRepository, times(1)).findByName(eq("name"));
        }

    }

    @Nested
    @DisplayName("get tags")
    class GetTags {

        @Test
        void getsTags() {
            apiService.getTags();

            verify(championsRepository, times(1)).getTags();
        }

    }

}
