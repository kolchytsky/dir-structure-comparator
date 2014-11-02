package com.coldenergia.dirstructurecomparator.sync;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createDirectory;
import static com.coldenergia.dirstructurecomparator.FileScaffolding.createFile;
import static com.coldenergia.dirstructurecomparator.FileScaffolding.createTmpDir;
import static com.coldenergia.dirstructurecomparator.FileScaffolding.deleteTmpDir;

/**
 * User: coldenergia
 * Date: 11/2/14
 * Time: 2:31 PM
 */
public class AbstractSyncIntegrationTest {

    private Path tmpRootDirPath = null;

    @Before
    public void setup() throws IOException {
        tmpRootDirPath = Paths.get("./sync_test");
        createTmpDir(tmpRootDirPath);
    }

    @After
    public void teardown() throws IOException {
        deleteTmpDir(tmpRootDirPath);
    }

    public Path getTmpRootDirPath() {
        return this.tmpRootDirPath;
    }

    public void createItem(Path dir, Item item) throws IOException {
        if (item.isDirectory) {
            createDirectory(dir, item.name);
        } else if (item.sizeInBytes > 0) {
            createFile(dir, item.name, item.sizeInBytes);
        } else {
            createFile(dir, item.name);
        }
    }

    public static class Item {

        public Item(String name, boolean isDirectory) {
            this.name = name;
            this.isDirectory = isDirectory;
            this.sizeInBytes = null;
        }

        public Item(String name, boolean isDirectory, long sizeInBytes) {
            this.name = name;
            this.isDirectory = isDirectory;
            this.sizeInBytes = sizeInBytes;
        }

        public String name;

        public boolean isDirectory;

        public Long sizeInBytes;
    }
}
