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
    public static void write(String filePath, String line) {
        try {
            writeOnFile(filePath, line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeOnFile(String filePath, String line) throws IOException {
        File file = new File(filePath);
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));

        printWriter.println(line);
        printWriter.close();
    }

    public static void clear(String filePath) {
        try {
            clearOnFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearOnFile(String filePath) throws IOException {
        File file = new File(filePath);
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, false));

        printWriter.print(Constants.EMPTY_FILE);
        printWriter.close();
    }

    public static List<String> getAllLines(String filePath) {
        try {
            return getAllLinesOnFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private static List<String> getAllLinesOnFile(String filePath) throws IOException {
        createFileIfNotExists(filePath);

        return Files.readAllLines(Paths.get(filePath));
    }

    private static boolean createFileIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        return file.createNewFile();
    }
}
