package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.model.BatchJobInstance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * JPA repository to the BATCH_JOB_INSTANCE table.
 */
public interface BatchJobInstancesRepository extends JpaRepository<BatchJobInstance, Long> {

    @Query("""
           select ji
           from BatchJobInstance ji
           where ji.name = 'allRetrievalsJob'
           """)
    List<BatchJobInstance> getMostRecentAllRetrievalsJob(Pageable pageable);

}
