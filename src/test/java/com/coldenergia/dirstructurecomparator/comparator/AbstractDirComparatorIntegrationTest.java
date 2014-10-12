package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceNode;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createTmpDir;
import static com.coldenergia.dirstructurecomparator.FileScaffolding.deleteTmpDir;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 4:01 PM
 */
public class AbstractDirComparatorIntegrationTest {

    private Path tmpRootDirPath = null;

    private Path leftDir = null;

    private Path rightDir = null;

    @Before
    public void setup() throws IOException {
        tmpRootDirPath = Paths.get("./file_tree");
        createTmpDir(tmpRootDirPath);
    }

    @After
    public void teardown() throws IOException {
        deleteTmpDir(tmpRootDirPath);
    }

    public void assertThatDifferencesContainName(Collection<DifferenceNode> nodes, String name) {
        assertNotNull(name);
        assertNotNull(nodes);
        assertThat(nodes, is(not(empty())));

        boolean foundName = false;
        for (DifferenceNode node : nodes) {
            Path leftPath = node.getLeftPath();
            Path rightPath = node.getRightPath();

            foundName = hasFileName(leftPath, name);
            if (foundName) {
                break;
            }

            foundName = hasFileName(rightPath, name);
            if (foundName) {
                break;
            }
        }
        assertTrue("There's no difference node containing " + name, foundName);
    }

    private boolean hasFileName(Path path, String fileName) {
        boolean pathValid = (path != null) && (path.getFileName() != null);
        if (pathValid && path.getFileName().toString().equals(fileName)) {
            return true;
        }
        return false;
    }

    public Path getTmpRootDirPath() {
        return tmpRootDirPath;
    }

    public Path getLeftDir() {
        return leftDir;
    }

    public void setLeftDir(Path leftDir) {
        this.leftDir = leftDir;
    }

    public Path getRightDir() {
        return rightDir;
    }

    public void setRightDir(Path rightDir) {
        this.rightDir = rightDir;
    }
}
