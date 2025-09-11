package Generator.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReaderClass {
    public static List<String> readNameList() {
        List<String> nameList = null;
        try {
            nameList = Files.readAllLines(Path.of("src/Files/names.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameList;
    }

    public static List<String> readSurnameList() {
        List<String> surnameList = null;
        try {
            surnameList = Files.readAllLines(Path.of("src/Files/surnames.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return surnameList;
    }

    public static List<String> readColors() {
        List<String> colors;
        try {
            colors = Files.readAllLines(Path.of("src/Files/colors.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return colors;
    }
}
