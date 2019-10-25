package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
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
class MapsApiServiceTest {

    @InjectMocks
    private MapsApiService apiService = new MapsApiServiceImpl();

    @Mock
    private MapsRepository mapsRepository;

    @Nested
    @DisplayName("query by example")
    class Qbe {

        @Test
        void qbe() {
            ExampleSpecification<GameMap> specification = mock(ExampleSpecification.class);
            Sort sort = mock(Sort.class);

            apiService.qbe(specification, sort);

            verify(mapsRepository, times(1)).findAll(eq(specification), eq(sort));
        }

    }

    @Nested
    @DisplayName("find by id")
    class FindById {

        @Test
        void findById() {
            apiService.findById(983261);

            verify(mapsRepository, times(1)).findById(eq(983261));
        }

    }

    @Nested
    @DisplayName("for troll build")
    class ForTrollBuild {

        @Test
        void forTrollBuild() {
            apiService.forTrollBuild();

            verify(mapsRepository, times(1)).forTrollBuild();
        }

    }

}
