package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import com.coldenergia.dirstructurecomparator.filetree.FileTree;
import com.coldenergia.dirstructurecomparator.filetree.builder.FileTreeBuilder;
import com.coldenergia.dirstructurecomparator.filetree.diff.DetachedFile;
import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;
import com.coldenergia.dirstructurecomparator.filetree.diff.Side;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
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

        DifferenceCollector collector = constructDifferenceCollector(leftTree, rightTree);

        CorrespondingTreeNodes correspondingTreeNodes = new CorrespondingTreeNodes();
        correspondingTreeNodes.putNodeForSide(Side.LEFT, leftTree.getRoot());
        correspondingTreeNodes.putNodeForSide(Side.RIGHT, rightTree.getRoot());

        traverseOneLevelDown(correspondingTreeNodes, collector.getRoot());

        return collector;
    }

    private DifferenceCollector constructDifferenceCollector(FileTree leftTree, FileTree rightTree) {
        DiffCollectorNode root = new DiffCollectorNode();
        // TODO: Rework this so that the parent dir information is stored separately in the diff collector. How ?
        root.setFile(new DetachedFile(leftTree.getRoot().getPath().toAbsolutePath().normalize().toString(), true), Side.LEFT);
        root.setFile(new DetachedFile(rightTree.getRoot().getPath().toAbsolutePath().normalize().toString(), true), Side.RIGHT);

        DifferenceCollector collector = new DifferenceCollector(root);
        return collector;
    }

    private Set<DiffCollectorNode> traverseOneLevelDown(
            CorrespondingTreeNodes currentTreeNodesBySide,
            DiffCollectorNode diffNode) {
        Set<DiffCollectorNode> differences = new HashSet<>();

        DetachedLeafFiles detachedLeafFiles = detachLeafFiles(currentTreeNodesBySide);
        Set<DetachedFile> immediateLeftFiles = detachedLeafFiles.getDetachedFilesForSide(Side.LEFT);
        Set<DetachedFile> immediateRightFiles = detachedLeafFiles.getDetachedFilesForSide(Side.RIGHT);

        Map<Side, Set<DetachedFile>> differenceBetweenSides = computeDifferenceBetweenSides(
                immediateLeftFiles, immediateRightFiles
        );
        for (Side side : Side.values()) {
            for (DetachedFile detachedFile : differenceBetweenSides.get(side)) {
                DiffCollectorNode newDiffNode = new DiffCollectorNode();
                newDiffNode.setFile(detachedFile, side);
                diffNode.addLeaf(newDiffNode);
                differences.add(newDiffNode);
            }
        }

        Set<DetachedFile> intersection = computeIntersection(immediateLeftFiles, immediateRightFiles);
        for (DetachedFile detachedFile : intersection) {
            DiffCollectorNode newDiffNodeCandidate = new DiffCollectorNode(detachedFile, detachedFile);

            CorrespondingTreeNodes nextCorrespondingNodes = detachedLeafFiles
                    .getCorrespondingTreeNodesForDetachedFile(detachedFile);
            Set<DiffCollectorNode> nestedDifferences = traverseOneLevelDown(
                    nextCorrespondingNodes, newDiffNodeCandidate);

            if (!nestedDifferences.isEmpty()) {
                // candidate node must be added
                diffNode.addLeaf(newDiffNodeCandidate);
            }
        }
        return differences;
    }

    private static Map<Side, Set<DetachedFile>> computeDifferenceBetweenSides(
            Set<DetachedFile> immediateLeftFiles,
            Set<DetachedFile> immediateRightFiles) {
        Set<DetachedFile> onlyInLeft = computeDifference(immediateLeftFiles, immediateRightFiles);
        Set<DetachedFile> onlyInRight = computeDifference(immediateRightFiles, immediateLeftFiles);

        Map<Side, Set<DetachedFile>> differences = new HashMap<>(2);
        differences.put(Side.LEFT, onlyInLeft);
        differences.put(Side.RIGHT, onlyInRight);

        return differences;
    }

    private static DetachedLeafFiles detachLeafFiles(CorrespondingTreeNodes correspondingNodes) {
        DetachedLeafFiles detachedLeafFiles = new DetachedLeafFiles();

        for (Side side : Side.values()) {
            Set<FileNode> leaves = correspondingNodes.getNodeBySide(side).getLeaves();
            for (FileNode fileNode : leaves) {
                String fileName = fileNode.getPath().getFileName().toString();
                // TODO: Think about the symbolic links here.
                boolean isDirectory = Files.isDirectory(fileNode.getPath());
                DetachedFile detachedFile = new DetachedFile(fileName, isDirectory);

                detachedLeafFiles.putDetachedFile(side, detachedFile, fileNode);
            }
        }

        return detachedLeafFiles;
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
