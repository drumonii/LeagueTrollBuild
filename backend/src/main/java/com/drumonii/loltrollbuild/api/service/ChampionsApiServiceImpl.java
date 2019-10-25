package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChampionsApiServiceImpl implements ChampionsApiService {

    @Autowired
    private ChampionsRepository championsRepository;

    @Override
    public List<Champion> qbe(ExampleSpecification<Champion> specification, Sort sort) {
        return championsRepository.findAll(specification, sort);
    }

    @Override
    public Optional<Champion> find(String value) {
        try {
            return championsRepository.findById(Integer.valueOf(value));
        } catch (NumberFormatException e) {
            return championsRepository.findByName(value);
        }
    }

    @Override
    public List<String> getTags() {
        return championsRepository.getTags();
    }

}
