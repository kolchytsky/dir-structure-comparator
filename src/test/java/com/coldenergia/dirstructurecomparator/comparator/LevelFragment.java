package com.coldenergia.dirstructurecomparator.comparator;

import com.coldenergia.dirstructurecomparator.filetree.diff.Side;
import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * User: coldenergia
 * Date: 10/22/14
 * Time: 9:25 PM
 */
public class LevelFragment {

    private int level;

    private Map<Side, Path> pathsMap = new HashMap<>();

    private DiffCollectorNode collectorNode;

    public LevelFragment() {}

    public LevelFragment(DiffCollectorNode collectorNode) {
        this.collectorNode = collectorNode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Map<Side, Path> getPathsMap() {
        return pathsMap;
    }

    public void setPathsMap(Map<Side, Path> pathsMap) {
        this.pathsMap = pathsMap;
    }

    public void putPath(Side side, Path path) {
        this.pathsMap.put(side, path);
    }

    public Path getPathBySide(Side side) {
        return this.pathsMap.get(side);
    }

    public DiffCollectorNode getCollectorNode() {
        return collectorNode;
    }

    public void setCollectorNode(DiffCollectorNode collectorNode) {
        this.collectorNode = collectorNode;
    }

}
