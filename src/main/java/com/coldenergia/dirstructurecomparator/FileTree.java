package com.coldenergia.dirstructurecomparator;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 3:15 PM
 */
public class FileTree {

    private FileNode root;

    public FileTree(FileNode root) {
        this.root = root;
    }

    public FileNode getRoot() {
        return root;
    }

    public void setRoot(FileNode root) {
        this.root = root;
    }

}
