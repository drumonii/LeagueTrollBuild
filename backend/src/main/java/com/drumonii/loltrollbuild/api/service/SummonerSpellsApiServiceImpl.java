package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SummonerSpellsApiServiceImpl implements SummonerSpellsApiService {

    @Autowired
    private SummonerSpellsRepository summonerSpellsRepository;

    @Override
    public List<SummonerSpell> qbe(ExampleSpecification<SummonerSpell> specification, Sort sort) {
        return summonerSpellsRepository.findAll(specification, sort);
    }

    @Override
    public List<SummonerSpell> forTrollBuild(GameMode mode) {
        return summonerSpellsRepository.forTrollBuild(mode);
    }

    @Override
    public Optional<SummonerSpell> findById(int id) {
        return summonerSpellsRepository.findById(id);
    }

}
