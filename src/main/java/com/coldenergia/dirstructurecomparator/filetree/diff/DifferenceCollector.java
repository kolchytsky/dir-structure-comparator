package com.coldenergia.dirstructurecomparator.filetree.diff;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 2:06 PM
 */
public class DifferenceCollector {

    private DifferenceNode root;

    public DifferenceCollector(DifferenceNode root) {
        this.root = root;
    }

    public DifferenceNode getRoot() {
        return root;
    }

    public void setRoot(DifferenceNode root) {
        this.root = root;
    }
}
