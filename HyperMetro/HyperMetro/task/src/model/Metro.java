package model;

import java.util.HashMap;
import utils.RouteCalculator;

public class Metro {

    private HashMap<String, Line> lines;
    private RouteCalculator routeCalculator;

    public Metro() {
        lines = new HashMap<>();
        routeCalculator = new RouteCalculator(this);
    }

    public boolean addLane(String entry) {
        if (lines.get(entry) == null) {
            lines.put(entry, new Line(entry));
            return true;
        } else {
            return false;
        }
    }

    public void connectStations(String laneA,
                                String stationA,
                                String laneB,
                                String stationB) {
        this.getLine(laneA)
            .getStation(stationA)
            .setTransfer(laneB, stationB);

        this.getLine(laneB)
            .getStation(stationB)
            .setTransfer(laneA, stationA);
    }

    public Line getLine(String line) {
        if (lines.get(line) == null && line.contains("Jubilee") && !line.contains("line")) {
            // F. hack for corrupted input data on station Jubelee
            line += " line";
        }
        return lines.get(line);
    }

    public String getShortestRoute(String startLine,
                                   String startStation,
                                   String endLine,
                                   String endStation) {

        return routeCalculator.setRouteFrom(startLine, startStation)
                              .setRouterTo(endLine, endStation)
                              .getShortestRoute();
    }

    public String getFastestRoute(String startLine,
                                  String startStation,
                                  String endLine,
                                  String endStation) {

        return routeCalculator.setRouteFrom(startLine, startStation)
                              .setRouterTo(endLine, endStation)
                              .getFastestRoute();
    }
}
