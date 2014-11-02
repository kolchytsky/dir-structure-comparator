package com.coldenergia.dirstructurecomparator.filetree.diff;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 2:07 PM
 */
public class DiffCollectorNode {

    private Map<Side, DetachedFile> filesMap = new HashMap<>();

    private Set<DiffCollectorNode> leaves = new HashSet<>();

    public DiffCollectorNode() {}

    public DiffCollectorNode(DetachedFile leftFile, DetachedFile rightFile) {
        setFile(leftFile, Side.LEFT);
        setFile(rightFile, Side.RIGHT);
    }

    public DiffCollectorNode(DetachedFile leftFile, DetachedFile rightFile, Set<DiffCollectorNode> leaves) {
        setFile(leftFile, Side.LEFT);
        setFile(rightFile, Side.RIGHT);
        this.leaves = leaves == null ? new HashSet<DiffCollectorNode>() : leaves;
    }

    public DetachedFile getLeftFile() {
        return getFileBySide(Side.LEFT);
    }

    public void setLeftFile(DetachedFile leftFile) {
        setFile(leftFile, Side.LEFT);
    }

    public DetachedFile getRightFile() {
        return getFileBySide(Side.RIGHT);
    }

    public void setRightFile(DetachedFile rightFile) {
        setFile(rightFile, Side.RIGHT);
    }

    public Map<Side, DetachedFile> getFilesMap() {
        return filesMap;
    }

    public void setFilesMap(Map<Side, DetachedFile> filesMap) {
        this.filesMap = filesMap;
    }

    public void setFile(DetachedFile detachedFile, Side side) {
        this.filesMap.put(side, detachedFile);
    }

    public DetachedFile getFileBySide(Side side) {
        return this.filesMap.get(side);
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

    /**
     * Determines if this node represents a file / directory, which is
     * the same at both sides (the contents of the directory may differ though!).
     * */
    public boolean isCommonNode() {
        if (getLeftFile() == null && getRightFile() == null) {
            return true;
        } else if (getLeftFile() == null) {
            return false;
        } else {
            return getLeftFile().equals(getRightFile());
        }
    }

    // TODO: Consider defining unique and looking at DiffCollectorNodeUnitTest
}
