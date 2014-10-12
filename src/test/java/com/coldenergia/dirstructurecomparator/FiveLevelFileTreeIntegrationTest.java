package com.coldenergia.dirstructurecomparator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 10:59 PM
 */
public class FiveLevelFileTreeIntegrationTest extends AbstractFileTreeBuilderIntegrationTest {

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

    /*@Test
    public void shouldBuildFiveLevelTree() {
        String rootDirFullPath = dirs.get(0).toAbsolutePath().toString();
        FileTreeBuilder builder = new FileTreeBuilder(rootDirFullPath);
        FileTree tree = builder.buildFileTree();

        FileNode node = tree.getRoot();
        assertNotNull(node);
        assertThatNodesContainPath(node.getLeaves(), dirs.get(i));
        assertThatNodesContainPath(node.getLeaves(), files.get(i));

        for (int i = 0; i < DIR_AND_FILE_COUNT; i++) {
        }
    }*/
}
