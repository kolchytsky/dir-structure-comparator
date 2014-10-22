package com.coldenergia.dirstructurecomparator.builder;

import com.coldenergia.dirstructurecomparator.filetree.diff.DetachedFile;

/**
 * User: coldenergia
 * Date: 10/15/14
 * Time: 9:28 PM
 */
public class DetachedFileBuilder {

    private DetachedFile detachedFile;

    public DetachedFileBuilder() {
        detachedFile = new DetachedFile("construction", true);
    }

    public DetachedFile build() {
        return detachedFile;
    }

    public DetachedFileBuilder withFileName(String fileName) {
        detachedFile.setFileName(fileName);
        return this;
    }

    public DetachedFileBuilder withIsDirectory(boolean isDirectory) {
        detachedFile.setDirectory(isDirectory);
        return this;
    }
}
