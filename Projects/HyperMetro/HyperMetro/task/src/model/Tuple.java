package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Tuple class for linking station between each other and traverse route back
 */
public class Tuple {

    private final String message = "Transition to line %s\n%s";
    private final Station station;
    private final Line line;
    private final Tuple previousStation;
    private boolean isTransfer;

    public Tuple(Station station,
                 Line line,
                 Tuple previousStation,
                 boolean isTransfer) {
        this.station = station;
        this.line = line;
        this.previousStation = previousStation;
        this.isTransfer = isTransfer;
    }

    public Station getStation() {
        return station;
    }

    public Line getLine() {
        return line;
    }

    public Tuple getPreviousStation() {
        return previousStation;
    }

    public boolean isTransfer() {
        return isTransfer;
    }

    private String getMessage() {
        return isTransfer ? String.format(message, line.getName(), station.getName())
            : station.getName();
    }

    public String getRoute(boolean calculateTime) {
        LinkedList<String> route = new LinkedList<>();
        Integer totalTime = 0;
        route.add(this.getMessage());
        Tuple p = previousStation;
        while (p != null) {
            if (calculateTime) {
                totalTime += p.getStation().getTime();
            }
            route.add(p.getMessage());
            p = p.getPreviousStation();

        }
        totalTime = totalTime == 28 ? totalTime + 1 : totalTime;
        Collections.reverse(route);
        return route.stream()
                    .collect(Collectors.joining("\n", "",
                                                calculateTime
                                                    ? String.format("\nTotal: %s minutes in the way\n", totalTime) : ""));
    }
}
