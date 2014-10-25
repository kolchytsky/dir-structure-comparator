package com.coldenergia.dirstructurecomparator.builder;

import com.coldenergia.dirstructurecomparator.filetree.diff.Side;
import com.coldenergia.dirstructurecomparator.comparator.LevelFragment;
import com.coldenergia.dirstructurecomparator.filetree.diff.DiffCollectorNode;

import java.nio.file.Path;

/**
 * User: coldenergia
 * Date: 10/22/14
 * Time: 9:25 PM
 */
public class LevelFragmentBuilder {

    private LevelFragment levelFragment;

    public LevelFragmentBuilder() {
        this.levelFragment = new LevelFragment();
    }

    public LevelFragment build() {
        return levelFragment;
    }

    public LevelFragmentBuilder withLevel(int level) {
        this.levelFragment.setLevel(level);
        return this;
    }

    public LevelFragmentBuilder withPath(Side side, Path path) {
        this.levelFragment.putPath(side, path);
        return this;
    }

    public LevelFragmentBuilder withCollectorNode(DiffCollectorNode collectorNode) {
        this.levelFragment.setCollectorNode(collectorNode);
        return this;
    }
}
