package com.coldenergia.dirstructurecomparator.filetree.builder;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

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
        createTmpDir();
    }

    @After
    public void teardown() throws IOException {
        deleteTmpDir();
    }

    public static void assertThatNodesContainPath(Set<FileNode> nodes, Path path) {
        assertNotNull(nodes);
        assertThat(nodes, is(not(empty())));
        assertNotNull(path);

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
        assertTrue(foundPath);
    }

    public Path getTmpRootDirPath() {
        return tmpRootDirPath;
    }

    public Path createFile(Path parentDirPath, String fileName) throws IOException {
        Path file = parentDirPath.resolve(fileName);
        Files.createFile(file);
        LOGGER.info("Created " + file.toAbsolutePath().normalize());
        return file;
    }

    public Path createDirectory(Path parentDirPath, String dirName) throws IOException {
        Path dir = parentDirPath.resolve(dirName);
        Files.createDirectory(dir);
        LOGGER.info("Created " + dir.toAbsolutePath().normalize());
        return dir;
    }

    private void createTmpDir() throws IOException {
        tmpRootDirPath = Paths.get("./file_tree");
        Files.createDirectory(tmpRootDirPath);
        LOGGER.info("Created a temp dir: " + tmpRootDirPath.toAbsolutePath().normalize());
    }

    private void deleteTmpDir() throws IOException {
        Files.walkFileTree(tmpRootDirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
        LOGGER.info("Deleted the temp dir: " + tmpRootDirPath.toAbsolutePath().normalize());
    }
}
