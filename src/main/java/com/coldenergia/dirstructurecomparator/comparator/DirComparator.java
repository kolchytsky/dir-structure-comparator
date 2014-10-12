package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.filetree.FileTree;
import com.coldenergia.dirstructurecomparator.filetree.builder.FileTreeBuilder;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;

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

        return null;
    }

    //private void traverseOneLevelDown()
}
