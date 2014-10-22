package com.coldenergia.dirstructurecomparator.builder;

import com.coldenergia.dirstructurecomparator.Side;
import com.coldenergia.dirstructurecomparator.filetree.diff.DetachedFile;
import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;

import java.util.Set;

/**
 * User: coldenergia
 * Date: 10/15/14
 * Time: 9:35 PM
 */
public class DiffCollectorNodeBuilder {

    private DiffCollectorNode diffCollectorNode;

    public DiffCollectorNodeBuilder() {
        diffCollectorNode = new DiffCollectorNode();
        diffCollectorNode.setLeftFile(new DetachedFileBuilder().build());
    }

    public DiffCollectorNode build() {
        return diffCollectorNode;
    }

    public DiffCollectorNodeBuilder withLeftFile(DetachedFile leftFile) {
        diffCollectorNode.setLeftFile(leftFile);
        return this;
    }

    public DiffCollectorNodeBuilder withRightFile(DetachedFile rightFile) {
        diffCollectorNode.setRightFile(rightFile);
        return this;
    }

    public DiffCollectorNodeBuilder withFile(DetachedFile file, Side side) {
        if (side == Side.LEFT) {
            return withLeftFile(file);
        } else {
            return withRightFile(file);
        }
    }

    public DiffCollectorNodeBuilder withLeaves(Set<DiffCollectorNode> leaves) {
        diffCollectorNode.setLeaves(leaves);
        return this;
    }
}
