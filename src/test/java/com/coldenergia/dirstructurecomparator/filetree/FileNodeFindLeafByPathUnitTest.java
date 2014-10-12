package com.coldenergia.dirstructurecomparator.filetree;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 9:25 AM
 */
public class FileNodeFindLeafByPathUnitTest {

    private FileNode root;

    private static final int LEAF_COUNT = 5;

    private static final String BASE_DIR = "./";

    private static final String BASE_FILE_NAME = "some-non-existent-file";

    @Before
    public void setup() {
        root = new FileNode(mock(Path.class));
        Set<FileNode> leaves = new HashSet<>(LEAF_COUNT);
        root.setLeaves(leaves);
        for (int i = 0; i < LEAF_COUNT; i++) {
            leaves.add(new FileNode(Paths.get(BASE_DIR + BASE_FILE_NAME + i)));
        }
    }

    @Test
    public void shouldFindLeafByRelativePath() {
        for (int i = 0; i < LEAF_COUNT; i++) {
            Path targetPath = Paths.get(BASE_DIR + BASE_FILE_NAME + i);
            FileNode node = root.findLeafByPath(targetPath);
            assertNotNull("There's no node with path " + targetPath, node);
            assertEquals(targetPath.normalize(), node.getPath().normalize());
        }
    }

    @Test
    public void shouldFindLeafByNonNormalizedPath() {
        for (int i = 0; i < LEAF_COUNT; i++) {
            Path targetPath = Paths.get(BASE_DIR + "dummy/../" + BASE_FILE_NAME + i);
            FileNode node = root.findLeafByPath(targetPath);
            assertNotNull("There's no node with path " + targetPath, node);
            assertEquals(targetPath.normalize(), node.getPath().normalize());
        }
    }

    @Test
    public void shouldFindLeafByAbsolutePath() {
        for (int i = 0; i < LEAF_COUNT; i++) {
            Path targetPath = Paths.get(BASE_DIR + BASE_FILE_NAME + i).toAbsolutePath();
            FileNode node = root.findLeafByPath(targetPath);
            assertNotNull("There's no node with path " + targetPath, node);
            assertEquals(targetPath.toAbsolutePath().normalize(), node.getPath().toAbsolutePath().normalize());
        }
    }

    @Test
    public void shouldFindNullLeafByNonExistentPath() {
        FileNode node = root.findLeafByPath(Paths.get("./non-existent"));
        assertNull(node);
    }
}
