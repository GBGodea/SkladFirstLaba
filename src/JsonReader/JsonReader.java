package JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class JsonReader {
    public static Map<String, List<String>> readItems() {
        Map<String, List<String>> map = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/Files/items.json"))) {
            String line;
            String category;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.equals("{")
                        || line.equals("}")) {
                    continue;
                }

                line = line.trim();
                category = line.substring(
                        line.indexOf("\"") + 1,
                        line.lastIndexOf(":") - 1
                );
                String[] arr = line.substring(line.indexOf("[") + 1, line.indexOf("]"))
                        .replaceAll("\"", "").replaceAll(",", "")
                        .split(" ");

                map.put(category, Arrays.stream(arr).toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static Map<String, Map<String, List<String>>> readNames() {
        Map<String, Map<String, List<String>>> result = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/Files/names.json"))) {
            String line;
            String currentGender = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"male\"") || line.startsWith("\"female\"")) {
                    currentGender = line.substring(1, line.indexOf("\"", 1));
                    result.put(currentGender, new LinkedHashMap<>());
                } else if (line.startsWith("\"firstNames\"")) {
                    List<String> firstNames = parseArray(line, reader);
                    result.get(currentGender).put("firstNames", firstNames);
                } else if (line.startsWith("\"lastNames\"")) {
                    List<String> lastNames = parseArray(line, reader);
                    result.get(currentGender).put("lastNames", lastNames);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static List<String> parseArray(String firstLine, BufferedReader reader) throws Exception {
        StringBuilder sb = new StringBuilder();
        int startIdx = firstLine.indexOf('[');
        int endIdx   = firstLine.indexOf(']');

        if (endIdx > startIdx) {
            sb.append(firstLine, startIdx + 1, endIdx);
        } else {
            sb.append(firstLine.substring(startIdx + 1));
            String line;
            while ((line = reader.readLine()) != null) {
                int idx = line.indexOf(']');
                if (idx >= 0) {
                    sb.append(line, 0, idx);
                    break;
                }
                sb.append(line);
            }
        }

        String[] tokens = sb.toString()
                .replaceAll("\"", "")
                .split("\\s*,\\s*");
        List<String> list = new ArrayList<>();
        for (String t : tokens) {
            if (!t.isBlank()) {
                list.add(t);
            }
        }
        return list;
    }
}
