package com.coldenergia.dirstructurecomparator.sync;

import com.coldenergia.dirstructurecomparator.comparator.DirComparator;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;
import com.coldenergia.dirstructurecomparator.filetree.diff.Side;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: coldenergia
 * Date: 11/2/14
 * Time: 2:28 PM
 */
public class SimpleSyncIntegrationTest extends AbstractSyncIntegrationTest {

    private Path leftDir, rightDir;

    private Set<Item> leftItems, rightItems;

    @Before
    public void setup() throws IOException {
        super.setup();
        leftDir = createDirectory(getTmpRootDirPath(), "left");
        rightDir = createDirectory(getTmpRootDirPath(), "right");

        leftItems = new HashSet<>();
        leftItems.add(new Item("file1", false, 10));
        leftItems.add(new Item("file2", false, 0));
        leftItems.add(new Item("file3", false, 0));
        leftItems.add(new Item("file4", false, 56));
        leftItems.add(new Item("dir1", true));
        leftItems.add(new Item("dir2", true));

        rightItems = new HashSet<>();
        rightItems.add(new Item("file1", false, 10));
        rightItems.add(new Item("file3", false, 0));
        rightItems.add(new Item("file5", false, 670));
        rightItems.add(new Item("dir2", true));
        rightItems.add(new Item("dir3", true));

        for (Item item : leftItems) {
            createItem(leftDir, item);
        }
        for (Item item : rightItems) {
            createItem(rightDir, item);
        }
    }

    @Test
    public void shouldCopyUnexistingFilesFromLeftToRight() throws IOException {
        DirComparator dirComparator = new DirComparator();
        DifferenceCollector diffCollector = dirComparator.compareDirectories(leftDir.toString(), rightDir.toString());
        Sync sync = new Sync(diffCollector);
        sync.sync(Side.LEFT, SyncOption.COPY);

        Set<Item> allItems = new HashSet<>();
        allItems.addAll(leftItems);
        allItems.addAll(rightItems);

        for (Item item : allItems) {
            Path path = Paths.get(rightDir.toString(), item.name);
            assertTrue(path + " doesn't exist.", Files.exists(path));
            assertEquals(item.isDirectory, Files.isDirectory(path));
            if (!item.isDirectory) {
                assertEquals((long) item.sizeInBytes, Files.size(path));
            }
        }
    }
}
