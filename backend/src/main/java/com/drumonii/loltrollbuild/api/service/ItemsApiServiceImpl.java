package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.specification.ExampleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemsApiServiceImpl implements ItemsApiService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Override
    public List<Item> qbe(ExampleSpecification<Item> specification, Sort sort) {
        return itemsRepository.findAll(specification, sort);
    }

    @Override
    public Optional<Item> findById(int id) {
        return itemsRepository.findById(id);
    }

    @Override
    public List<Item> boots(int mapId) {
        return itemsRepository.boots(mapId);
    }

    @Override
    public List<Item> forTrollBuild(int mapId) {
        return itemsRepository.forTrollBuild(mapId);
    }

    @Override
    public List<Item> trinkets(int mapId) {
        return itemsRepository.trinkets(mapId);
    }

}
