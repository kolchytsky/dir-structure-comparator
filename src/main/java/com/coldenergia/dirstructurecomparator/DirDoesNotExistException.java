package com.coldenergia.dirstructurecomparator;

/**
 * User: coldenergia
 * Date: 10/11/14
 * Time: 7:10 PM
 */
public class DirDoesNotExistException extends FileTreeBuildException {

    public DirDoesNotExistException() {}

    public DirDoesNotExistException(String message) {
        super(message);
    }

    public DirDoesNotExistException(Throwable cause) {
        super(cause);
    }

    public DirDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
