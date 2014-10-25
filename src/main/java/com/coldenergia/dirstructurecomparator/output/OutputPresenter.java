package com.coldenergia.dirstructurecomparator.output;

import com.coldenergia.dirstructurecomparator.filetree.diff.DetachedFile;
import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;
import com.coldenergia.dirstructurecomparator.filetree.diff.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: coldenergia
 * Date: 10/25/14
 * Time: 10:12 PM
 */
public class OutputPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputPresenter.class);

    private DifferenceCollector differenceCollector = null;

    private Map<Side, DetachedFile> rootDirectories = new HashMap<>(2);

    private Side suppressOutputForSide = null;

    public OutputPresenter(DifferenceCollector differenceCollector) {
        this.differenceCollector = differenceCollector;
        DiffCollectorNode root = this.differenceCollector.getRoot();
        this.rootDirectories = root.getFilesMap();
    }

    public OutputPresenter(DifferenceCollector differenceCollector, Side suppressOutputForSide) {
        this(differenceCollector);
        this.suppressOutputForSide = suppressOutputForSide;
    }

    public void outputDifferences() {
        DiffCollectorNode node = this.differenceCollector.getRoot();
        for (DiffCollectorNode leaf : node.getLeaves()) {
            outputNode(leaf);
        }
    }

    public void outputNode(DiffCollectorNode node) {
        Map<Side, DetachedFile> differences = node.getFilesMap();
        DetachedFile leftFile = differences.get(Side.LEFT);
        DetachedFile rightFile = differences.get(Side.RIGHT);

        if (leftFile != null && rightFile != null) {
            LOGGER.info("Common files: " + leftFile.getFileName() + ", but there may be differences inside.");
        } else if (leftFile != null && suppressOutputForSide != Side.LEFT) {
            LOGGER.info("File " + leftFile.getFileName() + " is present in " + rootDirectories.get(Side.LEFT).getFileName());
        } else if (rightFile != null && suppressOutputForSide != Side.RIGHT) {
            LOGGER.info("File " + rightFile.getFileName() + " is present in " + rootDirectories.get(Side.RIGHT).getFileName());
        }

        for (DiffCollectorNode leaf : node.getLeaves()) {
            outputNode(leaf);
        }
    }
}
