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
 * Time: 4:37 PM
 */
public class NestedSyncIntegrationTest extends AbstractSyncIntegrationTest {

    private Path leftDir, rightDir, nestedLeftDir, nestedRightDir;

    private Set<Item> nestedLeftItems, nestedRightItems;

    @Before
    public void setup() throws IOException {
        super.setup();
        leftDir = createDirectory(getTmpRootDirPath(), "left");
        rightDir = createDirectory(getTmpRootDirPath(), "right");
        nestedLeftDir = createDirectory(leftDir, "nested");
        nestedRightDir = createDirectory(rightDir, "nested");

        nestedRightItems = new HashSet<>();
        nestedRightItems.add(new Item("file1", false, 45));
        nestedRightItems.add(new Item("file2", false, 55));
        nestedRightItems.add(new Item("file3", false, 554));

        nestedLeftItems = new HashSet<>();
        nestedLeftItems.add(new Item("file3", false, 554));
        nestedLeftItems.add(new Item("file4", false, 54));

        for (Item item : nestedRightItems) {
            createItem(nestedRightDir, item);
        }
        for (Item item : nestedLeftItems) {
            createItem(nestedLeftDir, item);
        }
    }

    @Test
    public void shouldCopyFilesInNestedDirectory() throws IOException {
        DirComparator dirComparator = new DirComparator();
        DifferenceCollector diffCollector = dirComparator.compareDirectories(leftDir.toString(), rightDir.toString());
        Sync sync = new Sync(diffCollector);
        sync.sync(Side.RIGHT, SyncOption.COPY);

        Set<Item> allItems = new HashSet<>();
        allItems.addAll(nestedLeftItems);
        allItems.addAll(nestedRightItems);

        for (Item item : allItems) {
            Path path = Paths.get(nestedLeftDir.toString(), item.name);
            assertTrue(path + " doesn't exist.", Files.exists(path));
            assertEquals(item.isDirectory, Files.isDirectory(path));
            if (!item.isDirectory) {
                assertEquals((long) item.sizeInBytes, Files.size(path));
            }
        }
    }
}
