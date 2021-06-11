package utils;

import com.google.gson.internal.LinkedTreeMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import model.Metro;
import model.StationBuilder;

public class StringUtils {

    public static String readFileData(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public static String getMatchedValue(List<String> matchedValues, int position) {
        return matchedValues.size() > position ?
            matchedValues.get(position)
                         .trim()
                         .replace("\"", "") : null;
    }

    public static List<String> getMatches(Pattern pattern, String text) {
        return pattern.matcher(text.replace("> ", ""))
                      .results()
                      .map(r -> r.group())
                      .collect(Collectors.toList());

    }

    public static void parseJson(LinkedTreeMap metro, Metro output) {
        metro.keySet().stream()
             .filter(line -> output.addLane((String) line))
             .forEach(line -> {
                 ArrayList stations = (ArrayList) metro.get(line);
                 stations.stream()
                         .map(s -> (LinkedTreeMap) s)
                         .forEach(props -> {
                             output.getLine((String) line)
                                   .addStation(new StationBuilder()
                                                   .setName((String) ((LinkedTreeMap<?, ?>) props).get("name"))
                                                   .setTime((Double) ((LinkedTreeMap<?, ?>) props).get("time"))
                                                   .setNextStation((ArrayList) ((LinkedTreeMap<?, ?>) props).get("next"))
                                                   .setPrevStation((ArrayList) ((LinkedTreeMap<?, ?>) props).get("prev"))
                                                   .setTransfers((ArrayList<LinkedTreeMap>) ((LinkedTreeMap<?, ?>) props).get("transfer"))
                                                   .build());
                         });
                 output.getLine((String) line)
                       .buildLine();
             });
    }
}

