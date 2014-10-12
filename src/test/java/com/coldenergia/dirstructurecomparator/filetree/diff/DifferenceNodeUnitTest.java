package com.coldenergia.dirstructurecomparator.filetree.diff;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 2:23 PM
 */
public class DifferenceNodeUnitTest {

    @Test
    public void shouldNeverReturnNullLeafList() {
        DifferenceNode node = new DifferenceNode();
        assertNotNull(node);
        assertThat(node.getLeaves(), is(empty()));
    }

    @Test
    public void shouldAddLeaf() {
        Set<DifferenceNode> leaves = new HashSet<>(4);
        for (int i = 0; i < 3; i++) {
            leaves.add(new DifferenceNode());
        }
        DifferenceNode node = new DifferenceNode(null, null, leaves);
        int initialLeafCount = node.getLeaves().size();

        node.addLeaf(new DifferenceNode());

        int finalLeafCount = node.getLeaves().size();
        assertEquals(1, finalLeafCount - initialLeafCount);
    }
}
