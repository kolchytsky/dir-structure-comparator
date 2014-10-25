package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.filetree.FileNode;
import com.coldenergia.dirstructurecomparator.filetree.diff.DetachedFile;
import com.coldenergia.dirstructurecomparator.filetree.diff.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: coldenergia
 * Date: 10/23/14
 * Time: 7:07 AM
 */
public class DetachedLeafFiles {

    private Map<Side, Map<DetachedFile, FileNode>> detachedFilesBySide = new HashMap<>();

    public CorrespondingTreeNodes getCorrespondingTreeNodesForDetachedFile(DetachedFile detachedFile) {
        CorrespondingTreeNodes correspondingTreeNodes = new CorrespondingTreeNodes();

        for (Side side : Side.values()) {
            FileNode node = this.detachedFilesBySide.get(side).get(detachedFile);
            if (node != null) {
                correspondingTreeNodes.putNodeForSide(side, node);
            }
        }
        return correspondingTreeNodes;
    }

    public Set<DetachedFile> getDetachedFilesForSide(Side side) {
        return getDetachedFileNodeMapForSide(side).keySet();
    }

    public void putDetachedFile(Side side, DetachedFile detachedFile, FileNode originalNodeForFile) {
        Map<DetachedFile, FileNode> mapForSide = getDetachedFileNodeMapForSide(side);
        mapForSide.put(detachedFile, originalNodeForFile);
    }

    private Map<DetachedFile, FileNode> getDetachedFileNodeMapForSide(Side side) {
        Map<DetachedFile, FileNode> map = detachedFilesBySide.get(side);
        if (map == null) {
            map = new HashMap<>();
            detachedFilesBySide.put(side, map);
        }
        return map;
    }
}
