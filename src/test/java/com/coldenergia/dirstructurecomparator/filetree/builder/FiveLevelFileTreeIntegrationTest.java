package com.coldenergia.dirstructurecomparator.filetree.builder;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import com.coldenergia.dirstructurecomparator.filetree.FileTree;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createDirectory;
import static com.coldenergia.dirstructurecomparator.FileScaffolding.createFile;
import static org.junit.Assert.assertNotNull;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 10:59 PM
 */
public class FiveLevelFileTreeIntegrationTest extends AbstractFileTreeBuilderIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FiveLevelFileTreeIntegrationTest.class);

    private static final int DIR_AND_FILE_COUNT = 5;

    private String[] dirPathNames = {"dir1", "dir2", "dir3", "dir4", "dir5"};

    private String[] filePathNames = {"file1", "file2", "file3", "file4", "file5"};

    private List<Path> dirs = new ArrayList<>(5);

    private List<Path> files = new ArrayList<>(5);

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        Path parentDir = getTmpRootDirPath();
        for (int i = 0; i < DIR_AND_FILE_COUNT; i++) {
            String dirPathName = dirPathNames[i];
            Path dir = createDirectory(parentDir, dirPathName);
            parentDir = dir;
            dirs.add(dir);

            String filePathName = filePathNames[i];
            Path file = createFile(dir, filePathName);
            files.add(file);
        }
    }

    @Test
    public void shouldBuildFiveLevelTree() {
        String rootDirFullPath = dirs.get(0).toAbsolutePath().toString();
        FileTreeBuilder builder = new FileTreeBuilder(rootDirFullPath);
        FileTree tree = builder.buildFileTree();

        FileNode node = tree.getRoot();
        int level = 0;
        do {
            assertNotNull(node);
            if (level + 1 < DIR_AND_FILE_COUNT) {
                assertThatNodesContainPath(node.getLeaves(), dirs.get(level + 1));
            }
            assertThatNodesContainPath(node.getLeaves(), files.get(level));

            if (level + 1 < DIR_AND_FILE_COUNT) {
                node = node.findLeafByPath(dirs.get(level + 1));
            }
            level++;
        } while (level < DIR_AND_FILE_COUNT);
    }
}
