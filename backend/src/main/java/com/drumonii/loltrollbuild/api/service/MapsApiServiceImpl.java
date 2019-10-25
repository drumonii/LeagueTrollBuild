package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapsApiServiceImpl implements MapsApiService {

    @Autowired
    private MapsRepository mapsRepository;

    @Override
    public List<GameMap> qbe(ExampleSpecification<GameMap> specification, Sort sort) {
        return mapsRepository.findAll(specification, sort);
    }

    @Override
    public Optional<GameMap> findById(int mapId) {
        return mapsRepository.findById(mapId);
    }

    @Override
    public List<GameMap> forTrollBuild() {
        return mapsRepository.forTrollBuild();
    }

}
