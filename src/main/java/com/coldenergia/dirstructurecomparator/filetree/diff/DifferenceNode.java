package com.coldenergia.dirstructurecomparator.filetree.diff;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 2:07 PM
 */
public class DifferenceNode {

    private Path leftPath;

    private Path rightPath;

    private Set<DifferenceNode> leaves = new HashSet<>();

    public DifferenceNode() {}

    public DifferenceNode(Path leftPath, Path rightPath, Set<DifferenceNode> leaves) {
        this.leftPath = leftPath;
        this.rightPath = rightPath;
        this.leaves = leaves == null ? new HashSet<DifferenceNode>() : leaves;
    }

    public Path getLeftPath() {
        return leftPath;
    }

    public void setLeftPath(Path leftPath) {
        this.leftPath = leftPath;
    }

    public Path getRightPath() {
        return rightPath;
    }

    public void setRightPath(Path rightPath) {
        this.rightPath = rightPath;
    }

    public void addLeaf(DifferenceNode node) {
        this.leaves.add(node);
    }

    public Set<DifferenceNode> getLeaves() {
        return leaves;
    }

    public void setLeaves(Set<DifferenceNode> leaves) {
        this.leaves = leaves;
    }
}
