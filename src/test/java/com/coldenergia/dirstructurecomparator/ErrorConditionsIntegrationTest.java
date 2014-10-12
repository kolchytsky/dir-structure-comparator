package com.coldenergia.dirstructurecomparator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 9:11 PM
 */
public class ErrorConditionsIntegrationTest extends AbstractFileTreeBuilderIntegrationTest {

    @Test
    public void shouldThrowExceptionIfRootDirPathIsNull() {
        FileTreeBuilder builder = new FileTreeBuilder(null);
        try {
            builder.buildFileTree();
            fail("Should've thrown an exception here");
        } catch (DirDoesNotExistException expected) {
            assertNotNull(expected);
            assertEquals("The root directory path is null.", expected.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionForNonExistentRootDirPath() {
        String nonExistentPath = "/this-directory-must-not-exist/definitely";
        FileTreeBuilder builder = new FileTreeBuilder(nonExistentPath);
        try {
            builder.buildFileTree();
            fail("Should've thrown an exception here");
        } catch (DirDoesNotExistException expected) {
            assertNotNull(expected);
            assertEquals("There's no such directory for path " + nonExistentPath, expected.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionForInvalidRootDirPath() {
        String invalidPath = "\0$\\:/!(#$JJ$#:///\\442!\\/**-%#?@\n^";
        FileTreeBuilder builder = new FileTreeBuilder(invalidPath);
        try {
            builder.buildFileTree();
            fail("Should've thrown an exception here");
        } catch (DirDoesNotExistException expected) {
            assertNotNull(expected);
            assertEquals("Path " + invalidPath + " doesn't exist", expected.getMessage());
        }
    }
}
