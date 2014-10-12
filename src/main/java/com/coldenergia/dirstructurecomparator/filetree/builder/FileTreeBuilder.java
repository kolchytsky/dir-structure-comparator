package com.coldenergia.dirstructurecomparator.filetree.builder;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import com.coldenergia.dirstructurecomparator.filetree.FileTree;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 4:33 PM
 */
public class FileTreeBuilder {

    private String rootDirFullPath = null;

    private Integer maxHeight = null;

    public FileTreeBuilder(String rootDirFullPath) {
        this.rootDirFullPath = rootDirFullPath;
    }

    /**
     * @throws DirDoesNotExistException If the root directory path is null, or the
     * {@link FileTreeBuilder#rootDirFullPath} does not represent a valid path, or
     * there's no such directory.
     * */
    public FileTree buildFileTree() {
        Path rootDirPath = getRootDirPath();
        FileNode rootNode = new FileNode(rootDirPath);
        FileTree fileTree = new FileTree(rootNode);
        try {
            fillFileTree(fileTree.getRoot(), null);
        } catch (IOException e) {
            throw new FileTreeBuildException(e);
        }
        return fileTree;
    }

    private void fillFileTree(FileNode startNode, Integer heightOfStartLevel) throws IOException {
        /*boolean exceedsHeight = (this.maxHeight != null) && ((heightOfStartLevel + 1) > this.maxHeight);
        if (this.maxHeight != null) {
        }*/

        Path nodePath = startNode.getPath();
        DirectoryStream<Path> stream = Files.newDirectoryStream(nodePath);
        for (Path path : stream) {
            FileNode node = new FileNode(path);
            startNode.addLeaf(node);
            if (path.toFile().isDirectory()) {
                fillFileTree(node, null);
            }
        }
    }

    /**
     * @throws DirDoesNotExistException
     * */
    private Path getRootDirPath() {
        if (StringUtils.isEmpty(this.rootDirFullPath)) {
            throw new DirDoesNotExistException("The root directory path is null.");
        }
        Path rootDirPath = null;
        try {
            rootDirPath = Paths.get(this.rootDirFullPath);
        } catch (InvalidPathException e) {
            throw new DirDoesNotExistException("Path " + this.rootDirFullPath + " doesn't exist", e);
        }
        if (rootDirPath == null || Files.notExists(rootDirPath)) {
            throw new DirDoesNotExistException("There's no such directory for path " + this.rootDirFullPath);
        }
        return rootDirPath;
    }

}
