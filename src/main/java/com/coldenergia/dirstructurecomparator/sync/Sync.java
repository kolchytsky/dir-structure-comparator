package com.coldenergia.dirstructurecomparator.sync;

import com.coldenergia.dirstructurecomparator.filetree.diff.DetachedFile;
import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;
import com.coldenergia.dirstructurecomparator.filetree.diff.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: coldenergia
 * Date: 11/2/14
 * Time: 3:09 PM
 */
public class Sync {

    private final static Logger LOGGER = LoggerFactory.getLogger(Sync.class);

    private DifferenceCollector diffCollector;

    public Sync(DifferenceCollector diffCollector) {
        this.diffCollector = diffCollector;
    }

    public void sync(Side referenceSide, SyncOption... syncOptions) {
        for (SyncOption syncOption : syncOptions) {
            if (syncOption == SyncOption.COPY) {
                copy(referenceSide);
            }
        }
    }

    private void copy(Side referenceSide) {
        DiffCollectorNode root = diffCollector.getRoot();
        boolean topFilesNotDirs = !(root.getRightFile().isDirectory() && root.getLeftFile().isDirectory());
        if (topFilesNotDirs) {
            throw new RuntimeException(root.getRightFile().getFileName() + ", " + root.getLeftFile().getFileName() +
                    " must be directories!");
        }

        Map<Side, DetachedFile> topDirs = root.getFilesMap();
        Map<Side, Path> topDirPaths = new HashMap<>();
        for (Side side : topDirs.keySet()) {
            topDirPaths.put(side, Paths.get(topDirs.get(side).getFileName()));
        }

        Path fromPath = topDirPaths.get(referenceSide);
        Path toPath = topDirPaths.get(referenceSide == Side.LEFT ? Side.RIGHT : Side.LEFT);

        copyLevel(fromPath, toPath, root, referenceSide);
    }

    private void copyLevel(Path fromPath, Path toPath, DiffCollectorNode node, Side referenceSide) {
        Set<DiffCollectorNode> leaves = node.getLeaves();
        for (DiffCollectorNode leaf : leaves) {
            DetachedFile referenceFile = leaf.getFileBySide(referenceSide);

            if (referenceFile == null) {
                continue;
            }

            String referenceFileName = referenceFile.getFileName();
            if (!leaf.isCommonNode()) {
                copyFile(fromPath, toPath, referenceFileName);
            }

            if (referenceFile.isDirectory()) {
                Path newFromPath = fromPath.resolve(referenceFileName);
                Path newToPath = toPath.resolve(referenceFileName);
                copyLevel(newFromPath, newToPath, leaf, referenceSide);
            }
        }
    }

    private void copyFile(Path fromPath, Path toPath, String referenceFileName) {
        Path source = fromPath.resolve(referenceFileName);
        Path target = toPath.resolve(referenceFileName);
        try {
            Files.copy(source, target);
            LOGGER.info("Copied " + source + " to " + target);
        } catch (IOException e) {
            LOGGER.error("Error while copying file " + referenceFileName + " from " + fromPath + " to " + toPath, e);
        }
    }
}
