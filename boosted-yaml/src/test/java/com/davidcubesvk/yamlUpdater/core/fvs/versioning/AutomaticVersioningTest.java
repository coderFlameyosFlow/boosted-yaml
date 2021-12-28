package com.davidcubesvk.yamlUpdater.core.fvs.versioning;

import com.davidcubesvk.yamlUpdater.core.YamlFile;
import com.davidcubesvk.yamlUpdater.core.fvs.segment.Segment;
import com.davidcubesvk.yamlUpdater.core.settings.dumper.DumperSettings;
import com.davidcubesvk.yamlUpdater.core.settings.general.GeneralSettings;
import com.davidcubesvk.yamlUpdater.core.settings.loader.LoaderSettings;
import com.davidcubesvk.yamlUpdater.core.settings.updater.UpdaterSettings;
import com.davidcubesvk.yamlUpdater.core.fvs.Pattern;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AutomaticVersioningTest {

    // Pattern
    private static final Pattern PATTERN = new Pattern(Segment.range(1, Integer.MAX_VALUE), Segment.literal("."), Segment.range(0, 10));
    // Versioning
    private static final AutomaticVersioning VERSIONING = new AutomaticVersioning(PATTERN, "x");

    @Test
    void getDefSectionVersion() throws IOException {
        assertEquals(PATTERN.getVersion("1.4"), VERSIONING.getDefSectionVersion(createFile().getDefaults()));
    }

    @Test
    void getUserSectionVersion() throws IOException {
        assertEquals(PATTERN.getVersion("1.2"), VERSIONING.getUserSectionVersion(createFile()));
    }

    @Test
    void getOldest() {
        assertEquals(PATTERN.getFirstVersion(), VERSIONING.getFirstVersion());
    }

    @Test
    void updateVersionID() throws IOException {
        // Recreate file
        YamlFile userFile = YamlFile.create(new ByteArrayInputStream("x: 1.2\ny: true".getBytes(StandardCharsets.UTF_8)));
        // Update
        VERSIONING.updateVersionID(userFile, createFile().getDefaults());
        // Assert
        assertEquals("1.4", userFile.getString("x"));
    }

    private YamlFile createFile() throws IOException {
        return YamlFile.create(
                new ByteArrayInputStream("x: 1.2\ny: true".getBytes(StandardCharsets.UTF_8)),
                new ByteArrayInputStream("x: 1.4\ny: false".getBytes(StandardCharsets.UTF_8)),
                GeneralSettings.DEFAULT, LoaderSettings.DEFAULT, DumperSettings.DEFAULT, UpdaterSettings.DEFAULT);
    }
}