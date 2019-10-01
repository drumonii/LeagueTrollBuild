package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Builder for {@link Version}s.
 */
public final class VersionBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionBuilder.class);

    private Integer major;
    private Integer minor;
    private Integer revision;

    private String patchToParse;

    public VersionBuilder fromPatch(String patch) {
        this.patchToParse = patch;
        if (patch != null && !patch.isEmpty()) {
            return parsePatch(patch);
        }
        return this;
    }

    private VersionBuilder parsePatch(String patch) {
        String[] versioning = patch.split("\\."); // 7.10.1 style
        if (versioning.length == 3) {
            return parseNormalStyle(versioning);
        } else if (patch.contains("lolpatch_")) {
            versioning = patch.substring(patch.lastIndexOf('_') + 1).split("\\.");
            return parselolpatchStyle(versioning);
        }
        return this;
    }

    private VersionBuilder parseNormalStyle(String[] versioning) {
        withMajor(parseIntoInteger(versioning[0]));
        withMinor(parseIntoInteger(versioning[1]));
        withRevision(parseIntoInteger(versioning[2]));
        return this;
    }

    private VersionBuilder parselolpatchStyle(String[] versioning) {
        withMajor(parseIntoInteger(versioning[0]));
        withMinor(parseIntoInteger(versioning[1]));
        withRevision(0);
        return this;
    }

    private Integer parseIntoInteger(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            LOGGER.warn("Unable to parse {} into an Integer", integer, e);
            return null;
        }
    }

    public VersionBuilder withMajor(Integer major) {
        this.major = major;
        return this;
    }

    public VersionBuilder withMinor(Integer minor) {
        this.minor = minor;
        return this;
    }

    public VersionBuilder withRevision(Integer revision) {
        this.revision = revision;
        return this;
    }

    public Version build() {
        Assert.noNullElements(new Integer[] { major, minor, revision }, "Failed to build the Version from the provided patch: '" + patchToParse + "'");

        Version version = new Version();
        version.setMajor(major);
        version.setMinor(minor);
        version.setRevision(revision);
        version.setPatch(major + "." + minor + "." + revision);
        return version;
    }

}
