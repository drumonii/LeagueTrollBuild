package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VersionsApiServiceImpl implements VersionsApiService {

    @Autowired
    private VersionsRepository versionsRepository;

    @Override
    public List<Version> qbe(Example<Version> example, Sort sort) {
        return versionsRepository.findAll(example, sort);
    }

    @Override
    public Optional<Version> findById(String patch) {
        return versionsRepository.findById(patch);
    }

    @Override
    public Version latestVersion() {
        return versionsRepository.latestVersion();
    }

}
