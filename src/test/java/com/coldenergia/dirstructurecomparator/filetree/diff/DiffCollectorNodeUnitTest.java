package com.coldenergia.dirstructurecomparator.filetree.diff;

import com.coldenergia.dirstructurecomparator.builder.DetachedFileBuilder;
import com.coldenergia.dirstructurecomparator.builder.DiffCollectorNodeBuilder;
import junit.extensions.TestDecorator;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 2:23 PM
 */
public class DiffCollectorNodeUnitTest {

    @Test
    public void shouldNeverReturnNullLeafList() {
        DiffCollectorNode node = new DiffCollectorNode();
        assertNotNull(node);
        assertThat(node.getLeaves(), is(empty()));
    }

    @Test
    public void shouldAddLeaf() {
        Set<DiffCollectorNode> leaves = new HashSet<>(4);
        for (int i = 0; i < 3; i++) {
            leaves.add(new DiffCollectorNode());
        }
        DiffCollectorNode node = new DiffCollectorNode(null, null, leaves);
        int initialLeafCount = node.getLeaves().size();

        node.addLeaf(new DiffCollectorNode());

        int finalLeafCount = node.getLeaves().size();
        assertEquals(1, finalLeafCount - initialLeafCount);
    }

    @Test
    public void shouldAddLeaves() {
        // TODO: Need one more test for Set<> uniqueness logic once equals is overridden for DiffCollectorNode
        final int INITIAL_SIZE = 4;
        Set<DiffCollectorNode> uniqueLeaves = new HashSet<>();
        for (int i = 0; i < INITIAL_SIZE; i++) {
            DetachedFile detachedFile = new DetachedFileBuilder().withFileName("a" + i).build();
            uniqueLeaves.add(new DiffCollectorNodeBuilder().withLeftFile(detachedFile).build());
        }
        DiffCollectorNode node = new DiffCollectorNode(null, null, uniqueLeaves);
        int initialLeafCount = node.getLeaves().size();

        final int ADDITIONAL_LEAVES_SIZE = 6;
        Set<DiffCollectorNode> moreUniqueLeaves = new HashSet<>(ADDITIONAL_LEAVES_SIZE);
        for (int i = INITIAL_SIZE; i < ADDITIONAL_LEAVES_SIZE + INITIAL_SIZE; i++) {
            moreUniqueLeaves.add(new DiffCollectorNode(new DetachedFile("a" + i), null));
        }

        node.addLeaves(moreUniqueLeaves);
        int finalLeafCount = node.getLeaves().size();
        assertEquals(ADDITIONAL_LEAVES_SIZE, finalLeafCount - initialLeafCount);
    }

    @Test
    public void shouldIdentifyCommonNode() {
        DiffCollectorNode node = new DiffCollectorNodeBuilder()
                .withLeftFile(new DetachedFileBuilder().withFileName("dir1").withIsDirectory(true).build())
                .withRightFile(new DetachedFileBuilder().withFileName("dir1").withIsDirectory(true).build())
                .build();
        assertTrue("Must have identified the node " + node + " as common", node.isCommonNode());

        node = new DiffCollectorNodeBuilder()
                .withLeftFile(new DetachedFileBuilder().withFileName("file1").withIsDirectory(false).build())
                .withRightFile(new DetachedFileBuilder().withFileName("file1").withIsDirectory(false).build())
                .build();
        assertTrue("Must have identified the node " + node + " as common", node.isCommonNode());

        node = new DiffCollectorNodeBuilder()
                .withLeftFile(null)
                .withRightFile(null)
                .build();
        assertTrue("Must have identified the node " + node + " as common", node.isCommonNode());
    }

    @Test
    public void shouldNotIdentifyCommonNode() {
        DiffCollectorNode node = new DiffCollectorNodeBuilder()
                .withLeftFile(new DetachedFileBuilder().withFileName("smth").withIsDirectory(true).build())
                .withRightFile(new DetachedFileBuilder().withFileName("smth").withIsDirectory(false).build())
                .build();
        assertFalse("Shouldn't have identified the node " + node + " as common", node.isCommonNode());

        node = new DiffCollectorNodeBuilder()
                .withLeftFile(new DetachedFileBuilder().build())
                .withRightFile(null)
                .build();
        assertFalse(node.isCommonNode());

        node = new DiffCollectorNodeBuilder()
                .withLeftFile(null)
                .withRightFile(new DetachedFileBuilder().build())
                .build();
        assertFalse(node.isCommonNode());
    }
}
