package com.coldenergia.dirstructurecomparator.sync;

import com.coldenergia.dirstructurecomparator.comparator.DirComparator;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;
import com.coldenergia.dirstructurecomparator.filetree.diff.Side;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: coldenergia
 * Date: 11/2/14
 * Time: 5:49 PM
 */
public class NonExistentDirectorySyncIntegrationTest extends AbstractSyncIntegrationTest {

    private Path leftDir, rightDir;

    private Path nestedLeftDir;

    private Set<Item> nestedLeftItems;

    @Before
    public void setup() throws IOException {
        super.setup();
        leftDir = createDirectory(getTmpRootDirPath(), "x-left");
        rightDir = createDirectory(getTmpRootDirPath(), "x-right");
        nestedLeftDir = createDirectory(leftDir, "awesome");

        nestedLeftItems = new HashSet<>();
        nestedLeftItems.add(new Item("file", false, 0));
        nestedLeftItems.add(new Item("dir", true));

        for (Item item : nestedLeftItems) {
            createItem(nestedLeftDir, item);
        }
    }

    @Test
    public void shouldCreateNonExistentDirectory() {
        DirComparator dirComparator = new DirComparator();
        DifferenceCollector diffCollector = dirComparator.compareDirectories(leftDir.toString(), rightDir.toString());
        Sync sync = new Sync(diffCollector);
        sync.sync(Side.LEFT, SyncOption.COPY);

        Path nestedRightDir = rightDir.resolve(nestedLeftDir.getFileName());
        assertTrue(nestedRightDir + " doesn't exist.", Files.exists(nestedRightDir));

        for (Item item : nestedLeftItems) {
            Path path = nestedRightDir.resolve(item.name);
            assertTrue(path + " doesn't exist.", Files.exists(path));
            assertEquals(item.isDirectory, Files.isDirectory(path));
        }
    }
}
