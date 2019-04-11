package com.drumonii.loltrollbuild.test.batch;

import com.drumonii.loltrollbuild.model.Version;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import static org.assertj.core.api.Fail.fail;

public abstract class AbstractBatchTests {

    protected Version latestVersion;

    public abstract void before();

    protected JobParameters getJobParameters() {
        if (latestVersion == null) {
            fail("Latest Version was null. It must be initialized in the before implementation");
        }
        return new JobParametersBuilder()
                .addString("latestRiotPatch", latestVersion.getPatch())
                .addDouble("random", Math.random())
                .toJobParameters();
    }

}
