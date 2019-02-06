package server.io;

import server.constants.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Class containing helper method to perform operations on .txt files.
 */
public class TxtFilesHelper {

    /**
     * Writes content on a file.
     *
     * @param filePath path of the file.
     * @param content  content we want to write on a single line.
     */
    public static void write(String filePath, String content) {
        try {
            writeOnFile(filePath, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc} from the caller.
     */
    private static void writeOnFile(String filePath, String line) throws IOException {
        File file = new File(filePath);
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));

        printWriter.println(line);
        printWriter.close();
    }

    /**
     * Clears the entire file.
     *
     * @param filePath path of the file.
     */
    public static void clear(String filePath) {
        try {
            clearOnFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc} from the caller.
     */
    private static void clearOnFile(String filePath) throws IOException {
        File file = new File(filePath);
        // We open the file with append=false in order to rewrite the content
        // by adding an empty string.
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, false));

        printWriter.print(Constants.EMPTY_FILE);
        printWriter.close();
    }

    /**
     * Get all the lines on a specific file.
     *
     * @param filePath path of the file.
     * @return a list containing all the lines of the file.
     */
    public static List<String> getAllLines(String filePath) {
        try {
            return getAllLinesOnFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc} from the caller.
     */
    private static List<String> getAllLinesOnFile(String filePath) throws IOException {
        createFileIfNotExists(filePath);

        return Files.readAllLines(Paths.get(filePath));
    }

    /**
     * Creates a file if is not existing on the disk.
     *
     * @param filePath path of the file.
     * @return true if the file didn't exist and has been created false otherwise.
     * @throws IOException If an IO error occurred.
     */
    private static boolean createFileIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        return file.createNewFile();
    }
}
