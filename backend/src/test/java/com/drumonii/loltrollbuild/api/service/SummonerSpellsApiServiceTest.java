package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
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
class SummonerSpellsApiServiceTest {

    @InjectMocks
    private SummonerSpellsApiService apiService = new SummonerSpellsApiServiceImpl();

    @Mock
    private SummonerSpellsRepository summonerSpellsRepository;

    @Nested
    @DisplayName("query by example")
    class Qbe {

        @Test
        void qbe() {
            ExampleSpecification<SummonerSpell> specification = mock(ExampleSpecification.class);
            Sort sort = mock(Sort.class);

            apiService.qbe(specification, sort);

            verify(summonerSpellsRepository, times(1)).findAll(eq(specification), eq(sort));
        }

    }

    @Nested
    @DisplayName("for troll build")
    class ForTrollBuild {

        @Test
        void forTrollBuild() {
            apiService.forTrollBuild(GameMode.CLASSIC);

            verify(summonerSpellsRepository, times(1)).forTrollBuild(eq(GameMode.CLASSIC));
        }

    }

    @Nested
    @DisplayName("find by id")
    class FindById {

        @Test
        void findById() {
            apiService.findById(56745);

            verify(summonerSpellsRepository, times(1)).findById(eq(56745));
        }

    }

}
