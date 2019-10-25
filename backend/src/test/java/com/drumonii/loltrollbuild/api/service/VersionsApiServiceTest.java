package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersionsApiServiceTest {

    @InjectMocks
    private VersionsApiService apiService = new VersionsApiServiceImpl();

    @Mock
    private VersionsRepository versionsRepository;

    @Nested
    @DisplayName("query by example")
    class Qbe {

        @Test
        void qbe() {
            Example<Version> example = mock(Example.class);
            Sort sort = mock(Sort.class);

            apiService.qbe(example, sort);

            verify(versionsRepository, times(1)).findAll(eq(example), eq(sort));
        }

    }

    @Nested
    @DisplayName("find by id")
    class FindById {

        @Test
        void findsById() {
            apiService.findById("1.2.3");

            verify(versionsRepository, times(1)).findById(eq("1.2.3"));
        }

    }

    @Nested
    @DisplayName("latest version")
    class LatestVersion {

        @Test
        void latestVersion() {
            apiService.latestVersion();

            verify(versionsRepository, times(1)).latestVersion();
        }

    }

}
