package com.coldenergia.dirstructurecomparator.filetree.builder;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import com.coldenergia.dirstructurecomparator.filetree.FileTree;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createDirectory;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 10:55 AM
 */
public class TreeHeightIntegrationTest extends AbstractFileTreeBuilderIntegrationTest {

    private static final String BASE_DIR_NAME = "directory";

    private static final int LEVELS = 200;

    private List<Path> dirs = new ArrayList<>(LEVELS);

    private String rootDirFullPath;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        Path parentDir = getTmpRootDirPath();
        for (int level = 0; level < LEVELS; level++) {
            Path dir = createDirectory(parentDir, BASE_DIR_NAME + level);
            dirs.add(dir);
            parentDir = dir;
        }
        rootDirFullPath = dirs.get(0).toString();
    }

    @Test
    public void shouldCreateWholeTreeForNullMaxHeight() {
        FileTreeBuilder builder = new FileTreeBuilder(rootDirFullPath, null);
        FileTree tree = builder.buildFileTree();
        assertNodesFromZeroLevelUpToLevel(tree, LEVELS);
    }

    @Test
    public void shouldCreateWholeTreeForMaxHeightThatExceedsActualHeight() {
        FileTreeBuilder builder = new FileTreeBuilder(rootDirFullPath, LEVELS + 1);
        FileTree tree = builder.buildFileTree();
        FileNode theVeryFinalNode = assertNodesFromZeroLevelUpToLevel(tree, LEVELS);
        assertThat(theVeryFinalNode.getLeaves(), is(empty()));
    }

    @Test
    public void shouldCreateTreeForMaxHeightOfTen() {
        final int maxHeight = 10;
        FileTreeBuilder builder = new FileTreeBuilder(rootDirFullPath, maxHeight);
        FileTree tree = builder.buildFileTree();
        FileNode finalNodeAllowedByMaxHeight = assertNodesFromZeroLevelUpToLevel(tree, maxHeight);
        assertThat(
                "The tree height exceeds the maximum height of " + maxHeight,
                finalNodeAllowedByMaxHeight.getLeaves(), is(empty()));
    }

    /**
     * @return The last node - the one corresponding to the last level.
     * */
    private FileNode assertNodesFromZeroLevelUpToLevel(FileTree tree, int level) {
        FileNode node = null;
        for (int currentLevel = 0; currentLevel < level; currentLevel++) {
            node = getNextNode(tree, node);

            assertNotNull(node);
            Path expected = dirs.get(currentLevel).toAbsolutePath().normalize();
            Path actual = node.getPath().toAbsolutePath().normalize();
            assertEquals(expected, actual);
        }
        return node;
    }

    private FileNode getNextNode(FileTree tree, FileNode currentNode) {
        FileNode nextNode = null;
        if (currentNode != null) {
            Iterator<FileNode> nodeIterator = currentNode.getLeaves().iterator();
            assertTrue("Node " + currentNode + " has no leaf directory", nodeIterator.hasNext());
            nextNode = nodeIterator.next();
        } else {
            nextNode = tree.getRoot();
        }
        return nextNode;
    }
}
