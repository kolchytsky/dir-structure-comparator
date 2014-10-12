package com.coldenergia.dirstructurecomparator;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 3:15 PM
 */
public class FileNode {

    private Set<FileNode> leaves = new HashSet<>();

    private Path path;

    public FileNode(Path path) {
        this.path = path;
    }

    /**
     * @return {@link FileNode} which has the same {@link Path},
     * or {@code null} if no such node exists. Please note that paths are compared using
     * {@link Path#equals(Object)}, and not on the basis of whether they point at the same file.
     * */
    public FileNode findLeafByPath(Path path) {
        FileNode target = null;
        for (FileNode leaf : this.leaves) {
            Path normalizedPath = path.toAbsolutePath().normalize();
            Path normalizedLeafPath = leaf.getPath().toAbsolutePath().normalize();
            if (normalizedPath.equals(normalizedLeafPath)) {
                target = leaf;
                break;
            }
        }
        return target;
    }

    public void addLeaf(FileNode fileNode) {
        this.leaves.add(fileNode);
    }

    public Set<FileNode> getLeaves() {
        return leaves;
    }

    public void setLeaves(Set<FileNode> leaves) {
        this.leaves = leaves;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileNode{");
        sb.append("leaves=").append(leaves);
        sb.append(", path=").append(path);
        sb.append('}');
        return sb.toString();
    }

}
