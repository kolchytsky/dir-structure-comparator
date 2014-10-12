package com.coldenergia.dirstructurecomparator.filetree.builder;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static com.coldenergia.dirstructurecomparator.FileScaffolding.createTmpDir;
import static com.coldenergia.dirstructurecomparator.FileScaffolding.deleteTmpDir;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 10:07 PM
 */
public class AbstractFileTreeBuilderIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileTreeBuilderIntegrationTest.class);

    private Path tmpRootDirPath = null;

    @Before
    public void setup() throws IOException {
        tmpRootDirPath = Paths.get("./file_tree");
        createTmpDir(tmpRootDirPath);
    }

    @After
    public void teardown() throws IOException {
        deleteTmpDir(tmpRootDirPath);
    }

    public static void assertThatNodesContainPath(Set<FileNode> nodes, Path path) {
        assertNotNull(nodes);
        assertNotNull(path);
        assertThat("The node set is empty. It cannot contain the path " + path, nodes, is(not(empty())));

        boolean foundPath = false;
        for (FileNode node : nodes) {
            try {
                foundPath = Files.isSameFile(node.getPath(), path);
            } catch (IOException e) {
                fail("An exception has occurred " + String.valueOf(e.getMessage()));
            }
            if (foundPath) {
                break;
            }
        }
        assertTrue("Path " + path + " not found", foundPath);
    }

    public Path getTmpRootDirPath() {
        return tmpRootDirPath;
    }
}
