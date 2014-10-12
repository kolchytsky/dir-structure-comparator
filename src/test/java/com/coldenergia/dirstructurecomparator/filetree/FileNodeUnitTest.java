package com.coldenergia.dirstructurecomparator.filetree;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 8:37 PM
 */
public class FileNodeUnitTest {

    @Test
    public void shouldNeverReturnNullLeavesList() {
        FileNode node = new FileNode(mock(Path.class));
        assertNotNull(node.getLeaves());
        assertThat(node.getLeaves(), is(empty()));
    }

    @Test
    public void shouldAddLeaf() {
        FileNode node = new FileNode(mock(Path.class));
        FileNode leafNode = new FileNode(mock(Path.class));
        node.addLeaf(leafNode);
        assertNotNull(node.getLeaves());
        assertThat(node.getLeaves(), containsInAnyOrder(leafNode));
    }
}
