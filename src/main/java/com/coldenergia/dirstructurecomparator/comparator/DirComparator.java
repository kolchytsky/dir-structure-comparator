package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import com.coldenergia.dirstructurecomparator.filetree.FileTree;
import com.coldenergia.dirstructurecomparator.filetree.builder.FileTreeBuilder;
import com.coldenergia.dirstructurecomparator.filetree.diff.DetachedFile;
import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: Refactor this using Side...Move this enum to src/main/java.
 * User: coldenergia
 * Date: 10/12/14
 * Time: 1:18 PM
 */
public class DirComparator {

    public DifferenceCollector compareDirectories(String leftDirectory, String rightDirectory) {
        FileTreeBuilder leftBuilder = new FileTreeBuilder(leftDirectory, null);
        FileTree leftTree = leftBuilder.buildFileTree();
        FileTreeBuilder rightBuilder = new FileTreeBuilder(rightDirectory, null);
        FileTree rightTree = rightBuilder.buildFileTree();

        DiffCollectorNode root = new DiffCollectorNode();
        root.setLeftFile(new DetachedFile(leftTree.getRoot().getPath().toAbsolutePath().normalize().toString(), true));
        root.setRightFile(new DetachedFile(rightTree.getRoot().getPath().toAbsolutePath().normalize().toString(), true));
        DifferenceCollector collector = new DifferenceCollector(root);

        traverseOneLevelDown(leftTree.getRoot(), rightTree.getRoot(), root);

        return collector;
    }

    private Set<DiffCollectorNode> traverseOneLevelDown(FileNode leftTreeNode, FileNode rightTreeNode, DiffCollectorNode diffNode) {
        Set<DiffCollectorNode> differences = new HashSet<>();

        Map<DetachedFile, FileNode> leftMap = detachFiles(leftTreeNode.getLeaves());
        Map<DetachedFile, FileNode> rightMap = detachFiles(rightTreeNode.getLeaves());

        Set<DetachedFile> immediateLeftFiles = leftMap.keySet();
        Set<DetachedFile> immediateRightFiles = rightMap.keySet();

        Set<DetachedFile> onlyInLeft = computeDifference(immediateLeftFiles, immediateRightFiles);
        for (DetachedFile detachedFile : onlyInLeft) {
            DiffCollectorNode newDiffNode = new DiffCollectorNode(detachedFile, null);
            diffNode.addLeaf(newDiffNode);
            differences.add(newDiffNode);
        }

        Set<DetachedFile> onlyInRight = computeDifference(immediateRightFiles, immediateLeftFiles);
        for (DetachedFile detachedFile : onlyInRight) {
            DiffCollectorNode newDiffNode = new DiffCollectorNode(null, detachedFile);
            diffNode.addLeaf(newDiffNode);
            differences.add(newDiffNode);
        }

        Set<DetachedFile> intersection = computeIntersection(immediateLeftFiles, immediateRightFiles);
        for (DetachedFile detachedFile : intersection) {
            DiffCollectorNode newDiffNodeCandidate = new DiffCollectorNode(detachedFile, detachedFile);
            Set<DiffCollectorNode> nestedDifferences = traverseOneLevelDown(leftMap.get(detachedFile), rightMap.get(detachedFile), newDiffNodeCandidate);
            if (!nestedDifferences.isEmpty()) {
                // candidate node must be added
                diffNode.addLeaf(newDiffNodeCandidate);
            }
        }
        return differences;
    }

    private static Map<DetachedFile, FileNode> detachFiles(Set<FileNode> fileNodes) {
        Map<DetachedFile, FileNode> map = new HashMap<>();
        for (FileNode fileNode : fileNodes) {
            String fileName = fileNode.getPath().getFileName().toString();
            // TODO: Think about the symbolic links here.
            boolean isDirectory = Files.isDirectory(fileNode.getPath());
            DetachedFile detachedFile = new DetachedFile(fileName, isDirectory);
            map.put(detachedFile, fileNode);
        }
        return map;
    }

    private static Set<DetachedFile> computeIntersection(Set<DetachedFile> first, Set<DetachedFile> second) {
        boolean isFirstLarger = first.size() > second.size() ? true : false;
        Set<DetachedFile> largerSet, smallerSet = null;
        if (isFirstLarger) {
            largerSet = first;
            smallerSet = new HashSet<>(second); // clone
        } else {
            largerSet = second;
            smallerSet = new HashSet<>(first); // clone
        }
        smallerSet.retainAll(largerSet);
        return smallerSet;
    }

    /**
     * Computes an asymmetric difference.
     * */
    private static Set<DetachedFile> computeDifference(Set<DetachedFile> minuend, Set<DetachedFile> subtrahend) {
        Set<DetachedFile> minuendClone = new HashSet<>(minuend);
        minuendClone.removeAll(subtrahend);
        return minuendClone;
    }
}
