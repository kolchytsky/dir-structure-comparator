package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import com.coldenergia.dirstructurecomparator.filetree.diff.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents tree nodes denoting directories with the same files names,
 * but in different file trees. As there is a file tree for each side,
 * these tree nodes are organized by side.<br>
 * User: coldenergia
 * Date: 10/23/14
 * Time: 6:37 AM
 */
public class CorrespondingTreeNodes {

    private Map<Side, FileNode> parallelTreeNodes = new HashMap<>();

    public CorrespondingTreeNodes() {}

    public CorrespondingTreeNodes(Map<Side, FileNode> parallelTreeNodes) {
        this.parallelTreeNodes = parallelTreeNodes;
    }

    public FileNode getNodeBySide(Side side) {
        return this.parallelTreeNodes.get(side);
    }

    public void putNodeForSide(Side side, FileNode node) {
        this.parallelTreeNodes.put(side, node);
    }

    public Map<Side, FileNode> getParallelTreeNodes() {
        return parallelTreeNodes;
    }

    public void setParallelTreeNodes(Map<Side, FileNode> parallelTreeNodes) {
        this.parallelTreeNodes = parallelTreeNodes;
    }
}
