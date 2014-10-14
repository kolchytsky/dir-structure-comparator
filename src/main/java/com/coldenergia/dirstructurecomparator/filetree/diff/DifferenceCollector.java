package com.coldenergia.dirstructurecomparator.filetree.diff;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 2:06 PM
 */
public class DifferenceCollector {

    private DiffCollectorNode root;

    public DifferenceCollector(DiffCollectorNode root) {
        this.root = root;
    }

    public DiffCollectorNode getRoot() {
        return root;
    }

    public void setRoot(DiffCollectorNode root) {
        this.root = root;
    }
}
