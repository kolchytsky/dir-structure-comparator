package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createDirectory;
import static com.coldenergia.dirstructurecomparator.FileScaffolding.createFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 4:56 PM
 */
public class OneLevelDirComparisonIntegrationTest extends AbstractDirComparatorIntegrationTest {

    private Set<Path> onlyInLeft = new HashSet<>();

    private Set<Path> onlyInRight = new HashSet<>();

    private Set<Path> inBoth = new HashSet<>();

    @Before
    @Override
    public void setup() throws IOException {
        super.setup();
        setLeftDir(createDirectory(getTmpRootDirPath(), "first-dir"));
        inBoth.add(createFile(getLeftDir(), "construction.lvl"));
        onlyInLeft.add(createDirectory(getLeftDir(), "asylum"));
        onlyInLeft.add(createFile(getLeftDir(), "depot.lvl"));
        inBoth.add(createDirectory(getLeftDir(), "spillkill.lvl"));

        setRightDir(createDirectory(getTmpRootDirPath(), "second-dir"));
        onlyInRight.add(createFile(getRightDir(), "docks.lvl"));
        inBoth.add(createDirectory(getRightDir(), "spillkill.lvl"));
        inBoth.add(createFile(getRightDir(), "construction.lvl"));
    }

    @Test
    public void shouldBuildDifferenceCollector() {
        DirComparator comparator = new DirComparator();
        DifferenceCollector output = comparator.compareDirectories(getLeftDir().toString(), getRightDir().toString());
        assertNotNull(output);

        DiffCollectorNode root = output.getRoot();
        assertNotNull(root);
        assertEquals(getLeftDir().toAbsolutePath().normalize().toString(), root.getLeftFile().getFileName());
        assertEquals(getRightDir().toAbsolutePath().normalize().toString(), root.getRightFile().getFileName());

        final int differenceCount = onlyInLeft.size() + onlyInRight.size();
        assertThat(root.getLeaves(), hasSize(differenceCount));
        assertThatDifferencesContainName(root.getLeaves(), "asylum");
        assertThatDifferencesContainName(root.getLeaves(), "depot.lvl");
        assertThatDifferencesContainName(root.getLeaves(), "docks.lvl");
    }
}
