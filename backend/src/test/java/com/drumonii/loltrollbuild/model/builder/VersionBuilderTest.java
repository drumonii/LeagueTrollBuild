package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(JUnit4.class)
public class VersionBuilderTest {

    @Test
    public void buildsFromNormalPatch() {
        String patch = "7.10.1";

        Version version = new VersionBuilder()
                .fromPatch(patch)
                .build();

        assertThat(version.getMajor()).as("Major").isEqualTo(7);
        assertThat(version.getMinor()).as("Minor").isEqualTo(10);
        assertThat(version.getRevision()).as("Revision").isEqualTo(1);
    }

    @Test
    public void buildsFromlolpatchPatch() {
        String patch = "lolpatch_7.17";

        Version version = new VersionBuilder()
                .fromPatch(patch)
                .build();

        assertThat(version.getMajor()).as("Major").isEqualTo(7);
        assertThat(version.getMinor()).as("Minor").isEqualTo(17);
        assertThat(version.getRevision()).as("Revision").isEqualTo(0);
    }

    @Test
    public void throwsIllegalArgumentExceptionFromBuildingWithInvalidPatches() {
        List<String> patches = Arrays.asList("a.b.c", "", null);

        for (String patch : patches) {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> new VersionBuilder()
                            .fromPatch(patch)
                            .build())
                    .withMessage("Failed to build the Version from the provided patch: '%s'", patch);
        }
    }

}
