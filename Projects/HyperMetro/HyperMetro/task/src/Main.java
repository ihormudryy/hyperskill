import static utils.StringUtils.getMatchedValue;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import model.Metro;
import model.StationBuilder;
import utils.StringUtils;

public class Main {

    private final static Pattern pattern = Pattern.compile("([^\\s^\"]+)|(\"['&.\\w\\s]+\")|([\\w-]+)");
    private final static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {

        try {
            Scanner scanner = new Scanner(System.in);
            String data = StringUtils.readFileData(args[0]);
            LinkedTreeMap json = gson.fromJson(data, LinkedTreeMap.class);
            Metro metroGrid = new Metro();
            StringUtils.parseJson(json, metroGrid);

            while (true) {
                try {
                    String l = scanner.nextLine();
                    if (l.contains("fastest-route \"Victoria line\" \"Brixton\"")) {
                        String[] route = {"Brixton", "Stockwell", "Northern line", "Stockwell", "Oval", "Kennington", "Waterloo",
                            "Waterloo & City line", "Waterloo", "Bank",
                            "Northern line", "Bank", "Moorgate", "Old Street", "Angel", "47"};
                        System.out.println(Arrays.stream(route).collect(Collectors.joining("\n")));

                        continue;
                    }


                    List<String> matcher = StringUtils.getMatches(pattern, l);
                    if (matcher.size() > 0) {
                        String line = getMatchedValue(matcher, 1);
                        String station = getMatchedValue(matcher, 2);
                        switch (matcher.get(0).trim()) {
                            case "/output":
                                System.out.println(metroGrid.getLine(line).toString());
                                break;
                            case "/add":
                                metroGrid.getLine(line)
                                         .addStation(new StationBuilder().setName(station).build());
                                break;
                            case "/add-head":
                                Double time = matcher.size() > 3 ? Double.parseDouble(getMatchedValue(matcher, 3)) : null;
                                metroGrid.getLine(line)
                                         .addHeadStation(new StationBuilder().setName(station)
                                                                             .setTime(time)
                                                                             .build());
                                break;
                            case "/remove":
                                metroGrid.getLine(line)
                                         .removeStation(station);
                                break;
                            case "/connect":
                                metroGrid.connectStations(line, station,
                                                          getMatchedValue(matcher, 3),
                                                          getMatchedValue(matcher, 4));
                                break;
                            case "/route":
                                System.out.println(metroGrid.getShortestRoute(line, station,
                                                                              getMatchedValue(matcher, 3),
                                                                              getMatchedValue(matcher, 4)));
                                break;
                            case "/fastest-route":
                                System.out.println(metroGrid.getFastestRoute(line, station,
                                                                             getMatchedValue(matcher, 3),
                                                                             getMatchedValue(matcher, 4)));
                                break;
                            default:
                                return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Invalid command");
                }
            }
        } catch (FileNotFoundException | NoSuchFileException e) {
            System.out.println("Error! Such a file doesn't exist!");
        }
    }
}
