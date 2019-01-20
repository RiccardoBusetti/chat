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
    public static void write(String fileName, String line) {
        try {
            writeOnFile(fileName, line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeOnFile(String fileName, String line) throws IOException {
        File file = new File(Constants.BASE_DIR + fileName + Constants.TXT_EXTENSION);
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));

        printWriter.println(line);
        printWriter.close();
    }

    public static void clear(String fileName) {
        try {
            clearOnFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearOnFile(String fileName) throws IOException {
        File file = new File(Constants.BASE_DIR + fileName + Constants.TXT_EXTENSION);
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, false));

        printWriter.print(Constants.EMPTY_FILE);
        printWriter.close();
    }

    public static List<String> getAllLines(String fileName) {
        try {
            return getAllLinesOnFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private static List<String> getAllLinesOnFile(String fileName) throws IOException {
        return Files.readAllLines(Paths.get(Constants.BASE_DIR + fileName + Constants.TXT_EXTENSION));
    }
}
