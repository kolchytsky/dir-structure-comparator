package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.Side;
import com.coldenergia.dirstructurecomparator.builder.DiffCollectorNodeBuilder;
import com.coldenergia.dirstructurecomparator.builder.LevelFragmentBuilder;
import com.coldenergia.dirstructurecomparator.filetree.diff.DetachedFile;
import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createDirectory;
import static com.coldenergia.dirstructurecomparator.FileScaffolding.createFile;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * User: coldenergia
 * Date: 10/15/14
 * Time: 9:42 PM
 */
public class DeeplyNestedDirsIntegrationTest extends AbstractDirComparatorIntegrationTest {

    private DifferenceCollector expectedCollector;

    private LevelFragment rootLevelFragment;

    private static final int MAX_LEVELS = 50;

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        this.rootLevelFragment = createExpectedRoot();
        expectedCollector = new DifferenceCollector(rootLevelFragment.getCollectorNode());
        createLevel(rootLevelFragment);
    }

    private LevelFragment createExpectedRoot() throws IOException {
        LevelFragmentBuilder levelFragmentBuilder = new LevelFragmentBuilder();
        DiffCollectorNodeBuilder nodeBuilder = new DiffCollectorNodeBuilder();

        for (Side side : Side.values()) {
            Path dir = createDirectory(getTmpRootDirPath(), side.getValue() + "-dir");
            levelFragmentBuilder.withPath(side, dir);

            DetachedFile file = new DetachedFile(dir.toAbsolutePath().normalize().toString(), true);
            nodeBuilder.withFile(file, side);
        }

        DiffCollectorNode root = nodeBuilder.build();
        levelFragmentBuilder.withCollectorNode(root).withLevel(1);

        LevelFragment levelFragment = levelFragmentBuilder.build();
        return levelFragment;
    }

    private void createLevel(LevelFragment parentLevelFragment) throws IOException {
        if (parentLevelFragment.getLevel() >= MAX_LEVELS) {
            return;
        }

        LevelFragment levelFragment = createFiveFilesAndReturnFirst(parentLevelFragment);
        createLevel(levelFragment);
    }

    private LevelFragment createFiveFilesAndReturnFirst(LevelFragment levelFragment) throws IOException {
        int level = levelFragment.getLevel();

        LevelFragment coreLevelFragment = createCommonFileForBothSides(levelFragment, "directory-" + level, true);

        createCommonFileForBothSides(levelFragment, "file-" + level, false);
        createCommonFileForBothSides(levelFragment, "another-directory-" + level, true);

        for (Side side : Side.values()) {
            createFileForOneSide(levelFragment, side, "directory-" + side.getValue() + "-" + level, true);
            createFileForOneSide(levelFragment, side, "file-" + side.getValue()+ "-" + level, false);
        }

        return coreLevelFragment;
    }

    private LevelFragment createCommonFileForBothSides(LevelFragment levelFragment, String fileName, boolean isDirectory)
            throws IOException {
        LevelFragmentBuilder levelFragmentBuilder = new LevelFragmentBuilder();
        DiffCollectorNodeBuilder nodeBuilder = new DiffCollectorNodeBuilder();

        for (Side side : Side.values()) {
            DetachedFile file = new DetachedFile(fileName, isDirectory);
            Path nested = null;

            if (isDirectory) {
                nested = createDirectory(levelFragment.getPathBySide(side), fileName);
            } else {
                nested = createFile(levelFragment.getPathBySide(side), fileName);
            }

            nodeBuilder.withFile(file, side);
            levelFragmentBuilder.withPath(side, nested);
        }

        DiffCollectorNode node = nodeBuilder.build();
        levelFragmentBuilder.withCollectorNode(node).withLevel(levelFragment.getLevel() + 1);

        DiffCollectorNode parentNode = levelFragment.getCollectorNode();
        parentNode.addLeaf(node);

        return levelFragmentBuilder.build();
    }

    private Path createFileForOneSide(LevelFragment levelFragment, Side side, String fileName, boolean isDirectory)
            throws IOException {
        Path nested = null;

        if (isDirectory) {
            nested = createDirectory(levelFragment.getPathBySide(side), fileName);
        } else {
            nested = createFile(levelFragment.getPathBySide(side), fileName);
        }

        DiffCollectorNodeBuilder nodeBuilder = new DiffCollectorNodeBuilder();
        DetachedFile file = new DetachedFile(fileName, isDirectory);
        DiffCollectorNode node = nodeBuilder.withFile(file, side).build();

        DiffCollectorNode parentNode = levelFragment.getCollectorNode();
        parentNode.addLeaf(node);

        return nested;
    }

    @Test
    public void shouldCreateDirComparisonTree() {
        DirComparator dirComparator = new DirComparator();
        String left = rootLevelFragment.getPathBySide(Side.LEFT).toAbsolutePath().normalize().toString();
        String right = rootLevelFragment.getPathBySide(Side.RIGHT).toAbsolutePath().normalize().toString();

        DifferenceCollector collector = dirComparator.compareDirectories(left, right);
        assertNotNull(collector);

        int level = 1;
        DiffCollectorNode currentNode = collector.getRoot();
        assertNotNull(currentNode);

        while (level < MAX_LEVELS) {
            Set<DiffCollectorNode> leaves = currentNode.getLeaves();

            boolean isLastLevel = level == (MAX_LEVELS - 1);
            int expectedLeavesCount = isLastLevel ? 4 : 5;
            assertThat(
                    "The node doesn't have enough leaves. Error has occurred on level " + level,
                    leaves, hasSize(expectedLeavesCount)
            );

            for (Side side : Side.values()) {
                assertThatDifferencesContainName(leaves, "directory-" + side.getValue() + "-" + level, side);
                assertThatDifferencesContainName(leaves, "file-" + side.getValue() + "-" + level, side);
            }

            DiffCollectorNode commonDirectoryLeaf = getCommonDirectoryLeaf(leaves, level);
            if (!isLastLevel) {
                assertNotNull("Common directory leaf is null. Error has occurred on level " + level, commonDirectoryLeaf);
            } else {
                assertNull(
                        "There should be no common directory in differences on the last level, "
                        + "as its contents are the same - empty!",
                        commonDirectoryLeaf);
            }

            currentNode = commonDirectoryLeaf;
            level++;
        }
    }

    private DiffCollectorNode getCommonDirectoryLeaf(Set<DiffCollectorNode> leaves, int level) {
        DiffCollectorNode commonDirectoryLeaf = null;
        for (DiffCollectorNode node : leaves) {
            boolean hasInLeft = node.getLeftFile() != null && node.getLeftFile().getFileName().equals("directory-" + level);
            boolean hasInRight = node.getRightFile() != null && node.getRightFile().getFileName().equals("directory-" + level);
            if (hasInLeft && hasInRight) {
                commonDirectoryLeaf = node;
                break;
            }
        }
        return commonDirectoryLeaf;
    }
}
