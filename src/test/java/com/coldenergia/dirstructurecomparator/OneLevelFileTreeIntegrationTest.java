package com.coldenergia.dirstructurecomparator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 10:09 PM
 */
public class OneLevelFileTreeIntegrationTest extends AbstractFileTreeBuilderIntegrationTest {

    private Path oneLevelDir = null;

    private Path file1 = null;

    private Path file2 = null;

    private Path emptyEmbeddedDir = null;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        oneLevelDir = createDirectory(getTmpRootDirPath(), "dir1");
        file1 = createFile(oneLevelDir, "file1");
        file2 = createFile(oneLevelDir, "file2");
        emptyEmbeddedDir = createDirectory(oneLevelDir, "emptyDir");
    }

    @Test
    public void shouldCreateOneLevelTree() {
        FileTreeBuilder builder = new FileTreeBuilder(oneLevelDir.toString());
        FileTree tree = builder.buildFileTree();
        assertNotNull(tree);

        FileNode rootNode = tree.getRoot();
        assertNotNull(rootNode);
        assertEquals(oneLevelDir, rootNode.getPath());

        Set<FileNode> rootLeaves = rootNode.getLeaves();
        assertNotNull(rootLeaves);
        assertThat(rootLeaves, hasSize(3));

        assertThatNodesContainPath(rootLeaves, file1);
        assertThatNodesContainPath(rootLeaves, file2);
        assertThatNodesContainPath(rootLeaves, emptyEmbeddedDir);
    }
}
