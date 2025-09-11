package JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonReader {
    public Map<String, List<String>> read() {
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
}
