package com.coldenergia.dirstructurecomparator.filetree.diff;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 2:07 PM
 */
public class DiffCollectorNode {

    private DetachedFile leftFile;

    private DetachedFile rightFile;

    private Set<DiffCollectorNode> leaves = new HashSet<>();

    public DiffCollectorNode() {}

    public DiffCollectorNode(DetachedFile leftFile, DetachedFile rightFile) {
        this.leftFile = leftFile;
        this.rightFile = rightFile;
    }

    public DiffCollectorNode(DetachedFile leftFile, DetachedFile rightFile, Set<DiffCollectorNode> leaves) {
        this.leftFile = leftFile;
        this.rightFile = rightFile;
        this.leaves = leaves == null ? new HashSet<DiffCollectorNode>() : leaves;
    }

    public DetachedFile getLeftFile() {
        return leftFile;
    }

    public void setLeftFile(DetachedFile leftFile) {
        this.leftFile = leftFile;
    }

    public DetachedFile getRightFile() {
        return rightFile;
    }

    public void setRightFile(DetachedFile rightFile) {
        this.rightFile = rightFile;
    }

    public void addLeaf(DiffCollectorNode node) {
        this.leaves.add(node);
    }

    public void addLeaves(Collection<DiffCollectorNode> newLeaves) {
        this.leaves.addAll(newLeaves);
    }

    public Set<DiffCollectorNode> getLeaves() {
        return leaves;
    }

    public void setLeaves(Set<DiffCollectorNode> leaves) {
        this.leaves = leaves;
    }

    // TODO: Consider defining unique and looking at DiffCollectorNodeUnitTest
}
