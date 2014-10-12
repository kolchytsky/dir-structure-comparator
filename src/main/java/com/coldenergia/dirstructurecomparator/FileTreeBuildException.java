package com.coldenergia.dirstructurecomparator;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 10:31 PM
 */
public class FileTreeBuildException extends RuntimeException {

    public FileTreeBuildException() {
    }

    public FileTreeBuildException(String message) {
        super(message);
    }

    public FileTreeBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileTreeBuildException(Throwable cause) {
        super(cause);
    }
}
