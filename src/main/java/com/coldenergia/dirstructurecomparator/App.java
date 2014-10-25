package com.coldenergia.dirstructurecomparator;

import com.coldenergia.dirstructurecomparator.comparator.DirComparator;
import com.coldenergia.dirstructurecomparator.filetree.diff.DifferenceCollector;
import com.coldenergia.dirstructurecomparator.filetree.diff.Side;
import com.coldenergia.dirstructurecomparator.output.OutputPresenter;

public class App {

    //private static final String LEFT_DIR = "/home/coldenergia/video";
    //private static final String LEFT_DIR = "/home/coldenergia/audio";
    //private static final String LEFT_DIR = "/home/coldenergia/docs";
    private static final String LEFT_DIR = "/home/coldenergia/images";

    //private static final String RIGHT_DIR = "/run/media/coldenergia/SP PHD U3/ce/video";
    //private static final String RIGHT_DIR = "/run/media/coldenergia/SP PHD U3/ce/audio";
    //private static final String RIGHT_DIR = "/run/media/coldenergia/SP PHD U3/ce/docs";
    private static final String RIGHT_DIR = "/run/media/coldenergia/SP PHD U3/ce/images";

    public static void main(String[] args) {
        DirComparator dirComparator = new DirComparator();
        DifferenceCollector differenceCollector = dirComparator.compareDirectories(LEFT_DIR, RIGHT_DIR);
        OutputPresenter outputPresenter = new OutputPresenter(differenceCollector, Side.RIGHT);
        //OutputPresenter outputPresenter = new OutputPresenter(differenceCollector);
        outputPresenter.outputDifferences();
    }
}
