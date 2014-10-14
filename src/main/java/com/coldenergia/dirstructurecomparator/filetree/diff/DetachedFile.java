package com.coldenergia.dirstructurecomparator.filetree.diff;

import java.util.Objects;

/**
 * Represents a file detached from the file tree. This file is parent-directory-agnostic.
 * User: coldenergia
 * Date: 10/14/14
 * Time: 9:10 PM
 */
public class DetachedFile {

    private String fileName;

    private boolean isDirectory;

    public DetachedFile(String fileName) {
        this.fileName = fileName;
    }

    public DetachedFile(String fileName, boolean directory) {
        this.fileName = fileName;
        isDirectory = directory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DetachedFile{");
        sb.append("fileName='").append(fileName).append('\'');
        sb.append(", isDirectory=").append(isDirectory);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(this.getFileName(), this.isDirectory());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DetachedFile)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        DetachedFile otherDetachedFile = (DetachedFile) obj;
        boolean areFileNamesEqual = Objects.equals(this.getFileName(), otherDetachedFile.getFileName());
        boolean areBothEitherFilesOrDirs = this.isDirectory() == otherDetachedFile.isDirectory();
        boolean equals = areFileNamesEqual && areBothEitherFilesOrDirs;
        return equals;
    }
}
