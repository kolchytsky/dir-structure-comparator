package com.coldenergia.dirstructurecomparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * User: coldenergia
 * Date: 10/12/14
 * Time: 4:07 PM
 */
public class FileScaffolding {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileScaffolding.class);

    public static Path createFile(Path parentDirPath, String fileName) throws IOException {
        Path file = parentDirPath.resolve(fileName);
        Files.createFile(file);
        LOGGER.debug("Created " + file.toAbsolutePath().normalize());
        return file;
    }

    public static Path createFile(Path parentDirPath, String fileName, long fileSize) throws IOException {
        Path file = createFile(parentDirPath, fileName);
        RandomAccessFile f = new RandomAccessFile(file.toFile(), "rw");
        f.setLength(fileSize);
        return file;
    }

    public static Path createDirectory(Path parentDirPath, String dirName) throws IOException {
        Path dir = parentDirPath.resolve(dirName);
        Files.createDirectory(dir);
        LOGGER.debug("Created " + dir.toAbsolutePath().normalize());
        return dir;
    }

    public static void createTmpDir(Path tmpDirPath) throws IOException {
        Files.createDirectory(tmpDirPath);
        LOGGER.debug("Created a temp dir: " + tmpDirPath.toAbsolutePath().normalize());
    }

    public static void deleteTmpDir(Path tmpDirPath) throws IOException {
        Files.walkFileTree(tmpDirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
        LOGGER.debug("Deleted the temp dir: " + tmpDirPath.toAbsolutePath().normalize());
    }
}
